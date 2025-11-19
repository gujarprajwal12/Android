package com.psg.android.presentaion.page

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.psg.android.R
import com.psg.android.databinding.ActivityStaticBinding
import com.psg.android.databinding.ActivitySwitchBinding
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class StaticActivity : AppCompatActivity() {
    lateinit var binding: ActivityStaticBinding
    lateinit var imageList : ArrayList<Int>
    var imageCount : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStaticBinding.inflate(layoutInflater)
        setContentView(binding.root)



                imageList=ArrayList()

                imageList.add(R.drawable.page1)
                imageList.add(R.drawable.page2)

                var right_to_left_anim=AnimationUtils.loadAnimation(this@StaticActivity,R.anim.right_to_left).apply {
                    duration=700
                    interpolator=AccelerateDecelerateInterpolator()
                }

                var left_to_right_anim=AnimationUtils.loadAnimation(this@StaticActivity,R.anim.left_to_right).apply {
                    duration=700
                    interpolator=AccelerateDecelerateInterpolator()
                }

                binding.rootLayout.setOnTouchListener(object : View.OnTouchListener {
                    private val SWIPE_THRESHOLD = 100
                    private var downX = 0f
                    private var upX = 0f

                    override fun onTouch(view: View, event: MotionEvent): Boolean {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                downX = event.x
                                return true
                            }

                            MotionEvent.ACTION_UP -> {
                                upX = event.x
                                val deltaX = downX - upX
                                if (deltaX > SWIPE_THRESHOLD) {
                                    // Swipe from right to left detected
                                    right_to_left_anim.setAnimationListener(object :
                                        Animation.AnimationListener {
                                        override fun onAnimationStart(animation: Animation) {
                                            var handler = Handler()
                                            handler.postDelayed({
                                                if(imageCount==imageList.size-1){
                                                    imageCount=-1
                                                }
                                                imageCount++
                                                binding.imageView.setImageResource(imageList[imageCount])
                                            },100)
                                        }

                                        override fun onAnimationEnd(p0: Animation?) {
                                        }

                                        override fun onAnimationRepeat(p0: Animation?) {
                                        }

                                    })
                                    binding.circle2.startAnimation(right_to_left_anim)
                                }
                                else if(-deltaX > SWIPE_THRESHOLD){
                                    left_to_right_anim.setAnimationListener(object : Animation.AnimationListener{
                                        override fun onAnimationStart(p0: Animation?) {
                                            var handler=Handler()
                                            handler.postDelayed({
                                                if(imageCount==0){
                                                    imageCount=imageList.size
                                                }
                                                imageCount--
                                                binding.imageView.setImageResource(imageList[imageCount])
                                            },100)
                                        }

                                        override fun onAnimationEnd(p0: Animation?) {
                                        }

                                        override fun onAnimationRepeat(p0: Animation?) {
                                        }

                                    })
                                    binding.circle1.startAnimation(left_to_right_anim)
                                }
                                return true
                            }
                        }
                        return false
                    }

                })


            }
        }