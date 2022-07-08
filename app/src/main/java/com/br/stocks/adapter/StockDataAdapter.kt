package com.br.stocks.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.stocks.databinding.StockItemBinding
import com.br.stocks.models.DataItem
import com.br.stocks.utils.listen
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class StockDataAdapter : RecyclerView.Adapter<StockDataAdapter.ViewHolder>() {

    private var dataList: List<DataItem> = arrayListOf()
    var onItemClick: ((DataItem) -> Unit)? = null

    var inputFormat= SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    var outputFormat= SimpleDateFormat("dd MMM, yyyy",Locale.ENGLISH)

    class ViewHolder(var binding: StockItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding).listen { position, _ ->
            try {
                onItemClick?.invoke(dataList[position])
            }catch (e:Exception){
                Log.d("TAG", "onCreateViewHolder: ${e.message}")
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (dataList[position].image != null && !dataList[position].image.isNullOrEmpty()) {
            Glide.with(holder.itemView.context).load( dataList[position].image).into(holder.binding.imgLogo)
            holder.binding.txtName.text = dataList[position].stockName

            val date= dataList[position].date?.let { inputFormat.parse(it) }
            holder.binding.txtDate.text= date?.let { outputFormat.format(it) }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun upDateList(dataList: List<DataItem>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}
