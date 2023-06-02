package com.sportpassword.bm.bm_new.ui.base

import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.LoadingAnimation
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    protected var binding: VB? = null
        private set
    private var viewJob: Job? = null
    private var loadingAnimation: LoadingAnimation? = null
    inline fun <reified K : BaseViewModel> generateVM(): Lazy<K> {
        return stateViewModel(state = { intent.extras ?: Bundle() })
    }

    protected abstract fun initViewBinding(): VB
    protected abstract fun initParam(data: Bundle)
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun getViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingAnimation = LoadingAnimation(this)

        binding = initViewBinding().apply {
            setContentView(root)
        }

        if (savedInstanceState == null) {
            initParam(intent.extras ?: Bundle.EMPTY)
        }

        viewJob = getViewModel()?.eventFlow?.onEach {
            handleViewEvent(it)
        }?.launchIn(lifecycleScope)
        initView(savedInstanceState)

    }

    override fun onDestroy() {
        loadingAnimation?.stop()
        binding = null
        super.onDestroy()
    }

    protected open fun handleViewEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.Loading -> {
                showLoadingAnim()
            }
            is ViewEvent.Done -> {
                finishLoadingAnim()
            }
            is ViewEvent.NormalError -> {
                finishLoadingAnim()
                Toast.makeText(this, event.msg, Toast.LENGTH_SHORT).show()
                Timber.d("error:${this.javaClass.name}, ${event.msg} ")
            }
            is ViewEvent.UnknownError -> {
                finishLoadingAnim()
            }
            is ViewEvent.ServerError -> {
                Toast.makeText(this, "伺服器錯誤", Toast.LENGTH_SHORT).show()
                finishLoadingAnim()
            }
            is ViewEvent.SignOut -> {
                finishLoadingAnim()
                event.message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
                startActivity(intent)
            }
            else -> {}
        }

    }

    fun addFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name
    ) {
        supportFragmentManager.commit(supportFragmentManager.isStateSaved) {
            setCustomAnimations(R.anim.slide_in_right, 0)
            add(containerViewId, fragment, tag)
        }
    }

    fun replaceFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        addToBackStack: Boolean = true
    ) {
        supportFragmentManager.commit(supportFragmentManager.isStateSaved) {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
    }

    fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(0, android.R.anim.fade_out)
            hide(fragment)  //改在fragment中監聽onHiddenChanged, 隱藏後再remove(應用淡出效果)
            commit()
        }
    }

    protected fun popFragment(name: String? = null, include: Boolean = false): Boolean {
        supportFragmentManager.run {
            if (backStackEntryCount == 0) {
                return false
            }
            if (name != null) {
                popBackStack(name, if (include) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
            } else {
                popBackStack()
            }
            return true
        }
    }

    protected fun showDialog(fragment: DialogFragment) {
        val fm = supportFragmentManager
        if (fm.isStateSaved || fm.isDestroyed) {
            return
        }
        fragment.show(fm, fragment.javaClass.name)
    }

    fun clearFragments() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }


    fun showLoadingAnim() {
        loadingAnimation?.start()
    }

    fun finishLoadingAnim() {
        loadingAnimation?.stop()
    }
}