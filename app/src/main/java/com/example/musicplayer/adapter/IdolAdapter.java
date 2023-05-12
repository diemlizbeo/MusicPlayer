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
import com.example.musicplayer.UpdateIdolActivity;
import com.example.musicplayer.model.Idol;
import com.example.musicplayer.model.MusicFile;
import com.example.musicplayer.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class IdolAdapter extends RecyclerView.Adapter<IdolAdapter.IdolViewHolder> {


    private Context context;
    public static ArrayList<Idol> listIdol;
    private FirebaseUser firebaseUser;


    public IdolAdapter(Context context, ArrayList<Idol> listIdol) {
        this.context = context;
        this.listIdol = listIdol;
    }

    @NonNull
    @Override
    public IdolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_idol, parent,false);
        return new IdolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdolViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Idol idol = listIdol.get(position);
        Picasso.get().load(idol.getIdolImg()).placeholder(R.drawable.avt).into(holder.img);
        holder.tvName.setText(idol.getName());
        holder.tvDob.setText(idol.getDob());
        holder.tvCountry.setText(idol.getCountry());
        holder.tvLike.setText(idol.getFavoriteReason());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateIdolActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idol", idol);
                context.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return listIdol.size();
    }


    public class IdolViewHolder extends  RecyclerView.ViewHolder{

        TextView tvName, tvDob, tvCountry, tvLike;
        ImageView img;

        public IdolViewHolder(@NonNull View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvDob = view.findViewById(R.id.tvDob);
            tvCountry = view.findViewById(R.id.tvCountry);
            tvLike = view.findViewById(R.id.tvLike);
            img = view.findViewById(R.id.img);
        }
    }



    public void updateList(ArrayList<Idol> idolArrayList){
        listIdol = new ArrayList<>();
        listIdol.addAll(idolArrayList);
        notifyDataSetChanged();
    }
    private void publisherInfor(final ImageView image_profile , final TextView username , final TextView publisher , String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getImageurl()).placeholder(R.drawable.avt).into(image_profile);
                username.setText(user.getUsername());
                publisher.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
