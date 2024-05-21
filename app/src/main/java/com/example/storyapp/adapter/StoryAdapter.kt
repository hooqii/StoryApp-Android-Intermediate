package com.example.storyapp.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.StoryRowItemBinding
import com.example.storyapp.util.DateFormat
import java.util.TimeZone

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(diffCallback) {

    var onClick: ((ListStoryItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            StoryRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user, onClick)
        } else {
            Log.e("StoryAdapter", "User at position $position is null")
        }
    }

    class ListViewHolder(private var binding: StoryRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(story: ListStoryItem, onClick: ((ListStoryItem) -> Unit)?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .error(R.drawable.wawan)
                    .into(ivPhoto)
                story.createdAt?.let {
                    val datePosted =
                        DateFormat.formatDate(it, TimeZone.getDefault().id)
                    tvDatePosted.text = itemView.context.getString(R.string.created_at, datePosted)
                    tvDatePosted.visibility = View.VISIBLE
                } ?: run {
                    tvDatePosted.visibility = View.GONE
                }
                tvItemName.text = story.name
                tvItemDescription.text = story.description

                itemView.setOnClickListener {
                    onClick?.invoke(story)
                }
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
