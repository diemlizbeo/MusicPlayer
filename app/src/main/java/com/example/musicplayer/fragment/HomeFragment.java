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

public class HomeFragment extends Fragment {
    private HomeView homeView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeView = new ViewModelProvider(this).get(HomeView.class);
        View view=inflater.inflate(R.layout.fragment_home, container,false);
        final TextView tv = view.findViewById(R.id.txt);
        homeView.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });
        return view;
    }
}
