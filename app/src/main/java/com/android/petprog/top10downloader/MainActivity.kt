package com.android.petprog.top10downloader

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.petprog.top10downloader.adapter.FeedAdapter
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val FEED_URL_STATE = "feedurl"
private const val FEED_LIMIT_STATE = "feedlimit"

class MainActivity : AppCompatActivity() {
    private var feedUrl =
        "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit = 10
    private val feedViewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate() called")

        val feedAdapter = FeedAdapter(this, R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView.adapter = feedAdapter
        if (savedInstanceState != null) {
            feedUrl = savedInstanceState.getString(FEED_URL_STATE, "")
            feedLimit = savedInstanceState.getInt(FEED_LIMIT_STATE)
        }
        feedViewModel.feedEntries.observe(this, Observer { feedAdapter.setFeedList(it) })
        feedViewModel.stringTitle.observe(this, Observer { tvTitle.text = it })

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        Log.d(TAG, "onCreate() done")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(FEED_URL_STATE, feedUrl)
        outState.putInt(FEED_LIMIT_STATE, feedLimit)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)

        if (feedLimit == 10) {
            menu?.findItem(R.id.menu10)?.isChecked = true
        } else {
            menu?.findItem(R.id.menu25)?.isChecked = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuFree ->
                feedUrl =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"

            R.id.menuPaid ->
                feedUrl =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"

            R.id.menuSongs ->
                feedUrl =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topSongs/limit=%d/xml"

            R.id.menuAlbums ->
                feedUrl =
                    "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=%d/xml"
            R.id.menuNew -> feedUrl =
                "https://rss.itunes.apple.com/api/v1/us/apple-music/new-releases/all/10/explicit.atom"

            R.id.menu10, R.id.menu25 -> {
                if (item.isChecked) {
                    Log.d(TAG, "onOptionsItemSelected: ${item.title} setting feedLimit unchanged")
//                    return true
                } else {
                    item.isChecked = true
                    feedLimit = 35 - feedLimit
                    Log.d(
                        TAG,
                        "onOptionsItemSelected: ${item.title} setting feedLimit to $feedLimit"
                    )
                }
            }
            R.id.menuRefresh -> feedViewModel.invalidate()
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    companion object {
    }


}
