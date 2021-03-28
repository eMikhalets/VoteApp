package com.emikhalets.voteapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.R
import com.emikhalets.voteapp.databinding.ItemImageBinding
import com.emikhalets.voteapp.model.entities.Image
import com.emikhalets.voteapp.test.loadMock
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

    class ImagesViewHolder(private val binding: ItemImageBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Image) {
            binding.apply {
                image.loadMock(item.url.toInt())
                textRating.text = root.context.getString(
                        R.string.item_images_text_rating,
                        item.rating
                )
                textOwner.text = root.context.getString(
                        R.string.item_images_text_owner,
                        item.ownerName
                )
            }
        }
    }
}