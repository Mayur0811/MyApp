package com.br.stocks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.databinding.EventItemBinding
import com.br.stocks.models.EventResponse
import com.br.stocks.utils.listen

class EventAdapter(var eventResponseDataList: ArrayList<EventResponse>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

  //  private var eventResponse: ArrayList<EventResponse> = arrayListOf()
    var onEventItemClick: ((EventResponse) -> Unit)? = null

    class ViewHolder(var binding: EventItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: EventResponse) {
            binding.event = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).listen { position, _ ->
            onEventItemClick?.invoke(eventResponseDataList[position])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventResponseDataList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return eventResponseDataList.size
    }

//    fun updateList(eventResponse: ArrayList<EventResponse>) {
//        this.eventResponse = eventResponse
//        notifyDataSetChanged()
//
//    }
}