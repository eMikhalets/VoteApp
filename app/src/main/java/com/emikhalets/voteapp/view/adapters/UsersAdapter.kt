package com.emikhalets.voteapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ItemUserBinding
import com.emikhalets.voteapp.model.entities.User
import com.emikhalets.voteapp.test.loadMock
import com.emikhalets.voteapp.utils.toast

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val images = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener { toast("User Click") }
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int = images.size

    fun updateList(list: List<User>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }

    fun addItem(item: User) {
        images.add(item)
        notifyItemInserted(images.size)
    }

    inner class UsersViewHolder(private val binding: ItemUserBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User, position: Int) {
            binding.apply {
                image.loadMock(item.photo.toInt())
                textPosition.text = (position + 1).toString()
                textUsername.text = item.username
                textRating.text = root.context.getString(
                        R.string.item_images_text_rating,
                        item.rating
                )
            }
        }
    }
}