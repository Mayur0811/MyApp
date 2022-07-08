package com.br.stocks.adapter

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.R
import com.br.stocks.databinding.InsuranceNewsItemBinding
import com.br.stocks.models.InsuranceNewsItem
import com.br.stocks.utils.AdViewHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class InsuranceNewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var newsItems: ArrayList<InsuranceNewsItem> = arrayListOf()
    private val ITEM_VIEW = 0
    private val AD_VIEW = 1
    private val ITEM_FEED_COUNT = 5
    private var activity: Activity? = null
    var onNewsItemClick: ((InsuranceNewsItem) -> Unit)? = null
    var inputFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa", Locale.ENGLISH)
    var outputFormat = SimpleDateFormat("dd MMM, yyyy hh:mm a",Locale.ENGLISH)

    fun setNewsItem(activity: Activity, newsItem: ArrayList<InsuranceNewsItem>) {
        this.activity = activity
        this.newsItems = newsItem
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(activity)
        return if (viewType == ITEM_VIEW) {
            val view: View =
                layoutInflater.inflate(R.layout.insurance_news_item, parent, false)
            InsuranceViewHolder(view)
        } else (if (viewType == AD_VIEW) {
            val view: View =
                layoutInflater.inflate(R.layout.layout_ad, parent, false)
            activity?.let { AdViewHolder(view, it) }
        } else {
            null
        })!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_VIEW) {
            //show ad on fifth position only
            val pos = if (position >= 5) {
                position - 1
            } else {
                position
            }
            //show ad on every fifth position
//            val pos = (position - Math.round((position / ITEM_FEED_COUNT).toDouble())).toInt()
            (holder as InsuranceViewHolder).bindData(newsItems[pos])

            holder.itemView.setOnClickListener {
                onNewsItemClick?.invoke(newsItems[pos])
            }
        } else if (holder.itemViewType == AD_VIEW) {
            (holder as AdViewHolder).bindAdData()
        }
    }

    override fun getItemCount(): Int {
        if (newsItems.size >= 5) {
            return newsItems.size + 1
//                    Math.round((newsItems.size / ITEM_FEED_COUNT).toDouble())
//                .toInt()
        }
        return newsItems.size

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 4) {
            if ((position + 1) % ITEM_FEED_COUNT == 0) {
                return AD_VIEW
            }
        }
        return ITEM_VIEW
    }

    inner class InsuranceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: InsuranceNewsItemBinding = InsuranceNewsItemBinding.bind(itemView)
        fun bindData(insuranceNewsItem: InsuranceNewsItem) {
            inputFormat.timeZone = TimeZone.getTimeZone("${Calendar.getInstance().timeZone}")

            binding.txtCaption.text = insuranceNewsItem.caption
            binding.txtNews.text=Html.fromHtml(insuranceNewsItem.news)
            val date = insuranceNewsItem.newsdate?.let { inputFormat.parse(it) }

            binding.txtDate.text = date?.let { outputFormat.format(it) }
        }

    }

}



