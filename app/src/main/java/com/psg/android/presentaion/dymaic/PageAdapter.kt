package com.psg.android.presentaion.dymaic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psg.android.R
import android.webkit.WebView
import android.webkit.WebViewClient
import android.graphics.Bitmap
import android.widget.ImageView


class PageAdapter(private val pages: List<Bitmap>) :
    RecyclerView.Adapter<PageAdapter.PageHolder>() {

    class PageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.pageImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_page, parent, false)
        return PageHolder(view)
    }

    override fun onBindViewHolder(holder: PageHolder, position: Int) {
        holder.img.setImageBitmap(pages[position])
    }

    override fun getItemCount(): Int = pages.size
}

