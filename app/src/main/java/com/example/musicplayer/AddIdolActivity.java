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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Calendar;
import java.util.HashMap;

public class AddIdolActivity extends AppCompatActivity {


    private ImageView img, back;
    private EditText edname, eddob, edcountry, edlike;
    private Button btAdd;
    Uri imgUri;
    String myUrl;
    StorageTask uploadTask;
    StorageReference storageReference;
    private static int REQUESCODE = 1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idol);
        img = findViewById(R.id.img);
        back = findViewById(R.id.back);
        edname = findViewById(R.id.edname);
        eddob = findViewById(R.id.eddob);
        edcountry = findViewById(R.id.edcountry);
        edlike = findViewById(R.id.edlike);
        btAdd = findViewById(R.id.btAdd);

        eddob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day  = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddIdolActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        storageReference = FirebaseStorage.getInstance().getReference("idols");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(AddIdolActivity.this);
                progressDialog.setMessage("Posting");
                progressDialog.show();
                if(imgUri != null){
                    final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));
                    uploadTask = filereference.putFile(imgUri);

                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filereference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                if(edname.getText().toString() != null && eddob.getText().toString() != null && edcountry.getText().toString() != null && edlike.getText().toString() != null){
                                    Uri downloadUri = (Uri) task.getResult();
                                    myUrl = downloadUri.toString();
                                    DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("Idols");
                                    String idolID = reference.push().getKey();
                                    HashMap<String , Object> hashMap = new HashMap<>();
                                    hashMap.put("idolId" , idolID);
                                    hashMap.put("name", edname.getText().toString());
                                    hashMap.put("idolImg" , myUrl);
                                    hashMap.put("dob" , eddob.getText().toString());
                                    hashMap.put("country" , edcountry.getText().toString());
                                    hashMap.put("favoriteReason" , edlike.getText().toString());
                                    hashMap.put("publisher" , FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    reference.child(idolID).setValue(hashMap);
                                    progressDialog.dismiss();

                                    startActivity(new Intent(AddIdolActivity.this , IdolActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(AddIdolActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddIdolActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddIdolActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }else{
                    Toast.makeText(AddIdolActivity.this, "No image selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void runtimePermission(){

        Dexter.withContext(this)
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent,REQUESCODE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddIdolActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest(); // if user denied permission.
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            imgUri = data.getData() ;
            img.setImageURI(imgUri);
        }
    }
}