package com.nccdms.bloomiqpro.ui.core.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nccdms.bloomiqpro.data.remote.response.FlowerResponseItem
import com.nccdms.bloomiqpro.databinding.FlowerItemBinding
import java.util.Locale

class AdapterFlower(private val onItemClick: (FlowerResponseItem) -> Unit
) : ListAdapter<FlowerResponseItem, AdapterFlower.FlowerViewHolder>(DiffCallback),Filterable {

    private var originalList: List<FlowerResponseItem> = emptyList()
    private var filteredList: List<FlowerResponseItem> = emptyList()

    init {
        originalList = currentList.toMutableList()
    }

    inner class FlowerViewHolder(private val binding: FlowerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FlowerResponseItem) {
            binding.apply {
                // Bind data to your item view
                logoIv.load(item.flowerImage)
                titleTv.text = item.flowerName
                descTv.text = item.flowerScientific

                // Set click listener
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""
                filteredList = if (query.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.flowerName?.lowercase(Locale.getDefault())?.contains(query) == true
                    }
                }

                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<FlowerResponseItem> ?: emptyList()

                // If the query is empty, display the original list
                if (constraint.isNullOrBlank()) {
                    filteredList = originalList
                }

                submitList(filteredList)
            }
        }
    }

    // ViewHolder and other adapter-related code

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FlowerResponseItem>() {
            override fun areItemsTheSame(oldItem: FlowerResponseItem, newItem: FlowerResponseItem): Boolean {
                return oldItem.flowerId == newItem.flowerId
            }

            override fun areContentsTheSame(oldItem: FlowerResponseItem, newItem: FlowerResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowerViewHolder {
        val binding = FlowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlowerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}