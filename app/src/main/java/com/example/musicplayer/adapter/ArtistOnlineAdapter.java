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
import com.example.musicplayer.ArtistDetailActivity;
import com.example.musicplayer.ArtistOnlineDetailActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class ArtistOnlineAdapter extends RecyclerView.Adapter<ArtistOnlineAdapter.ArtistViewHolder> {

    private Context context;
    private ArrayList<MusicFile> listArtist;
    View view;

    public ArtistOnlineAdapter(Context context, ArrayList<MusicFile> listArtist) {
        this.context = context;
        this.listArtist = listArtist;
    }



    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.artist_online_item,parent,false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.artistName.setText(listArtist.get(position).getArtist());
        byte[] image = getAlbumArt(listArtist.get(position).getPath());
        if(image != null){
            Glide.with(context).asBitmap().load(image).into(holder.imgArtist);
        }else{
            Glide.with(context).load(R.drawable.dia).into(holder.imgArtist);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArtistOnlineDetailActivity.class);
                intent.putExtra("artistonlinename", listArtist.get(position).getArtist());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listArtist.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder{
        ImageView imgArtist;
        TextView artistName;

        public ArtistViewHolder(@NonNull View view) {
            super(view);
            imgArtist = view.findViewById(R.id.imgArtist);
            artistName = view.findViewById(R.id.nameArtist);


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
