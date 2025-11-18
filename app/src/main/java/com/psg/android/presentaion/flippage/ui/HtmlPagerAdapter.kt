package com.psg.android.presentaion.flippage.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.psg.android.databinding.ItemWebviewBinding


class HtmlPagerAdapter : ListAdapter<String, HtmlPagerAdapter.WebViewHolder>(Diff) {


    class WebViewHolder(private val binding: ItemWebviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            val webView: WebView = binding.webview
            webView.webViewClient = WebViewClient()
            val ws: WebSettings = webView.settings
            ws.javaScriptEnabled = true
            ws.domStorageEnabled = true
            webView.loadUrl(url)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewHolder {
        val binding = ItemWebviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WebViewHolder(binding)
    }


    override fun onBindViewHolder(holder: WebViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}