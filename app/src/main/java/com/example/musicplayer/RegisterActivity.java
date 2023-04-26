package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.database.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edname, edemail, edpassword, edpasswordCf;
    private Button btSignUp;
    private TextView tvLogin;
    private ImageView imgAvt;
    Uri pickedImgUri ;
    protected FirebaseAuth mFirebaseAuth;

    private static int REQUESCODE = 1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        edname = findViewById(R.id.edusername);
        edemail = findViewById(R.id.email_field);
        edpassword = findViewById(R.id.editPassword);
        edpasswordCf = findViewById(R.id.editPasswordCf);
        btSignUp = findViewById(R.id.btSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        imgAvt = findViewById(R.id.imgAvt);


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edname.getText().toString().trim();
                String email = edemail.getText().toString().trim();
                String pw = edpassword.getText().toString().trim();
                String pw_cf = edpasswordCf.getText().toString().trim();

                if(pw.equals(pw_cf)){
                    mFirebaseAuth.createUserWithEmailAndPassword(edemail.getText().toString(),edpassword.getText().toString()).
                            addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(RegisterActivity.this,"Register Success", Toast.LENGTH_SHORT).show();
                                    updateUserInfor(name, pickedImgUri,mFirebaseAuth.getCurrentUser());
                                    final Intent data = new Intent(RegisterActivity.this, LoginActivity.class);
                                    // Truyền data vào intent
                                    data.putExtra("email", edemail.getText().toString());
                                    data.putExtra("pass", edpassword.getText().toString());
                                    // Đặt resultCode là Activity.RESULT_OK
                                    setResult(Activity.RESULT_OK, data);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }else{
                    Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               runtimePermission();
            }
        });
    }

    private void updateUserInfor(String name, Uri pickedImgUri, FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded succesfully
                // now we can get our image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contain user image url
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
//                                            Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                                            startActivity(intent);
//                                            finish();
                                        }

                                    }
                                });
                    }
                });


            }
        });
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
                        Toast.makeText(RegisterActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();
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

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            imgAvt.setImageURI(pickedImgUri);
        }
    }
}