package com.psg.android.core

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.psg.android.R
import com.psg.android.databinding.ActivitySwitchBinding
import com.psg.android.presentaion.book.BookActivity
import com.psg.android.presentaion.flippage.ui.MainActivity

class SwitchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySwitchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySwitchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        switchbetweenpages()
    }

    private fun switchbetweenpages() {

        binding.btnmain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnbook.setOnClickListener {
            startActivity(Intent(this, BookActivity::class.java))
        }
    }
}