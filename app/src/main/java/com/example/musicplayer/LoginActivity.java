package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.database.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edemail, edpassword;
    private Button btLogin;
    private TextView tvSignUp, tvResetpw;
    ViewGroup progressView;
    protected boolean isProgressShowing = false;
    private final static int REQUEST_CODE_REGISTER = 10000;
    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = auth.getCurrentUser();
        if (currUser != null) {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        edemail = findViewById(R.id.email_field);
        edpassword = findViewById(R.id.editPassword);
        btLogin = findViewById(R.id.btLogin);
        tvResetpw = findViewById(R.id.tvResetpw);
        auth = FirebaseAuth.getInstance();
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(registerIntent, REQUEST_CODE_REGISTER);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please wait...");
                pd.show();
                String email, password;
                email = String.valueOf(edemail.getText());
                password = String.valueOf((edpassword.getText()));

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                auth.signInWithEmailAndPassword(edemail.getText().toString(), edpassword.getText().toString()).
                     addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                                 reference.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         pd.dismiss();
                                         Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         startActivity(intent);
                                         finish();
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {
                                         pd.dismiss();
                                     }
                                 });
                             }else{
                                 pd.dismiss();
                                 Toast.makeText(LoginActivity.this, "Authentication Failed !", Toast.LENGTH_SHORT).show();
                             }
                         }
                     })  ;
            }
        });

        tvResetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(edemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "An email has been sent to you.", Toast.LENGTH_LONG).show();
                        } else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });

    }


    public void showProgressingView() {

        if (!isProgressShowing) {
            View view = findViewById(R.id.progressBar1);
            view.bringToFront();
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_REGISTER) {
            if(resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                final String email = data.getStringExtra("email");
                final String password = data.getStringExtra("pass");
                //Set lại giá trị cho txtEmail and password
                edemail.setText(email);
                edpassword.setText(password);
            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }

    }
}