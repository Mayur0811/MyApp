package com.br.stocks.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.R
import com.br.stocks.databinding.NewsItemBinding
import com.br.stocks.models.NewsItem
import com.br.stocks.utils.AdViewHolder
import java.util.*
import kotlin.collections.ArrayList


class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var newsItems: ArrayList<NewsItem> = arrayListOf()
    private var activity: Activity? = null

    private var onNewsItemClick: ((NewsItem) -> Unit)? = null
    private val ITEM_VIEW = 0
    private val AD_VIEW = 1
    private val ITEM_FEED_COUNT = 5

    fun setNewsItem(activity: Activity, newsItems: ArrayList<NewsItem>) {
        this.activity = activity
        this.newsItems = newsItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 4) {
            if ((position + 1) % ITEM_FEED_COUNT == 0) {
                return AD_VIEW
            }
        }

        return ITEM_VIEW
    }

    override fun getItemCount(): Int {
        if (newsItems.size >= 5) {
            return newsItems.size + 1
//                    Math.round((newsItems.size / ITEM_FEED_COUNT).toDouble())
//                .toInt()
        }
        return newsItems.size
    }

    inner class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: NewsItemBinding = NewsItemBinding.bind(itemView)
        private var inputFormat= java.text.SimpleDateFormat("HH:mm",Locale.ENGLISH)
        private var outputFormat= java.text.SimpleDateFormat("hh:mm aa",Locale.ENGLISH)

        private var dateInputFormat= java.text.SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH)
        private var dateOutputFormat= java.text.SimpleDateFormat("dd MMM, yyyy",Locale.ENGLISH)

        @SuppressLint("SetTextI18n")
        fun bindData(newsItem: NewsItem) {
            inputFormat.timeZone= TimeZone.getTimeZone("${Calendar.getInstance().timeZone}")
            binding.txtHeading.text = newsItem.heading
            binding.txtDate.text = newsItem.date?.let { date->
                dateInputFormat.parse(date)?.let { dateOutputFormat.format(it) }
            } + " " + newsItem.time?.let {time->
                inputFormat.parse(time)
                    ?.let { outputFormat.format(it) }
            }
            if (newsItem.caption != null) {
                binding.txtCaption.visibility = View.VISIBLE
                binding.txtCaption.text = newsItem.caption
            }

            itemView.setOnClickListener {
                onNewsItemClick?.invoke(newsItems[layoutPosition])
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_VIEW) {
            //show ad on fifth position only
            val pos=if (position >= 5){ position - 1 }else{ position }
            //show ad on every fifth position
//            val pos = (position - Math.round((position / ITEM_FEED_COUNT).toDouble())).toInt()
            (holder as NewViewHolder).bindData(newsItems[pos])
        } else if (holder.itemViewType == AD_VIEW) {
            (holder as AdViewHolder).bindAdData()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(activity)
        return if (viewType == ITEM_VIEW) {
            val view: View =
                layoutInflater.inflate(R.layout.news_item, parent, false)
            NewViewHolder(view)
        } else (if (viewType == AD_VIEW) {
            val view: View =
                layoutInflater.inflate(R.layout.layout_ad, parent, false)
            activity?.let { AdViewHolder(view, it) }
        } else {
            null
        })!!
    }


}