package com.example.musicplayer.fragment;


import static com.example.musicplayer.HomeActivity.listMusicOnline;
import static com.example.musicplayer.HomeActivity.listMusicTrend;
import static com.example.musicplayer.HomeActivity.listalbumOnline;
import static com.example.musicplayer.HomeActivity.listartistOnline;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.musicplayer.AlbumOnlineActivity;
import com.example.musicplayer.AllMusicActivity;
import com.example.musicplayer.ArtistOnlineActivity;
import com.example.musicplayer.HomeActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.TrendMusicActivity;
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.AlbumOnlineAdapter;
import com.example.musicplayer.adapter.ArtistAdapter;
import com.example.musicplayer.adapter.ArtistOnlineAdapter;
import com.example.musicplayer.adapter.MusicOnlineAdapter;
import com.example.musicplayer.adapter.MusicOnlineMainAdapter;
import com.example.musicplayer.adapter.MusicTrendAdapter;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class InternetFragment extends Fragment {

    private TextView tvHello, tvGetTrend, tvGetAll, tvgetAlbum, tvgetArtist;
    private ImageView imgHello;
    private MusicOnlineMainAdapter musicAdapter;
    private MusicTrendAdapter musicAdapterTrend;
    private AlbumOnlineAdapter albumAdapter;
    private ArtistOnlineAdapter artistAdapter;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerViewTrend, recyclerViewAll,reViewAlbum, reViewArtist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_internet_main, container, false);
        initView(view);
        recyclerViewAll.setHasFixedSize(true);
        if (!(listMusicOnline.size() < 1)) {
            musicAdapter = new MusicOnlineMainAdapter(getContext(), listMusicOnline);
            recyclerViewAll.setAdapter(musicAdapter);
            recyclerViewAll.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        }
        recyclerViewTrend.setHasFixedSize(true);
        if (!(listMusicTrend.size() < 1)) {
            musicAdapterTrend = new MusicTrendAdapter(getContext(), listMusicTrend);
            recyclerViewTrend.setAdapter(musicAdapterTrend);
            recyclerViewTrend.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        }
        reViewAlbum.setHasFixedSize(true);
        if (!(listalbumOnline.size() < 1)) {
            albumAdapter = new AlbumOnlineAdapter(getContext(), listalbumOnline);
            reViewAlbum.setAdapter(albumAdapter);
            reViewAlbum.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        }
        reViewArtist.setHasFixedSize(true);
        if (!(listartistOnline.size() < 1)) {
            artistAdapter = new ArtistOnlineAdapter(getContext(), listartistOnline);
            reViewArtist.setAdapter(artistAdapter);
            reViewArtist.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        }
        tvGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllMusicActivity.class);
                startActivity(intent);
            }
        });
        tvGetTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TrendMusicActivity.class);
                startActivity(intent);
            }
        });
        tvgetAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlbumOnlineActivity.class);
                startActivity(intent);
            }
        });

        tvgetArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ArtistOnlineActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {

        tvHello = view.findViewById(R.id.tvHello);
        imgHello = view.findViewById(R.id.imgHello);
        tvGetTrend = view.findViewById(R.id.tvGetTrend);
        tvGetAll = view.findViewById(R.id.tvGetAll);
        tvgetAlbum = view.findViewById(R.id.tvGetAlbum);
        tvgetArtist = view.findViewById(R.id.tvGetArtist);
        recyclerViewTrend = view.findViewById(R.id.recycleViewTrend);
        recyclerViewAll = view.findViewById(R.id.recycleViewAll);
        reViewAlbum = view.findViewById(R.id.recycleViewAlbum);
        reViewArtist = view.findViewById(R.id.recycleViewArtist);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvHello.setText("Hey " + user.getUsername() + " !");
                Picasso.get().load(user.getImageurl()).placeholder(R.drawable.avt).into(imgHello);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}