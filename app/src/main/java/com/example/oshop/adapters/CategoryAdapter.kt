package com.example.oshop.adapters

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oshop.data.Category
import com.example.oshop.databinding.ItemCategoryBinding
import com.example.oshop.databinding.ItemProductBinding
import com.example.oshop.utils.addStrikethrough

class CategoryAdapter(
    var items: List<Category>,
    val onClick: (Int) ->Unit,
    val onEdit: (Int) -> Unit,
    val onDelete: (Int) -> Unit
) : Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = items[position]
        holder.render(category)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            onEdit(position)
            true
        }
        holder.binding.deleteButton.setOnClickListener {
            onDelete(position)
        }
    }

    fun updateItems(newItems: List<Category>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
class CategoryViewHolder(val binding: ItemCategoryBinding): ViewHolder(binding.root){

    fun render(category: Category){
        binding.titleTextView.text = category.title
    }
}
