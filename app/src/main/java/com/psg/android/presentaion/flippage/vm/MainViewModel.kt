package com.psg.android.presentaion.flippage.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psg.android.presentaion.flippage.data.PageRepository



class MainViewModel : ViewModel() {
    private val repo = PageRepository()


    private val _pages = MutableLiveData<List<String>>()
    val pages: LiveData<List<String>> = _pages


    fun setPages(urls: List<String>) {
        _pages.value = repo.getPages(urls)
    }
}