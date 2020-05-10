package com.android.petprog.top10downloader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.petprog.top10downloader.FeedEntry
import com.android.petprog.top10downloader.R
import com.android.petprog.top10downloader.getProgressDrawable
import com.android.petprog.top10downloader.loadImage
import com.skyhope.showmoretextview.ShowMoreTextView
import com.squareup.picasso.Picasso


class ViewHolder(v: View) {
    val tvName: TextView = v.findViewById(R.id.tvName)
    val tvArtist: TextView = v.findViewById(R.id.tvArtist)
    val tvSummary: ShowMoreTextView = v.findViewById(R.id.tvSummary)
    val image: ImageView = v.findViewById(R.id.imageView)

}

class FeedAdapter(
    context: Context,
    private val resource: Int,
    private var applications: List<FeedEntry>
) : ArrayAdapter<FeedEntry>(context, resource) {
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
        } else {
            view = convertView
            viewHolder = ViewHolder(view)
        }
        val currentApp = applications[position]
        viewHolder.tvName.text = currentApp.name
        viewHolder.tvArtist.text = currentApp.artist
        viewHolder.tvSummary.text = currentApp.summary
//        viewHolder.image.loadImage(
//            currentApp.imageURL,
//            getProgressDrawable(parent.context)
//        )

        viewHolder.tvSummary.setShowingLine(4)
        viewHolder.tvSummary.addShowMoreText("Show more")
        viewHolder.tvSummary.addShowLessText("Show ess")
        val color = ContextCompat.getColor(context, R.color.colorAccent)
        viewHolder.tvSummary.setShowMoreColor(color)
        viewHolder.tvSummary.setShowLessTextColor(color)

        Picasso.get()
            .load(currentApp.imageURL)
            .error(R.drawable.baseline_broken_image_black_48)
            .placeholder(getProgressDrawable(parent.context))
            .into(viewHolder.image)

        return view
    }

    override fun getCount(): Int {
        return applications.size
    }

    fun setFeedList(feedList: List<FeedEntry>) {
        applications = feedList
        notifyDataSetChanged()
    }


}