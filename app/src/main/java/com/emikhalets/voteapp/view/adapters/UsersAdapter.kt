package com.emikhalets.voteapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ItemUserBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.utils.loadImage

class UsersAdapter(
        private val click: (User) -> Unit = {},
) : ListAdapter<User, UsersAdapter.UsersViewHolder>(UsersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onViewAttachedToWindow(holder: UsersViewHolder) {
        super.onViewAttachedToWindow(holder)
        val item = getItem(holder.adapterPosition)
        holder.itemView.setOnClickListener { click.invoke(item) }
    }

    override fun onViewDetachedFromWindow(holder: UsersViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class UsersViewHolder(private val binding: ItemUserBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User, position: Int) {
            binding.apply {
                image.loadImage(item.photo)
                textPosition.text = (position + 1).toString()
                textUsername.text = item.username
                textRating.text = root.context.getString(
                        R.string.item_users_text_rating,
                        item.rating
                )
            }
        }
    }

    class UsersDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}