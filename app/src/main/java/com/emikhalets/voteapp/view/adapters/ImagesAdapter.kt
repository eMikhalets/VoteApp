package com.emikhalets.voteapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ItemImageBinding
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.utils.loadImage
import com.emikhalets.voteapp.utils.toast

class ImagesAdapter(
        private val showOwner: Boolean = false
) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    private val images = mutableListOf<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener { toast("Image Click") }
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    fun updateList(list: List<Image>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }

    fun addItem(item: Image) {
        images.add(item)
        notifyItemInserted(images.size)
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
                            item.ownerName
                    )
                    textOwner.visibility = View.VISIBLE
                } else textOwner.visibility = View.GONE
            }
        }
    }
}