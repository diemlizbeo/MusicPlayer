package com.example.musicplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.R;
import com.example.musicplayer.model.HomeView;
import com.example.musicplayer.model.SlideshowView;

public class SlideshowFragment extends Fragment {
    private SlideshowView slideshowView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        slideshowView = new ViewModelProvider(this).get(SlideshowView.class);
        View view=inflater.inflate(R.layout.fragment_slideshow, container,false);
        final TextView tv = view.findViewById(R.id.txt);
        slideshowView.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });
        return view;
    }
}
