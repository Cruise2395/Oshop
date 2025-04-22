package com.example.oshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.oshop.data.Product
import com.example.oshop.databinding.ItemProductBinding
import com.example.oshop.utils.addStrikethrough

class ProductAdapter(
    var items: List<Product>,
    val onClick: (Int)-> Unit,
    val onDelete: (Int) -> Unit,
    val onCheck: (Int) -> Unit
) : Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int { //Int= items.size resumen para funciones q solo tienen una sentencia
        return items.size
    }
    //override fun getItemCount(): Int = items.size ambas funciones son la misma

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDelete(position)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { _, _ ->
            if (holder.binding.doneCheckBox.isPressed) {
                onCheck(position)
            }
        }
    }

    fun updateItems(items: List<Product>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class ProductViewHolder(val binding: ItemProductBinding) : ViewHolder(binding.root){

    fun render(task: Product) {
        if(task.done){
            binding.titleTextView.text = task.title.addStrikethrough()
        } else{
            binding.titleTextView.text = task.title
        }
        binding.doneCheckBox.isChecked = task.done
    }
}