package com.supercasual.fourtop.uimain;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.ItemImageBinding;
import com.supercasual.fourtop.network.pojo.DataImage;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<DataImage> images;

    public ImageAdapter() {
        images = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemImageBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<DataImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemImageBinding binding;

        public ViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DataImage image) {
            binding.pbLoading.setVisibility(View.VISIBLE);
            binding.textRate.setVisibility(View.INVISIBLE);

            String textRating = "Рейтинг: " + image.getRate();
            binding.textRate.setText(textRating);

            Glide.with(binding.getRoot().getContext())
                    .load(image.getLink())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target,
                                                    boolean isFirstResource) {
                            binding.pbLoading.setVisibility(View.INVISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target,
                                                       DataSource dataSource,
                                                       boolean isFirstResource) {
                            binding.pbLoading.setVisibility(View.INVISIBLE);
                            binding.textRate.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .error(R.drawable.image_placeholder)
                    .into(binding.imageItem);
        }
    }
}
