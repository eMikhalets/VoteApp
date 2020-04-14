package com.supercasual.fourtop.uimain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.databinding.FragmentUserImagesBinding;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.network.Network;
import com.supercasual.fourtop.network.VolleyCallBack;

import java.util.ArrayList;
import java.util.List;

public class UserImagesFragment extends Fragment {

    private FragmentUserImagesBinding binding;

    private static final int RESULT_LOAD_IMAGE = 0;

    private ImageAdapter imageAdapter;
    private List<Image> imagesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_images, container,
                false);

        imagesList = new ArrayList<>();
        binding.recyclerUserImages.setHasFixedSize(true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        // if adapter == null, imageList == null
        if (imageAdapter == null) {
            binding.recyclerUserImages.setLayoutManager(new LinearLayoutManager(getContext()));
            imagesList = Network.get(getContext()).galleryRequest(10, 0,
                    new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            imageAdapter.notifyDataSetChanged();
                        }
                    });
            imageAdapter = new ImageAdapter(getContext(), imagesList);
            binding.recyclerUserImages.setAdapter(imageAdapter);
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }

    // TODO: add image to List<> from content provider in fragment
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == RESULT_LOAD_IMAGE && data != null) {
//                Uri uri = data.getData();
//                FilePath filePath = new FilePath();
//                String imagePath = filePath.getUriRealPath(this, uri);
//
//                // TODO: the added image is not displayed until the screen is rotated
//                Network.get(this).galleryAddRequest(imagePath, new Network.VolleyCallBack() {
//                    @Override
//                    public void onSuccess() {
//                        imagesList = Network.get(UserImagesActivity.this)
//                                .galleryRequest(10, 0,
//                                        () -> {
//                                            imageAdapter.notifyDataSetChanged();
//                                            // TODO: remove TextView
//                                        });
//                    }
//                });
//            }
//        }
//    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user_images, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_user_images_add:
                // TODO: get access to content provider in activity
//                ActivityCompat.requestPermissions(UserImagesActivity.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // TODO: add context menu for images
//    @Override
//    public void onImageClick(int position) {
//        Image image = imagesList.get(position);
//        Toast.makeText(context, "Изображение удалено", Toast.LENGTH_SHORT).show();
//        Network.get(context).galleryRemoveRequest(image,
//                () -> {
//                    imagesList.remove(position);
//                    imageAdapter.notifyDataSetChanged();
//                });
//    }
}
