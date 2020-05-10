package com.android.petprog.top10downloader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "FeedViewModel"
val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel : ViewModel(), DownloadData.DownloadCallback {
    private var downloadData: DownloadData? = null
    private var cacheUrl = "invalid"

    private val feed = MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
        get() = feed
    private val title = MutableLiveData<String>()
    val stringTitle: LiveData<String>
        get() = title
    val loading = MutableLiveData<Boolean>()

    init {
        feed.postValue(EMPTY_FEED_LIST)
        loading.postValue(true)
    }

    fun downloadUrl(feedUrl: String) {
        Log.d(TAG, "downloadURL() called with url $feedUrl")
        loading.value = true
        if (cacheUrl != feedUrl) {
            Log.d(TAG, "downloadURL() starting AsyncTask ")
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)
            cacheUrl = feedUrl
            Log.d(TAG, "downloadUrl(): done")

        } else {
            Log.d(TAG, "downloadUrl not called at all")
        }
    }

    fun invalidate() {
        loading.value = true
        cacheUrl = "invalid"
    }

    override fun onDataAvailable(data: List<FeedEntry>, titleData: String) {
        Log.d(TAG, "onDataAvailable called")
        feed.value = data
        title.value = titleData
        loading.value = false
        Log.d(TAG, "onDataAvailable ends")

    }

    override fun onCleared() {
        Log.d(TAG, "Cancelling any pending downloads")
        downloadData?.cancel(true)
    }
}