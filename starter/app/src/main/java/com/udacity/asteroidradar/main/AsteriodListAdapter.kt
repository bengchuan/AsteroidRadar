package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.main.AsteroidListAdapter.AsteroidListViewHolder
import com.udacity.asteroidradar.databinding.ItemAsteriodBinding

class AsteroidListAdapter(val onClickListener: AsteriodOnClickListener) :
    ListAdapter<Asteroid, AsteroidListViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class AsteroidListViewHolder(private val binding: ItemAsteriodBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: AsteriodOnClickListener, asteroid: Asteroid) {
            binding.asteriod = asteroid
            binding.asteriodOnClickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAsteriodBinding.inflate(layoutInflater, parent, false)
                return AsteroidListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListViewHolder {
        return AsteroidListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(onClickListener, item)
    }
}

class AsteriodOnClickListener(val asteriodOnClickLamda: (asteriod: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = asteriodOnClickLamda(asteroid)
}