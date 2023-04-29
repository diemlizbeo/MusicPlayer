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

public class ArtistDetailAdapter extends RecyclerView.Adapter<ArtistDetailAdapter.ArtistViewHolder> {

    private Context context;
    public static ArrayList<MusicFile> listArtist;
    View view;

    public ArtistDetailAdapter(Context context, ArrayList<MusicFile> listArtist) {
        this.context = context;
        this.listArtist = listArtist;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_song,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.tvName.setText(listArtist.get(position).getTitle());
        holder.tvSinger.setText(listArtist.get(position).getArtist());
        byte[] image = getAlbumArt(listArtist.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap().load(image).into(holder.img);
        }else{
            Glide.with(context).load(R.drawable.welcome_logo).into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("sender", "artistDetails");
                intent.putExtra("position", position);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return listArtist.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvSinger;
        ImageView img, menuMore;

        public ArtistViewHolder(@NonNull View view) {
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
