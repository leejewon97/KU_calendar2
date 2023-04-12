package com.example.ku_calendar2

import android.content.Intent
import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ku_calendar2.databinding.DateBinding

class MyDataAdapter(val datas:ArrayList<MyData>): RecyclerView.Adapter<MyDataAdapter.ViewHolder>() {
    interface OnClickListener{
        fun onItemClick(data: MyData, adapterPosition: Int)
        fun onButtonClick(data: MyData, adapterPosition: Int)
    }

    var itemClickListener:OnClickListener?=null
    var buttonClickListener:OnClickListener?=null

    inner class ViewHolder(val binding: DateBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.textView1.setOnClickListener {
                itemClickListener?.onItemClick(datas[adapterPosition], adapterPosition)
            }
            binding.button.setOnClickListener {
                buttonClickListener?.onButtonClick(datas[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView1.text = datas[position].date
        holder.binding.textView2.text = datas[position].schedule
        holder.binding.textView2.visibility = when (datas[position].isOpen) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        holder.binding.button.visibility = when (datas[position].isOpen) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        if (datas[position].schedule != "")
            holder.binding.textView1.setTextColor(Color.GREEN)
        else
            holder.binding.textView1.setTextColor(Color.GRAY)

        if (datas[position].month == "8" || datas[position].month == "3")
            holder.binding.textView1.setTextColor(Color.LTGRAY)
    }
}