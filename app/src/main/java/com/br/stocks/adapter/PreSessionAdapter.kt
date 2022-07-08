package com.br.stocks.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.R
import com.br.stocks.databinding.ItemPreSessionBinding
import com.br.stocks.models.New
import com.br.stocks.utils.*
import java.util.*
import kotlin.collections.ArrayList

class PreSessionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var preSessionItem: ArrayList<New> = arrayListOf()
    private var activity: Activity? = null
    private val ITEM_VIEW = 0
    private val AD_VIEW = 1
    private val ITEM_FEED_COUNT = 5
    //  private var onNotificationItemClick: ((New) -> Unit)? = null

    fun setPreSessionItem(activity: Activity, notificationItem: ArrayList<New>) {
        this.activity = activity
        this.preSessionItem = notificationItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(activity)
        return if (viewType == ITEM_VIEW) {
            val view: View =
                layoutInflater.inflate(R.layout.item_pre_session, parent, false)
            PreSessionViewHolder(view)
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
            val pos=if (position >= 5){ position - 1 }else{ position }
            //show ad on every fifth position
//            val pos = (position - Math.round((position / ITEM_FEED_COUNT).toDouble())).toInt()
            (holder as PreSessionViewHolder).bindData(preSessionItem[pos])
        } else if (holder.itemViewType == AD_VIEW) {
            (holder as AdViewHolder).bindAdData()
        }
    }

    override fun getItemCount(): Int {
        if (preSessionItem.size >= 5) {
            return preSessionItem.size + 1
//                    Math.round((preSessionItem.size / ITEM_FEED_COUNT).toDouble())
//                .toInt()
        }
        return preSessionItem.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 4) {
            if ((position + 1) % ITEM_FEED_COUNT == 0) {
                return AD_VIEW
            }
        }
        return ITEM_VIEW
    }

    inner class PreSessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemPreSessionBinding = ItemPreSessionBinding.bind(itemView)
        private var inputFormat= java.text.SimpleDateFormat("HH:mm",Locale.ENGLISH)
        private var outputFormat= java.text.SimpleDateFormat("hh:mm aa",Locale.ENGLISH)

        private var dateInputFormat= java.text.SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH)
        private var dateOutputFormat= java.text.SimpleDateFormat("dd MMM, yyyy",Locale.ENGLISH)

        @SuppressLint("SetTextI18n")
        fun bindData(new: New) {
            inputFormat.timeZone= TimeZone.getTimeZone("${Calendar.getInstance().timeZone}")
            binding.txtDate.text = new.date?.let { date->
                dateInputFormat.parse(date)?.let { dateOutputFormat.format(it) }
            } + " " + new.time?.let {time->
                inputFormat.parse(time)
                    ?.let { outputFormat.format(it) }
            }

            binding.txtNews.text = new.section_name
            binding.txtCaption.text = new.heading

        }
    }
}