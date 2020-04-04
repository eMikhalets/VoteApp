package com.supercasual.fourtop;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.utils.Network;

import java.util.ArrayList;
import java.util.List;

public class TopImagesActivity extends AppCompatActivity implements ImageAdapter.OnImageListener {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Image> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_images);

        imagesList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_top_images);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // if adapter == null, imageList == null
        if (imageAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            imagesList = Network.get(this).topPhotosRequest(20, 0,
                    () -> {
                        imageAdapter.notifyDataSetChanged();
                        // TODO: remove TextView
                    });
            imageAdapter = new ImageAdapter(this, imagesList, this);
            recyclerView.setAdapter(imageAdapter);
        } else {
            imageAdapter.notifyDataSetChanged();
        }

        if (imagesList.isEmpty()) {
            // TODO: add TextView with text = "загрузите фотографию"
        } else {
            // TODO: remove TextView
        }
    }

    @Override
    public void onImageClick(int position) {
        Image image = imagesList.get(position);
        Toast.makeText(this, "rate = " + image.getRate(), Toast.LENGTH_SHORT).show();
    }
}
