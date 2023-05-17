package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edusername, edfullname, edemail, edpassword, edpasswordCf;
    private Button btSignUp;
    private TextView tvLogin;
    private ImageView imgAvt;
    Uri pickedImgUri ;
    private FirebaseAuth auth;
    private DatabaseReference reference ;
    private ProgressDialog pd;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    private static int REQUESCODE = 1 ;

    private String Stringimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        edfullname = findViewById(R.id.edfullname);
        edusername = findViewById(R.id.edusername);
        edemail = findViewById(R.id.email_field);
        edpassword = findViewById(R.id.editPassword);
        edpasswordCf = findViewById(R.id.editPasswordCf);
        btSignUp = findViewById(R.id.btSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        imgAvt = findViewById(R.id.imgAvt);
        storageRef = FirebaseStorage.getInstance().getReference("uploads");


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
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();
                String username = edusername.getText().toString();
                String fullname = edfullname.getText().toString();
                String email = edemail.getText().toString();
                String pw = edpassword.getText().toString();
                String pw_cf = edpasswordCf.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(fullname) ||TextUtils.isEmpty(email) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(pw_cf)){
                    Toast.makeText(RegisterActivity.this, "Empty credential$!", Toast.LENGTH_SHORT).show();
                } else if (pw.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else if (!pw.equals(pw_cf)) {
                    Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                } else{
                    auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userid = firebaseUser.getUid();
                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("username", username.toLowerCase());
                                hashMap.put("fullname", fullname);
                                hashMap.put("bio", "");
                                hashMap.put("imageurl", "");



                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            pd.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                if (pickedImgUri != null) {
                                    final StorageReference filereference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(pickedImgUri));

                                    uploadTask = filereference.putFile(pickedImgUri);
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

                                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("imageurl", "" + myUrl);

                                                reference.updateChildren(hashMap);
                                                pd.dismiss();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "Register fail", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
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
    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();


    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            pickedImgUri = data.getData() ;
            imgAvt.setImageURI(pickedImgUri);
        }
    }
}