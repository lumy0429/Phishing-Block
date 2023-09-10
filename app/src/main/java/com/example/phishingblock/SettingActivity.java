package com.example.phishingblock;

import static com.example.phishingblock.MainActivity.mDatabase;
import static com.example.phishingblock.MainActivity.name_1;
import static com.example.phishingblock.MainActivity.uid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    String number;
    TextView numtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        ImageButton backbutton=findViewById(R.id.imageView13);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        Button mypagebutton=findViewById(R.id.button8);
        mypagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MypageActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        //로그아웃
        mAuth = FirebaseAuth.getInstance();
        Button logoutbutton = findViewById(R.id.button222);
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        //개인정보 띄워줌
        mDatabase.child("user").child(uid).child("전화번호").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                number = value;
                numtext.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println(number);
        TextView ntext = (TextView) findViewById(R.id.textView33);
        ntext.setText(name_1);

        numtext = (TextView) findViewById(R.id.textView36);
        numtext.setText(number);


}
}

