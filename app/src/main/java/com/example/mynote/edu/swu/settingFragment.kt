package com.example.mynote.edu.swu


import android.app.Activity
import android.app.NotificationChannel
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mynote.R
import com.example.mynote.databinding.FgQueryBinding
import com.example.mynote.databinding.FgSettingBinding
import java.io.File


class settingFragment:Fragment() {
    val TAG = "@@settingFragment"


    private val channelId = "1024"
    private val CODE_CAMERA = 1024
    private val CODE_ALBUM = 1025

    private var channel: NotificationChannel? = null

    private val AUTHORITY = "edu.swu.note"

    private var output: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "I'm OK1")
       // val view = inflater.inflate(R.layout.fg_setting, container, false)
      //  init(view)
        val binding = DataBindingUtil.inflate<FgSettingBinding>(inflater, R.layout.fg_setting, container, false);
        binding.lifecycleOwner=this
        init(binding.getRoot())
        return binding.getRoot();
        //return view
    }

    private fun init(view: View) {
        view.apply {
            findViewById<ImageView>(R.id.btn_take_photo).setOnClickListener {
                chooseAlbum()
            }
            findViewById<ImageView>(R.id.btn_camera).setOnClickListener {
                takePhoto()
            }
            findViewById<TextView>(R.id.item1).setOnClickListener {
                val intent = Intent(activity, MainActivity::class.java)
                startActivityForResult(intent,10001)
            }
        }
           /* val webView=findViewById<WebView>(R.id.item2)
            webView.settings.setJavaScriptEnabled(true)
            webView.webViewClient = WebViewClient()
            webView.loadUrl("https://www.baidu.com")


        }*/
    }
    private fun takePhoto() {

        val externalCacheDir= context?.getExternalFilesDir(null)

        val file = File(externalCacheDir, "output.jpg")
        if (file.exists()) file.delete()
        file.createNewFile()

        output = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this.requireContext(), AUTHORITY, file)
        } else {
            file.toUri()
//            Uri.fromFile(file)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output)
        this.activity?.let { ActivityCompat.startActivityForResult(it, intent, CODE_CAMERA, null) }
    }

    private fun chooseAlbum() {
        // 1. 创建intent 开启相册
        // 2. 通过 startActivityFroResult
        // 3. 在回调方法中 获取选择结果
        Log.d(TAG, "I'm OK2")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        activity?.let { ActivityCompat.startActivityForResult(it, intent, CODE_ALBUM, null) }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            CODE_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {

                        view?.let { it1 -> renderImgView(output!!, it1) }

                }
            }
            CODE_ALBUM -> {
                if (resultCode == Activity.RESULT_OK) {
                    intent?.data?.let {
                        view?.let { it1 -> renderImgView(it, it1) }
                    }
                }
            }
        }
    }
    private fun renderImgView(output: Uri, view: View?) {
        val contentResolve= getActivity()?.getContentResolver()

        val fd = contentResolve?.openFileDescriptor(output, "r")
        view?.apply {
            Log.d(TAG,"放图片...")
            val imgView = findViewById<ImageView>(R.id.img_box)
            imgView.setImageBitmap(
                BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor)

            )
        }
    }

}