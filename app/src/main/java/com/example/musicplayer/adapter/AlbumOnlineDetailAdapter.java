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
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumOnlineDetailAdapter extends RecyclerView.Adapter<AlbumOnlineDetailAdapter.AlbumViewHolder> {

    private Context context;
    public static ArrayList<MusicFile> listAlbumOnline;
    View view;

    public AlbumOnlineDetailAdapter(Context context, ArrayList<MusicFile> listAlbumOnline) {
        this.context = context;
        this.listAlbumOnline = listAlbumOnline;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_song,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.tvName.setText(listAlbumOnline.get(position).getTitle());
        holder.tvSinger.setText(listAlbumOnline.get(position).getArtist());
        byte[] image = getAlbumArt(listAlbumOnline.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap().load(image).into(holder.img);
        }else{
            Glide.with(context).load(R.drawable.welcome_logo).into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("sender", "albumOnlineDetails");
                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listAlbumOnline.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvSinger;
        ImageView img, menuMore;

        public AlbumViewHolder(@NonNull View view) {
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

}
