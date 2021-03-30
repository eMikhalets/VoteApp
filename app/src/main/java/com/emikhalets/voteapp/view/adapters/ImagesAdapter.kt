package com.emikhalets.voteapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ItemImageBinding
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.utils.loadImage

class ImagesAdapter(
        private val showOwner: Boolean = false,
        private val click: (String) -> Unit,
) : ListAdapter<Image, ImagesAdapter.ImagesViewHolder>(ImagesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImagesViewHolder(binding)
    }

    override fun onViewAttachedToWindow(holder: ImagesViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener { click.invoke(getItem(holder.adapterPosition).url) }
    }

    override fun onViewDetachedFromWindow(holder: ImagesViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImagesViewHolder(private val binding: ItemImageBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Image) {
            binding.apply {
                image.loadImage(item.url)
                textRating.text = root.context.getString(
                        R.string.item_images_text_rating,
                        item.rating
                )
                if (showOwner) {
                    textOwner.text = root.context.getString(
                            R.string.item_images_text_owner,
                            item.owner_name
                    )
                    textOwner.visibility = View.VISIBLE
                } else textOwner.visibility = View.GONE
            }
        }
    }

    class ImagesDiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }
}