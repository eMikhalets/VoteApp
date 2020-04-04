package com.supercasual.fourtop;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.utils.FilePath;
import com.supercasual.fourtop.utils.Network;

import java.util.ArrayList;
import java.util.List;

public class UserImagesActivity extends AppCompatActivity implements ImageAdapter.OnImageListener {

    private static final int RESULT_LOAD_IMAGE = 0;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Image> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_images);

        imagesList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_user_images);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // if adapter == null, imageList == null
        if (imageAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            imagesList = Network.get(this).galleryRequest(10, 0,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE && data != null) {
                Uri uri = data.getData();
                FilePath filePath = new FilePath();
                String imagePath = filePath.getUriRealPath(this, uri);

                // TODO: the added image is not displayed until the screen is rotated
                Network.get(this).galleryAddRequest(imagePath, new Network.VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        imagesList = Network.get(UserImagesActivity.this)
                                .galleryRequest(10, 0,
                                        () -> {
                                            imageAdapter.notifyDataSetChanged();
                                            // TODO: remove TextView
                                        });
                    }
                });
            }
        }
    }

    @Override
    public void onImageClick(int position) {
        Image image = imagesList.get(position);
        Toast.makeText(this, "Изображение удалено", Toast.LENGTH_SHORT).show();
        Network.get(this).galleryRemoveRequest(image,
                () -> {
                    imagesList.remove(position);
                    imageAdapter.notifyDataSetChanged();
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_images_upload:
                ActivityCompat.requestPermissions(UserImagesActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
        }
    }
}
