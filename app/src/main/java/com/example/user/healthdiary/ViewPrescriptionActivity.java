package com.example.user.healthdiary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewPrescriptionActivity extends AppCompatActivity {

    TextView date, name, prescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);

        date = findViewById(R.id.viewPresDate);
        name = findViewById(R.id.viewPresName);
        prescription  = findViewById(R.id.viewPresPres);


        Visit visit = (Visit) getIntent().getExtras().get("data");

        if (visit != null){
            date.setText(visit.date);
            name.setText(visit.name);
            prescription.setText(visit.prescription);
        }
    }
}
