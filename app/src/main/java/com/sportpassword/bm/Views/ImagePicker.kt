package com.sportpassword.bm.Views

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.appcompat.app.AlertDialog
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.github.babedev.dexter.dsl.runtimePermission
import com.sportpassword.bm.R
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import com.github.babedev.dexter.dsl.runtimePermission
import com.sportpassword.bm.Controllers.BaseActivity
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.edit_vc.*
import java.io.File
import java.io.FileOutputStream

/**
 * Created by ives on 2018/3/14.
 */
interface ImagePicker {
    val ACTION_CAMERA_REQUEST_CODE: Int
    val ACTION_PHOTO_REQUEST_CODE: Int
    val activity: BaseActivity
    val context: Context
    var imagePickerLayer: AlertDialog
    var alertView: View
    var currentPhotoPath: String
    var filePath: String
    var file: File?
    var imageView: ImageView

    fun initImagePicker(resource: Int) {
        imagePickerLayer = AlertDialog.Builder(context).create()
        alertView = context.layoutInflater.inflate(resource, null)

        val photoBtn = alertView.findViewById<Button>(R.id.image_picker_photo_btn)
        photoBtn.onClick {
            grantPhotoPermission()
        }
        val cameraBtn = alertView.findViewById<Button>(R.id.image_picker_camera_btn)
        cameraBtn.onClick {
            grantCameraPermission()
        }
        val removeBtn = alertView.findViewById<Button>(R.id.image_picker_remove_btn)
        removeBtn.onClick {
            removeImage()
        }
        val cancelBtn = alertView.findViewById<Button>(R.id.image_picker_cancel_btn)
        cancelBtn.onClick {
            closeImagePickerLayer()
        }
    }

    fun grantPhotoPermission() {
        activity.runtimePermission {
            permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                checked { report ->
                    if (report.areAllPermissionsGranted()) {
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        activity.startActivityForResult(intent, ACTION_PHOTO_REQUEST_CODE)
                    } else {
                        //closeImagePickerLayer()
                        activity.toast("由於您沒有同意使用照片的權限，因此無法設定代表圖")
                    }
                }
            }
        }
    }

    fun grantCameraPermission() {
        activity.runtimePermission {
            permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                checked { report ->
                    if (report.areAllPermissionsGranted()) {

                        val values = ContentValues(1)
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                        val fileUri = activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (intent.resolveActivity(activity.packageManager) != null) {
                            currentPhotoPath = fileUri.toString()
                            //println(currentPhotoPath)
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                            activity.startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
                        }
                    } else {
                        //closeImagePickerLayer()
                        activity.toast("由於您沒有同意使用照片的權限，因此無法設定代表圖")
                    }
                }
            }
        }
    }

    fun showImagePickerLayer() {
        imagePickerLayer!!.window.setGravity(Gravity.BOTTOM)
        imagePickerLayer!!.window.attributes.windowAnimations = R.style.DialogAnimation_2
        imagePickerLayer!!.window.setBackgroundDrawableResource(R.color.LAYER)

        imagePickerLayer!!.setView(alertView)
        imagePickerLayer!!.show()
    }
    fun closeImagePickerLayer() {
        imagePickerLayer!!.dismiss()
    }

    fun setImage(newFile: File?, url: String?){
        if (newFile != null) {
            Picasso.with(context)
                    .load(newFile)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView)
        } else {
            if (url != null) {
                Picasso.with(context)
                        .load(url)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(imageView)
            }
        }
    }

    fun removeImage() {
        Picasso.with(context)
                .load(R.drawable.nophoto)
                .into(imageView)
    }

    fun makeTempEmptyFile(): File? {
        val dir = File("" + Environment.getExternalStorageDirectory() + "/Android/data" + activity.applicationContext.packageName + "Files")
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                return null
            }
        }
        filePath = dir.path + File.separator + "temp.jpg"
        val mediaFile = File(filePath)
        return mediaFile
    }

    fun selectToFile(data: Intent) {
        val inputStream = activity.contentResolver.openInputStream(data.data)
        val fileOutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var byteRead: Int
        while (true) {
            byteRead = inputStream.read(buffer)
            if (byteRead == -1) break
            fileOutputStream.write(buffer, 0, byteRead)
        }
        fileOutputStream.close()
        inputStream!!.close()
    }

    fun cameraToFile() {
        val cursor = activity.contentResolver.query(Uri.parse(currentPhotoPath), Array(1) { android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null)
        cursor.moveToFirst()
        filePath = cursor.getString(0)
        cursor.close()
        file = File(filePath)
    }

    fun dealPhoto(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            //println(resultCode)
            //println(data.data)

            file = makeTempEmptyFile()
            //if (file != null) {
            selectToFile(data!!)
                //println(file)
            //}
            setImage(file, null)
            closeImagePickerLayer()
        } else {
            println(requestCode)
        }
    }

    fun dealCamera(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {

            cameraToFile()
            //println(file)
            setImage(file, null)
            closeImagePickerLayer()
        }
    }

}