package com.example.simpletodoapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodoapp.R
import com.example.simpletodoapp.model.Group
import com.example.simpletodoapp.util.RandomColor
import com.example.simpletodoapp.viewmodel.CategoryViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CategoryGroupAdapter(context: Context) :
    RecyclerView.Adapter<CategoryGroupAdapter.DataAdapterViewHolder>() {

    var context: Context = context
    var data = ArrayList<Group>()
    val color = RandomColor.generateRandomColor()
    var categoryViewModel: CategoryViewModel =
        ViewModelProvider(context as ViewModelStoreOwner).get(CategoryViewModel::class.java)
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onItemClickListenerNew: OnItemClickListenerNew


    fun setData(list: List<Group>) {
        data.clear()
        data.add(Group.NewCategory("New category", R.drawable.back))
        data.add(Group.Category(title = "All", color = color))
        data.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            TYPE_CATEGORY -> R.layout.item_cat
            TYPE_NEW_CATEGORY -> R.layout.item_new_cat
            else -> throw IllegalArgumentException("Invalid type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return DataAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Group.Category -> TYPE_CATEGORY
            is Group.NewCategory -> TYPE_NEW_CATEGORY
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_NEW_CATEGORY = 1
    }

    inner class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                if (adapterPosition == 0) {
                    onItemClickListenerNew.onItemClick(data[adapterPosition] as Group.NewCategory)
                } else
                    onItemClickListener.onItemClick(data[adapterPosition] as Group.Category)
            }
        }

        private fun bindCategory(item: Group.Category) {
            itemView.findViewById<TextView>(R.id.titleCat)?.text = item.title
            itemView.findViewById<CardView>(R.id.root)?.setCardBackgroundColor(item.color)
            var editCat = itemView.findViewById<ImageView>(R.id.editCat)
            var deleteCat = itemView.findViewById<ImageView>(R.id.deleteCat)
            if (item.id == 0) {
                editCat.visibility = View.GONE
                deleteCat.visibility = View.GONE
            }
            deleteCat.setOnClickListener {
                deleteCategory(item)
            }
            editCat.setOnClickListener {
                editCategory(context, item)
            }
        }

        private fun bindNewCategory(item: Group.NewCategory) {
            itemView.findViewById<TextView>(R.id.titleNew)?.text = item.title
            itemView.findViewById<RelativeLayout>(R.id.rootLayout)
                ?.setBackgroundResource(item.image)
        }

        fun bind(dataModel: Group) {
            when (dataModel) {
                is Group.Category -> bindCategory(dataModel)
                is Group.NewCategory -> bindNewCategory(dataModel)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(category: Group.Category)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListenerNew {
        fun onItemClick(newCategory: Group.NewCategory)
    }

    fun setOnItemClickListenerNew(onItemClickListenerNew: OnItemClickListenerNew) {
        this.onItemClickListenerNew = onItemClickListenerNew
    }

    fun deleteCategory(category: Group.Category) {
        MainScope().launch {
            categoryViewModel.delete(category)
        }
    }

    fun editCategory(context: Context, category: Group.Category) {
        var newTitle = ""
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Edit Category Title")

        val input = EditText(context)
        input.hint = "Enter new title"
        input.inputType = InputType.TYPE_CLASS_TEXT

        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            newTitle = input.text.toString()
            MainScope().launch {
                if (newTitle.isNotEmpty()) {
                    var newCategory =
                        Group.Category(id = category.id, title = newTitle, color = category.color)
                    categoryViewModel.update(newCategory)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
}