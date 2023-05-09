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

import com.example.musicplayer.model.Idol;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Calendar;

public class UpdateIdolActivity extends AppCompatActivity {
    private ImageView img, back;
    private EditText edname, eddob, edcountry, edlike;
    private Button btAdd;
    Uri imgUri;
    String myUrl;
    StorageTask uploadTask;
    StorageReference storageReference;
    private Idol idol;



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
        btAdd = findViewById(R.id.btRemove);
        Intent intent = getIntent();
        idol = (Idol) intent.getSerializableExtra("idol");
//        img.setImageResource(Integer.parseInt(idol.getIdolImg()));
        edname.setText(idol.getName());
        eddob.setText(idol.getDob());
        edcountry.setText(idol.getCountry());
        edlike.setText(idol.getFavoriteReason());

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

    }
}