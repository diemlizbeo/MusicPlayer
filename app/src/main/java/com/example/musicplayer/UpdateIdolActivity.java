package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.model.Idol;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class UpdateIdolActivity extends AppCompatActivity {
    private ImageView img, back;
    private EditText edname, eddob, edcountry, edlike;
    private Button btUpdate, btRemove;
    Uri imgUri;
    String myUrl;
    StorageTask uploadTask;
    StorageReference storageReference;
    private Idol idol;
    private DatabaseReference reference ;
    private static int REQUESCODE = 1 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_idol);
        img = findViewById(R.id.img);
        back = findViewById(R.id.back);
        edname = findViewById(R.id.edname);
        eddob = findViewById(R.id.eddob);
        edcountry = findViewById(R.id.edcountry);
        edlike = findViewById(R.id.edlike);
        btUpdate = findViewById(R.id.btUpdate);
        btRemove = findViewById(R.id.btRemove);
        Intent intent = getIntent();
        idol = (Idol) intent.getSerializableExtra("idol");
        edname.setText(idol.getName());
        eddob.setText(idol.getDob());
        edcountry.setText(idol.getCountry());
        edlike.setText(idol.getFavoriteReason());
        Picasso.get().load(idol.getIdolImg()).placeholder(R.drawable.avt).into(img);

        storageReference = FirebaseStorage.getInstance().getReference("idols");


        eddob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day  = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(UpdateIdolActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if(m > 8){
                            date = d + "/" + (m+1) + "/" + y;

                        }
                        else{
                            date = d + "/0" + (m+1) + "/" + y;

                        }
                        eddob.setText(date);
                    }
                },year, month, day);
                dialog.show();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                runtimePermission();
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,REQUESCODE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Idols").child(idol.getIdolId());

                if(isNameChanged() || isCountryChanged() || isDobChanged() || isFavoriteReasonChanged()) {
                    if(edname.getText().toString().equals("") || eddob.getText().toString().equals("") || edcountry.getText().toString().equals("") || edlike.getText().toString().equals("")) {
                        Toast.makeText(UpdateIdolActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();

                    }else{
                        reference.child("name").setValue(edname.getText().toString());
                        reference.child("dob").setValue(eddob.getText().toString());
                        reference.child("country").setValue(edcountry.getText().toString());
                        reference.child("favoriteReason").setValue(edlike.getText().toString());
                        Toast.makeText(UpdateIdolActivity.this, "Updated !!!", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(UpdateIdolActivity.this, IdolActivity.class);
                        startActivity(intent1);
                    }
                }else{
                    Toast.makeText(UpdateIdolActivity.this, "No change detected", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Idols").child(idol.getIdolId());
                reference.removeValue();
                Intent intent1 = new Intent(UpdateIdolActivity.this, IdolActivity.class);
                startActivity(intent1);
            }
        });
    }

    public boolean isNameChanged(){
        if((edname.getText().toString()).equals(idol.getName()))
            return false;
        return true;
    }
    public boolean isDobChanged(){
        if((eddob.getText().toString()).equals(idol.getDob()))
            return false;
        return true;
    }
    public boolean isCountryChanged(){
        if((edcountry.getText().toString()).equals(idol.getCountry()))
            return false;
        return true;
    }
    public boolean isFavoriteReasonChanged(){
        if((edlike.getText().toString()).equals(idol.getFavoriteReason()))
            return false;
        return true;
    }
    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imgUri != null) {
            final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            uploadTask = filereference.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = (Uri) task.getResult();
                        String myUrl = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference().child("Idols").child(idol.getIdolId());
                        reference.child("idolImg").setValue(myUrl);

                        pd.dismiss();
                    } else {
                        Toast.makeText(UpdateIdolActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateIdolActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UpdateIdolActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            imgUri = data.getData() ;
            img.setImageURI(imgUri);
            uploadImage();

        }
    }
}