package com.example.musicplayer.fragment;

import static com.example.musicplayer.HomeActivity.listMusicOnline;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.AllMusicActivity;
import com.example.musicplayer.HomeActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.example.musicplayer.adapter.MusicOnlineMainAdapter;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class InternetFragment extends Fragment {
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
    private TextView tvHello, tvGetTrend, tvGetAll;
    private ImageView imgHello;
    private MusicOnlineMainAdapter musicAdapter;

    private RecyclerView recyclerViewTrend, recyclerViewAll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internet_main, container, false);
        initView(view);
        recyclerViewAll.setHasFixedSize(true);
        if (!(listMusicOnline.size() < 1)) {
            Toast.makeText(getContext(), String.valueOf(listMusicOnline.size()), Toast.LENGTH_SHORT).show();
            musicAdapter = new MusicOnlineMainAdapter(getContext(), listMusicOnline);
            recyclerViewAll.setAdapter(musicAdapter);
            recyclerViewAll.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        }
        tvGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllMusicActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
//        viewPager = view.findViewById(R.id.viewPager);
//        tabLayout = view.findViewById(R.id.tabLayout);
////        searchView = findViewById(R.id.searchView);
////        bottomNavigationView = findViewById(R.id.bottomNavigation);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
//        viewPagerAdapter.addFragments(new OnlineFragment(), "Online");
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);

        tvHello = view.findViewById(R.id.tvHello);
        tvGetTrend = view.findViewById(R.id.tvGetTrend);
        tvGetAll = view.findViewById(R.id.tvGetAll);
        recyclerViewTrend = view.findViewById(R.id.recycleViewTrend);
        recyclerViewAll = view.findViewById(R.id.recycleViewAll);
    }
}