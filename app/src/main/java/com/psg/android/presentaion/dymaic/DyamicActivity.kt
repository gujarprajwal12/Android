package com.psg.android.presentaion.dymaic

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.psg.android.R
import com.psg.android.databinding.ActivityDyamicBinding

class DyamicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDyamicBinding
    private lateinit var web: WebView
    private lateinit var pager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDyamicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        web = binding.hiddenWeb
        pager = binding.viewPager

        initWebView()
        loadWebsite("https://en.wikipedia.org/wiki/Android_(operating_system)")
    }

    private fun initWebView() {
        val settings = web.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadsImagesAutomatically = true
        web.webChromeClient = WebChromeClient()
    }



    private fun loadWebsite(url: String) {

        binding.progress.visibility = View.VISIBLE
        web.visibility = View.VISIBLE   // MUST BE VISIBLE HERE
        pager.visibility = View.GONE

        web.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {

                // Wait until WebView is fully measured
                web.postDelayed({
                    checkWebViewReady()
                }, 1500)
            }
        }

        web.loadUrl(url)
    }

    private fun checkWebViewReady() {

        val width = web.width
        val height = (web.contentHeight * web.scale).toInt()

        // If not ready, try again
        if (width <= 10 || height <= 10) {
            web.postDelayed({ checkWebViewReady() }, 500)
            return
        }

        captureFullPage()  // Now safe
    }

    private fun captureFullPage() {

        val width = web.width
        val height = (web.contentHeight * web.scale).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        web.draw(canvas)

        val pages = splitBitmap(bitmap)

        pager.adapter = PageAdapter(pages)
        pager.setPageTransformer(FlipPageTransformer())

        pager.visibility = View.VISIBLE
        web.visibility = View.GONE    // Hide AFTER captured
        binding.progress.visibility = View.GONE
    }

    private fun splitBitmap(bitmap: Bitmap): List<Bitmap> {

        val pageHeight = 1500

        val pages = mutableListOf<Bitmap>()
        var y = 0

        while (y < bitmap.height) {

            val h = if (y + pageHeight > bitmap.height)
                bitmap.height - y else pageHeight

            pages.add(
                Bitmap.createBitmap(bitmap, 0, y, bitmap.width, h)
            )

            y += pageHeight
        }

        return pages
    }

}
