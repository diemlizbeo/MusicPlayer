package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.AlbumDetailActivity;
import com.example.musicplayer.AlbumOnlineDetailActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumOnlineAdapter extends RecyclerView.Adapter<AlbumOnlineAdapter.AlbumViewHolder> {

    private Context context;
    private ArrayList<MusicFile> listAlbum;
    View view;
    public void setList(ArrayList<MusicFile> list){
        this.listAlbum = list;
        notifyDataSetChanged();
    }

    public AlbumOnlineAdapter(Context context, ArrayList<MusicFile> listAlbum) {
        this.context = context;
        this.listAlbum = listAlbum;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.album_item,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.albumName.setText(listAlbum.get(position).getAlbum());
        byte[] image = getAlbumArt(listAlbum.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap().load(image).into(holder.imgAlbum);
        }else{
            Glide.with(context).load(R.drawable.welcome_logo).into(holder.imgAlbum);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumOnlineDetailActivity.class);
                intent.putExtra("albumonlinename", listAlbum.get(position).getAlbum());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAlbum.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAlbum;
        TextView albumName;

        public AlbumViewHolder(@NonNull View view) {
            super(view);
            imgAlbum = view.findViewById(R.id.imgAlbum);
            albumName = view.findViewById(R.id.nameAlbum);


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

}
