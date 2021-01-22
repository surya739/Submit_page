package com.example.submit_profile

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.submit_profile.R
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.content.Intent
import android.app.Activity
import android.graphics.Bitmap
import android.content.ClipData
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.io.FileNotFoundException
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var imageIs: ImageView? = null
    private var nextpg: Button? = null
    private var previouspg: Button? = null
    private var pick_image: Button? = null
    private val imageuri: ArrayList<Uri>? = null
    var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextpg = findViewById(R.id.next)
        pick_image = findViewById(R.id.pink_images)
        previouspg = findViewById(R.id.previous)


        pick_image!!.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
                return@OnClickListener
            }
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            i.type = "image/*"
            startActivityForResult(i, 1)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {

            imageIs = findViewById(R.id.imageis)
            val bitmaps: MutableList<Bitmap> = ArrayList()
            val clipData = data!!.clipData

            if (clipData != null) {

                for (i in 0 until clipData.itemCount) {

                    val imageUri = clipData.getItemAt(i).uri

                    try {

                        val `is` = contentResolver.openInputStream(imageUri)
                        val bitmap = BitmapFactory.decodeStream(`is`)
                        bitmaps.add(bitmap)
                        imageIs!!.setImageBitmap(bitmaps[0])
                        position = 0

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            } else {

                val imageUri = data.data

                try {

                    val `is` = contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(`is`)
                    bitmaps.add(bitmap)
                    position = 0

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }


            Thread {
                for (b in bitmaps) {
                    runOnUiThread {
                        nextpg!!.setOnClickListener {
                            for (b in bitmaps) if (position > 0)
                                imageIs!!.setImageBitmap(b)
                            position++
                        }
                        previouspg!!.setOnClickListener {
                            if (position < bitmaps.size - 1)
                                imageIs!!.setImageBitmap(b)
                            position--
                        }
                    }
                }
            }.start()
        }
    }

    companion object {
        private const val PICK_Image_code = 0
    }
}