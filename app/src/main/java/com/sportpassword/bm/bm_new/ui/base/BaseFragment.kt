package com.sportpassword.bm.bm_new.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sportpassword.bm.Utilities.LoadingAnimation
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected var binding: VB? = null
        private set
    private var viewJob: Job? = null
    private var loadingAnimation: LoadingAnimation? = null

    protected abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    protected abstract fun initParam(data: Bundle)
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun bindFragmentListener(context: Context)
    protected abstract fun getViewModel(): BaseViewModel?
    inline fun <reified K : BaseViewModel> generateVM(): Lazy<K> {
        return stateViewModel(state = { arguments ?: Bundle() })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindFragmentListener(context)
        activity?.onBackPressedDispatcher?.addCallback(this, onBackPressedCallback())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initParam(arguments ?: Bundle.EMPTY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = initViewBinding(inflater, container, savedInstanceState)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewJob()
        initView(savedInstanceState)
    }

    open fun setViewJob() {
        viewJob = getViewModel()?.eventFlow?.onEach {
            handleViewEvent(it)
        }?.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        binding = null
        viewJob?.cancel()
        viewJob = null
        loadingAnimation?.stop()
        super.onDestroyView()
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
                Toast.makeText(requireContext(), event.msg, Toast.LENGTH_SHORT).show()
                Timber.d("error:${this.javaClass.name}, ${event.msg} ")
            }
            is ViewEvent.UnknownError -> {
                finishLoadingAnim()
            }
            is ViewEvent.ServerError -> {
                Toast.makeText(requireContext(), "伺服器錯誤", Toast.LENGTH_SHORT).show()
                finishLoadingAnim()
            }
            is ViewEvent.SignOut -> {
                finishLoadingAnim()
                event.message?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
    }

    protected fun popFragment(name: String? = null, include: Boolean = false) {
        childFragmentManager.run {
            if (backStackEntryCount == 0) return
            if (name != null) {
                popBackStack(name, if (include) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
            } else {
                popBackStack()
            }
        }
    }

    fun replaceFragment(
        @IdRes containerViewId: Int,
        fragment: Fragment,
        tag: String? = fragment::class.java.name,
        addToBackStack: Boolean = true
    ) {
        parentFragmentManager.commit(parentFragmentManager.isStateSaved) {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            replace(containerViewId, fragment, tag)
            if (addToBackStack) {
                addToBackStack(tag)
            }
        }
    }

    protected fun showDialog(fragment: DialogFragment) {
        val fm = childFragmentManager
        if (fm.isStateSaved || fm.isDestroyed) {
            return
        }
        fragment.show(fm, fragment.javaClass.name)
    }


    open fun onBackPressedCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (this@BaseFragment.isVisible && childFragmentManager.backStackEntryCount != 0) {
                    popFragment()
                } else {
                    isEnabled = false
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        }
    }


    fun showLoadingAnim() {
        loadingAnimation?.start()
    }

    fun finishLoadingAnim() {
        loadingAnimation?.stop()
    }
}