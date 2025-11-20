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


        webView = findViewById(R.id.hiddenWebView)
        pageCurlView = findViewById(R.id.pageCurlView)

        setupWebView()
        loadWebsite("https://en.wikipedia.org/wiki/Android_(operating_system)")


    }

        private fun setupWebView() {
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
        }

        private fun loadWebsite(url: String) {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String?) {
                    super.onPageFinished(view, url)

                    webView.post {
                        val pages = splitWebViewIntoPages(webView)
                        pageCurlView.setPages(pages)
                    }
                }
            }
            webView.loadUrl(url)
        }

        private fun splitWebViewIntoPages(webView: WebView): List<Bitmap> {

            val width = webView.measuredWidth
            val totalHeight = (webView.contentHeight * webView.scale).toInt()

            val pageHeight = webView.height

            val pages = mutableListOf<Bitmap>()

            var currentY = 0

            while (currentY < totalHeight) {
                val bitmap = Bitmap.createBitmap(width, pageHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                canvas.translate(0f, -currentY.toFloat())
                webView.draw(canvas)

                pages.add(bitmap)
                currentY += pageHeight
            }

            return pages
        }
    }
