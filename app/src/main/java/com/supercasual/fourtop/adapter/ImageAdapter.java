package com.supercasual.fourtop.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.supercasual.fourtop.R;
import com.supercasual.fourtop.model.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<Image> imageList;
    private OnImageListener mOnImageListener;

    public ImageAdapter(Context context, List<Image> imageList, OnImageListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.mOnImageListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view, mOnImageListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image image = imageList.get(position);
        //Picasso.get().load(image.getImageURL()).into(holder.imageView);

        try {
            Bitmap bitmap = new AsyncRequest().execute(image).get();
            holder.imageView.setImageBitmap(bitmap);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        OnImageListener onImageListener;

        public ViewHolder(@NonNull View itemView, OnImageListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
            this.onImageListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onImageListener.onImageClick(getAdapterPosition());
        }
    }

    public interface OnImageListener {
        void onImageClick(int position);
    }

    private class AsyncRequest extends AsyncTask<Image, Void, Bitmap> {

        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(Image... images) {
            try {
                URL url = new URL(images[0].getImageURL()) ;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
