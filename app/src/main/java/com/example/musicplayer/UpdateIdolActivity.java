package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicplayer.model.Idol;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

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

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Idols").child(idol.getId());

                if(isNameChanged() || isCountryChanged() || isDobChanged() || isFavoriteReasonChanged()) {
                    reference.child("name").setValue(edname.getText().toString());
                    reference.child("dob").setValue(eddob.getText().toString());
                    reference.child("country").setValue(edcountry.getText().toString());
                    reference.child("favoriteReason").setValue(edlike.getText().toString());
                    Toast.makeText(UpdateIdolActivity.this, "Updated !!!", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(UpdateIdolActivity.this, IdolActivity.class);
                    startActivity(intent1);

                }else{
                    Toast.makeText(UpdateIdolActivity.this, "No change detected", Toast.LENGTH_SHORT).show();
                }
                
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
}