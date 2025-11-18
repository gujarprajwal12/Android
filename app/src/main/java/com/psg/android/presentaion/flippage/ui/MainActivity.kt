package com.psg.android.presentaion.flippage.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.psg.android.R
import com.psg.android.databinding.ActivityMainBinding
import com.psg.android.presentaion.flippage.vm.MainViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = HtmlPagerAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.setPageTransformer(FlipPageTransformer())

        vm.setPages(listOf(
            "https://gujarprajwal12.github.io/Android-Dev-Portfolio-Animated/",
            "https://developer.android.com/about",
            "https://www.github.com/"
        ))


        lifecycleScope.launch {
            vm.pages.observe(this@MainActivity) { pages ->
                adapter.submitList(pages)
            }
        }
    }
    }
