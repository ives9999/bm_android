package com.sportpassword.bm.Utilities

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.Manifest.permission.*
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R

class PermissionManager constructor(private val activity: AppCompatActivity) {

    private val requiredPermissions = mutableListOf<Permission>()

    //提示訊息文字
    private var rationale: String? = null

    private var callback: (Boolean)-> Unit = {}

    private var detailedCallback: (Map<Permission, Boolean>)-> Unit = {}

    private val permissionCheck =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults->
            sendResultAndCleanUp(grantResults)
        }

    fun rationale(description: String): PermissionManager {
        rationale = description
        return this
    }

    fun request(vararg permission: Permission): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(callback: (Boolean)-> Unit) {
        this.callback = callback
        handelPermissionRequest()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkDetailPermission(callback: (Map<Permission, Boolean>) -> Unit) {
        this.detailedCallback = callback
        handelPermissionRequest()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handelPermissionRequest() {
        when {
            areAllPermissionsGranted() ->
                sendPositiveResult()
            shouldShowPermissionRationale() ->
                displayRationale()
            else -> requestPermissions()
        }
    }

    private fun displayRationale() {
        val customDialog = AlertDialog.Builder(activity)
        customDialog.setTitle("請求權限")
            .setMessage(rationale ?: "是否允許拍照的權限？")
            .setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_like_in_svg))
            .setPositiveButton("同意",
            DialogInterface.OnClickListener { dialogInterface, i ->
                requestPermissions()
            })
            .show()
    }

    private fun sendPositiveResult() {
        sendResultAndCleanUp(getPermissionList().associate { it to true })
    }

    private fun sendResultAndCleanUp(grantResults: Map<String, Boolean>) {
        callback(grantResults.all { it.value })
        detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
        cleanUp()
    }

    private fun cleanUp() {
        requiredPermissions.clear()
        rationale = null
        callback = {}
        detailedCallback = {}
    }

    private fun requestPermissions() {
        permissionCheck.launch(getPermissionList())
    }

    private fun areAllPermissionsGranted() =
        requiredPermissions.all { it.isGranted() }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldShowPermissionRationale() =
        requiredPermissions.any { it.requiresRationale(activity) }

    private fun getPermissionList() =
        requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()

    private fun Permission.isGranted() =
        permissions.all { hasPermission(it) }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun Permission.requiresRationale(activity: AppCompatActivity) =
        permissions.any { activity.shouldShowRequestPermissionRationale(it) }

    private fun hasPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    companion object {
        fun from(activity: AppCompatActivity) = PermissionManager(activity)
    }
}

sealed class Permission(vararg val permissions: String) {

    object Camera: Permission(CAMERA)

    object MandatoryForFeatureOne: Permission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

    object Location: Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    object Storage: Permission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object Notification: Permission(POST_NOTIFICATIONS)

    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            CAMERA -> Camera
            POST_NOTIFICATIONS -> Notification
            else -> throw java.lang.IllegalArgumentException("UnKnown permission: $permission")
        }
    }
}



























