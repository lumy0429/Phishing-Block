package com.example.phishingblock;

import static com.example.phishingblock.MainActivity.uid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        uid = getIntent().getStringExtra("uid");

        ImageButton backbutton=(ImageButton)findViewById(R.id.imageView13);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });





    }
}