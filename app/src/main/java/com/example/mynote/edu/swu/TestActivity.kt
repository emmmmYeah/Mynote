package com.example.mynote.edu.swu


import android.app.Activity
import android.app.NotificationChannel
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mynote.R
import java.io.File


class TestActivity: AppCompatActivity()
     {
         val TAG = "@@MAIN"
         private val channelId = "1024"
         private val CODE_CAMERA = 1024
         private val CODE_ALBUM = 1025

         private var channel: NotificationChannel? = null

         private val AUTHORITY = "cn.swu.cs.lesson11.file_provider"

         private var output: Uri? = null

         private var mediaPlayer: MediaPlayer? = null
         private var videoView: VideoView? = null

         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)
             setContentView(R.layout.activity_test)


             findViewById<Button>(R.id.btn_take_photo).setOnClickListener {
                 takePhoto()
             }
             findViewById<Button>(R.id.btn_choose_album).setOnClickListener {
                 chooseAlbum()
             }

         }

         private var duration = 0
         private var finished = false




         private fun chooseAlbum() {
             // 1. 创建intent 开启相册
             // 2. 通过 startActivityFroResult
             // 3. 在回调方法中 获取选择结果
             val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
             intent.type = "image/*"
             intent.addCategory(Intent.CATEGORY_OPENABLE)

             ActivityCompat.startActivityForResult(this, intent, CODE_ALBUM, null)
         }



         private fun takePhoto() {
             // 1. 调用相机应用
             // 2. 传递保存路径
             // 3. 在回调函数 获取结果

             val file = File(externalCacheDir, "output.jpg")
             if (file.exists()) file.delete()
             file.createNewFile()

             output = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                 FileProvider.getUriForFile(this, AUTHORITY, file)
             } else {
                 file.toUri()
//            Uri.fromFile(file)
             }

             val intent = Intent("android.media.action.IMAGE_CAPTURE")
             intent.putExtra(MediaStore.EXTRA_OUTPUT, output)
             ActivityCompat.startActivityForResult(this, intent, CODE_CAMERA, null)
         }

         override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
             super.onActivityResult(requestCode, resultCode, data)

             when (requestCode) {
                 CODE_CAMERA -> {
                     if (resultCode == Activity.RESULT_OK) {
                         renderImgView(output!!)
                     }
                 }
                 CODE_ALBUM -> {
                     if (resultCode == Activity.RESULT_OK) {
                         data?.data?.let {
                             renderImgView(it)
                         }
                     }
                 }
             }

         }

         private fun renderImgView(output: Uri) {
             val fd = contentResolver.openFileDescriptor(output, "r")
             val imgView = findViewById<ImageView>(R.id.img_box)
             imgView.setImageBitmap(
                 BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor)
             )
         }



     }