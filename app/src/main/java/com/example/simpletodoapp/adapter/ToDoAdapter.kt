package com.example.simpletodoapp.adapter

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodoapp.R
import com.example.simpletodoapp.model.ToDo

class ToDoAdapter(context: Context) : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    private var data = ArrayList<ToDo>()
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onDoneStateChangeListener: OnDoneStateChangeListener
    var context = context

    fun setData(list: List<ToDo>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.checkBox.text = item.title
        if (item.done) {
            val sp = SpannableString(item.title)
            sp.setSpan(StrikethroughSpan(),
                0,
                item.title.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.checkBox.text = sp
            holder.checkBox.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
            holder.checkBox.isChecked = true
        } else
            holder.checkBox.setTextColor(ContextCompat.getColor(context, R.color.white))
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            onDoneStateChangeListener.onStateChanged(item, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getToDo(position: Int): ToDo {
        return data[position]
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var checkBox = item.findViewById<CheckBox>(R.id.checkbox)!!

        init {
            item.setOnClickListener {
                onItemClickListener.onItemClick(data[adapterPosition])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(toDo: ToDo)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnDoneStateChangeListener {
        fun onStateChanged(toDo: ToDo, isChecked: Boolean)
    }

    fun setOnStateChangeListener(onDoneStateChangeListener: OnDoneStateChangeListener) {
        this.onDoneStateChangeListener = onDoneStateChangeListener
    }
}
