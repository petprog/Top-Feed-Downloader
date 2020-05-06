package com.android.petprog.top10downloader

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private const val TAG = "DownloadData"

open class DownloadData(private val callback: DownloadCallback) :
    AsyncTask<String, Void, String>() {

    interface DownloadCallback {
        fun onDataAvailable(data: List<FeedEntry>, titleData: String)
    }

    override fun doInBackground(vararg url: String): String {
        // using vararg causes the actual value to be pass as an array
        Log.d(TAG, "doInBackground: starts with ${url[0]}")
        val rssFeed = downloadXML(url[0])
        if (rssFeed.isEmpty()) {
            Log.e(TAG, "doInBackground: Error downloading")
        }
        return rssFeed
    }

    override fun onPostExecute(result: String) {
        val parseApplications = ParseApplications()
        if (result.isNotEmpty()) {
            parseApplications.parse(result)
        }
        callback.onDataAvailable(parseApplications.applications, parseApplications.title)
    }

    private fun downloadXML(urlPath: String): String {
        var result = ""
        try {
            result = URL(urlPath).readText()
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    "doInBackground: Invalid URL: ${e.message}"
                }
                is IOException -> {
                    "doInBackground: IO Exception reading data: ${e.message}"
                }
                is SecurityException -> {
                    "doInBackground: Security Exception: Permission needed!: ${e.message}"
                }
                else -> {
                    "doInBackground: Unknown error: ${e.message}"
                }
            }
            Log.d(TAG, errorMessage)
        }
        return result
    }
}