package com.supercasual.fourtop.ui.topimages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.supercasual.fourtop.R;
import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.model.Image;
import com.supercasual.fourtop.network.Network;

import java.util.ArrayList;
import java.util.List;

public class TopImagesFragment extends Fragment {

    private Context context;
    private View view;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Image> imagesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top_images, container, false);
        context = view.getContext();

        imagesList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_top_images);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // if adapter == null, imageList == null
        if (imageAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            imagesList = Network.get(context).topPhotosRequest(20, 0,
                    () -> {
                        imageAdapter.notifyDataSetChanged();
                        // TODO: remove TextView
                    });
            imageAdapter = new ImageAdapter(context, imagesList);
            recyclerView.setAdapter(imageAdapter);
        } else {
            imageAdapter.notifyDataSetChanged();
        }
    }
}
