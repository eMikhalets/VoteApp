package com.supercasual.fourtop.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.supercasual.fourtop.model.Image;

import java.util.List;

// TODO: delete this shit, create ViewModel
public class RetainVotingFragment extends Fragment {

    private List<Image> imageList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<Image> getData() {
        return imageList;
    }

    public void setData(List<Image> imageList) {
        this.imageList = imageList;
    }
}
