package com.emikhalets.voteapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emikhalets.voteapp.databinding.ItemImageBinding
import com.emikhalets.voteapp.network.pojo.DataImage
import com.squareup.picasso.Picasso
import java.util.*

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var images: List<DataImage>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setImages(images: List<DataImage>) {
        this.images = images
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: DataImage) {
            val textRating = "Рейтинг: " + image.rate
            binding.textRating.text = textRating
            Picasso.get().load(image.link).into(binding.imageItem)
        }
    }

    init {
        images = ArrayList()
    }
}