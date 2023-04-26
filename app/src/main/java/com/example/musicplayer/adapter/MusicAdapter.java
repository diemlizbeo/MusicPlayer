package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicFile;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {


    private Context context;
    public static ArrayList<MusicFile> listFile;

    public MusicAdapter(Context context, ArrayList<MusicFile> listFile) {
        this.context = context;
        this.listFile = listFile;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvName.setText(listFile.get(position).getTitle());
        holder.tvSinger.setText(listFile.get(position).getArtist());
        byte[] image = getAlbumArt(listFile.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap().load(image).into(holder.img);
        }else{
            Glide.with(context).load(R.drawable.welcome_logo).into(holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete:
                                Toast.makeText(context, "Delete clicked", Toast.LENGTH_SHORT).show();

                                // detele file here


                                break;
                        }
                        return true;
                    }
                });
            }
        });



    }


    @Override
    public int getItemCount() {
        return listFile.size();
    }


    public class MusicViewHolder extends  RecyclerView.ViewHolder{

        TextView tvName, tvSinger;
        ImageView img, menuMore;

        public MusicViewHolder(@NonNull View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvSinger = view.findViewById(R.id.tvSinger);
            img = view.findViewById(R.id.img);
            menuMore = view.findViewById(R.id.menuMore);

        }
    }

    private byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return art;
    }

    public void updateList(ArrayList<MusicFile> musicFileArrayList){
        listFile = new ArrayList<>();
        listFile.addAll(musicFileArrayList);
        notifyDataSetChanged();
    }
}
