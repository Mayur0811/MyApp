package com.br.stocks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.databinding.NitificationItemBinding
import com.br.stocks.models.NotificationData
import com.br.stocks.utils.listen
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var notificationItem: ArrayList<NotificationData> = arrayListOf()

    var onNotificationItemClick: ((NotificationData) -> Unit)? = null

    fun setNotificationItem(notificationItem: ArrayList<NotificationData>) {
        this.notificationItem = notificationItem
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: NitificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.ENGLISH)
        var outputFormat = SimpleDateFormat("dd MMM, yyyy hh:mm a",Locale.ENGLISH)
        fun bind(item: NotificationData) {
            inputFormat.timeZone= TimeZone.getTimeZone("${Calendar.getInstance().timeZone}")
            binding.txtDescription.text = item.description
            binding.txtTitle.text = item.title
            binding.time.text = inputFormat.parse(item.created_at)?.let { outputFormat.format(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NitificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).listen { pos, _ ->
            notificationItem[pos].post_id?.let {
                onNotificationItemClick?.invoke(notificationItem[pos])
            }
        }
    }


    override fun getItemCount(): Int {
        return notificationItem.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = notificationItem[position]
        holder.bind(newsItem)


    }
}
