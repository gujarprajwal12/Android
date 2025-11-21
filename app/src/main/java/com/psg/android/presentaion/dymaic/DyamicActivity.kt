package com.psg.android.presentaion.dymaic


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.psg.android.R
import com.psg.android.databinding.ActivityDyamicBinding

import android.graphics.Canvas
import android.view.View

import android.webkit.WebView
import android.webkit.WebViewClient
class DyamicActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDyamicBinding
    private lateinit var webView: WebView
    private lateinit var pageCurlView: PageCurlView

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

        webView = binding.hiddenWebView
        pageCurlView = binding.pageCurlView

        initWebView()
        loadWebsite("https://en.wikipedia.org/wiki/Android_(operating_system)")
    }

    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
    }

    private fun loadWebsite(url: String) {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)

                webView.postDelayed({
                    generatePages()
                }, 600) // Delay ensures content fully rendered
            }
        }
        webView.loadUrl(url)
    }

    private fun generatePages() {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(webView.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        webView.measure(widthSpec, heightSpec)
        webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)

        val totalHeight = webView.measuredHeight
        val width = webView.measuredWidth
        val pageHeight = pageCurlView.height

        if (width == 0 || totalHeight == 0 || pageHeight == 0) return

        val pages = mutableListOf<Bitmap>()
        var yOffset = 0

        while (yOffset < totalHeight) {
            val height = minOf(pageHeight, totalHeight - yOffset)

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            canvas.save()
            canvas.translate(0f, -yOffset.toFloat())
            webView.draw(canvas)
            canvas.restore()

            pages.add(bitmap)
            yOffset += height
        }

        pageCurlView.setPages(pages)
    }


    }
