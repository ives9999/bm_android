package com.sportpassword.bm.Controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewTreeObserver
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.sportpassword.bm.R
import com.sportpassword.bm.Views.ShowTop2
import java.io.IOException
import android.view.ViewTreeObserver.OnGlobalLayoutListener

@SuppressLint("MissingPermission")
class ScanQRCodeVC : BaseActivity() {

    var top: ShowTop2? = null
    var scanHolder: SurfaceHolder? = null

    private lateinit var scanSV: SurfaceView
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector

    private lateinit var viewTreeObserver: ViewTreeObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode_vc)

        findViewById<ShowTop2>(R.id.top) ?. let {
            top = it
            it.showPrev(true)
            it.setTitle("掃描QRCode")
        }

        init()
        toScan()

        resultCallback()
    }

    override fun init() {
        findViewById<SurfaceView>(R.id.scanner) ?. let {
            scanSV = it
        }

        scanSV.apply {
            barcodeDetector =
                BarcodeDetector.Builder(this@ScanQRCodeVC).setBarcodeFormats(Barcode.ALL_FORMATS).build()

            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    cameraSource = CameraSource.Builder(this@ScanQRCodeVC, barcodeDetector)
                        .setRequestedPreviewSize(1920, 1080)
                        .setAutoFocusEnabled(true)
                        .build()
                }
            })
        }
    }

    private fun toScan() {

        scanSV.apply {
            holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        cameraSource.start(holder)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//                    try {
//                        cameraSource.start(holder)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
                }

                override fun surfaceDestroyed(p0: SurfaceHolder) {
                    cameraSource.stop()
                }
            })
        }

//        findViewById<SurfaceView>(R.id.scanner) ?. let {
//
//

//        }
    }

    fun resultCallback() {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_LONG).show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    val scannedValue: String = barcodes.valueAt(0).displayValue
                    runOnUiThread {
                        cameraSource.stop()
                        //Toast.makeText(this@ScanQRCodeVC, "$scannedValue", Toast.LENGTH_LONG).show()

                        val intent = Intent()
                        intent.putExtra("token", scannedValue)
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    }
                } else {
                    Toast.makeText(this@ScanQRCodeVC, "value- else", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}














