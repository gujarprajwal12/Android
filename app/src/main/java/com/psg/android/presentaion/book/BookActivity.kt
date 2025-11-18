package com.psg.android.presentaion.book


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.psg.android.R
import com.psg.android.databinding.ActivityBookBinding



class BookActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookBinding

    private lateinit var htmlPages: ArrayList<String>
    private var currentPage = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)


        htmlPages = arrayListOf(
            "https://gujarprajwal12.github.io/Android-Dev-Portfolio-Animated/",
            "https://developer.android.com/about",
            "https://github.com/"
        )


        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            cacheMode = WebSettings.LOAD_DEFAULT
        }


        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return true
            }
        }
        binding.webView.webChromeClient = WebChromeClient()


        binding.webView.loadUrl(htmlPages[currentPage])


        val rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left).apply {
            duration = 60
            interpolator = AccelerateDecelerateInterpolator()
        }
        val leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right).apply {
            duration = 60
            interpolator = AccelerateDecelerateInterpolator()
        }

        // SWIPE LISTENER ON WEBVIEW (Fix)
        binding.webView.setOnTouchListener(object : View.OnTouchListener {
            private val SWIPE_THRESHOLD = 150
            private var downX = 0f

            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {
                        downX = event.x
                        return false
                    }

                    MotionEvent.ACTION_UP -> {
                        val deltaX = downX - event.x

                        // Next Page
                        if (deltaX > SWIPE_THRESHOLD) {
                            rightToLeft.setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {
                                    Handler().postDelayed({
                                        currentPage =
                                            if (currentPage == htmlPages.size - 1) 0 else currentPage + 1
                                        binding.webView.loadUrl(htmlPages[currentPage])
                                    }, 150)
                                }

                                override fun onAnimationEnd(animation: Animation?) {}
                                override fun onAnimationRepeat(animation: Animation?) {}
                            })
                            binding.circle2.startAnimation(rightToLeft)
                        }

                        // Previous Page
                        else if (-deltaX > SWIPE_THRESHOLD) {
                            leftToRight.setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {
                                    Handler().postDelayed({
                                        currentPage =
                                            if (currentPage == 0) htmlPages.size - 1 else currentPage - 1
                                        binding.webView.loadUrl(htmlPages[currentPage])
                                    }, 150)
                                }

                                override fun onAnimationEnd(animation: Animation?) {}
                                override fun onAnimationRepeat(animation: Animation?) {}
                            })
                            binding.circle1.startAnimation(leftToRight)
                        }

                        return false
                    }
                }
                return false
            }
        })
    }
}