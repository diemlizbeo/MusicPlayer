package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class MusicOnlineMainAdapter extends RecyclerView.Adapter<MusicOnlineMainAdapter.MusicViewHolder> {


    private Context context;
    public static ArrayList<MusicFile> listFile;

    public MusicOnlineMainAdapter(Context context, ArrayList<MusicFile> listFile) {
        this.context = context;
        this.listFile = listFile;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.songonline_item, parent,false);
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
                intent.putExtra("sender", "musicOnlineMain");
                intent.putExtra("position", position);
                context.startActivity(intent);
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
//            menuMore = view.findViewById(R.id.menuMore);

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
