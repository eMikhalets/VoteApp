package com.supercasual.fourtop.uimain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.R;
import com.supercasual.fourtop.databinding.ItemImageBinding;
import com.supercasual.fourtop.network.pojo.DataImages;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private ItemImageBinding binding;
    private List<DataImages> images;

    public ImageAdapter(Context context, List<DataImages> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_image, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        showProgressBar();
        String textRating = context.getString(
                R.string.image_adapter_rating, images.get(position).getRate());

        Picasso.get().load(images.get(position).getLink())
                .into(binding.imageItem, new Callback() {
                    @Override
                    public void onSuccess() {
                        hideProgressBar();
                        binding.textItemImageRate.setText(textRating);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<DataImages> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    private void showProgressBar() {
        binding.pbLoadImage.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.pbLoadImage.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemImageBinding binding;

        public ViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
