package com.example.phishingblock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MypageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Button changeButton=findViewById(R.id.button1);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSend();
                Intent intent=new Intent(getApplicationContext(),SettingActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

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
    private void dataSend(){ //입력된 정보 firebase에 저장
        EditText nameEdit=(EditText)findViewById(R.id.name);
        String value=nameEdit.getText () .toString();

        EditText numEdit=(EditText)findViewById(R.id.number);
        String v=numEdit.getText () .toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance () ;
        DatabaseReference myRef = database. getReference ();
        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        myRef. child(  "user") .child (uid).child("전화번호").setValue (v);
        myRef. child(  "user") .child (uid).child("이름").setValue (value);

        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);

    }
}
