package com.example.lotteryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NameAdapter(private val nameList: List<String>) :
    RecyclerView.Adapter<NameAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_name, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return if (nameList == null) 0 else Int.MAX_VALUE

    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.setText(nameList.get(position % nameList.size))
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView = view.findViewById(R.id.tv_name)
    }
}