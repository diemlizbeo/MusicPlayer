package com.example.musicplayer.server;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.UpdateIdolActivity;
import com.example.musicplayer.model.MusicFile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ServerActivity extends AppCompatActivity {

    Uri audioUri ;
    StorageReference mStorageref;
    StorageTask mUploadsTask ;
    DatabaseReference referenceSongs ;

    MediaMetadataRetriever metadataRetriever;
    byte [] art ;
    String title, artist, album, duration;
    TextView tvtitle, tvartist,tvalbum, tvdurations;
    ImageView album_art ;

    EditText edID;
    long id;
    String path;
    Button btSelect;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        tvtitle = findViewById(R.id.title);
        tvartist = findViewById(R.id.artist);
        tvdurations = findViewById(R.id.duration);
        tvalbum = findViewById(R.id.album);
        album_art = findViewById(R.id.imageview);
        btSelect = findViewById(R.id.btSelect);
        edID = findViewById(R.id.edID);

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("Songs");
        mStorageref = FirebaseStorage.getInstance().getReference().child("songs");
        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("audio/*");
                startActivityForResult(i,1000);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode  == RESULT_OK && data.getData() != null){

            audioUri = data.getData();
            metadataRetriever.setDataSource(this,audioUri);
            art = metadataRetriever.getEmbeddedPicture();
            if(art != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
                album_art.setImageBitmap(bitmap);
            }
            album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            tvalbum.setText(album);
            tvartist.setText(artist);
            tvdurations.setText(duration);
            tvtitle.setText(title);
        }

    }

    public  void  uploadFileToFirebase (View v ){
        if(!edID.getText().toString().isEmpty() && edID.getText().toString().matches("\\d+")){
            if(mUploadsTask != null && mUploadsTask.isInProgress()){
                Toast.makeText(this, "Song uploads in allready progress!", Toast.LENGTH_SHORT).show();

            }else {
                uploadFiles();
            }
        }else{
            Toast.makeText(this, "Please enter ID as number",Toast.LENGTH_SHORT).show();
        }




    }

    private void uploadFiles() {

        if(audioUri != null){
            final  StorageReference storageReference = mStorageref.child(System.currentTimeMillis()+"."+getFileExtension(audioUri));
            mUploadsTask = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            String ID = referenceSongs.push().getKey();

                            id = Long.parseLong(edID.getText().toString());

                            MusicFile uploadSong = new MusicFile(uri.toString(),title,artist,album,duration,id);
                            referenceSongs.child(String.valueOf(id)).setValue(uploadSong);
                            Toast.makeText(ServerActivity.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });

        }else {
            Toast.makeText(this, "No file selected to uploads", Toast.LENGTH_SHORT).show();
        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
