package com.example.phishingblock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewgroupActivity extends AppCompatActivity {
    EditText editText_name;
    EditText editText_phone;
    EditText editText_relation;
    Button button;
    String uid;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgroup);
        uid = getIntent().getStringExtra("uid");
        firebaseAuth = FirebaseAuth.getInstance();

        init();
        SettingListener();
    }





    private void datasend() { //전달받은 내용 firebase에 추가
        EditText phoneEdit=(EditText)findViewById(R.id.phone);
        String val=phoneEdit.getText().toString();

        EditText idEdit=(EditText)findViewById(R.id.name);
        String value=idEdit.getText().toString();

        EditText relationEdit=(EditText)findViewById(R.id.relation);
        String v=relationEdit.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance () ;
        DatabaseReference myRef = database. getReference ();
        uid = firebaseAuth.getCurrentUser().getUid();
        myRef.child("user").child(uid).child("friend").child(value).setValue(val);

    }

    private void init() {
        editText_name = findViewById(R.id.name);
        editText_phone = findViewById(R.id.phone);
        editText_relation = findViewById(R.id.relation);
        button = findViewById(R.id.button);


    }

    private void SettingListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datasend();

                String n,p,r;
                n = editText_name.getText().toString();
                p = editText_phone.getText().toString();
                r = editText_relation.getText().toString();

                ArrayList<String> profile = new ArrayList<>();
                profile.add(n);
                profile.add(p);
                profile.add(r);
                Intent intent = new Intent(NewgroupActivity.this, Family2Activity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                finish();

            }
        });


        ImageButton backbutton = findViewById(R.id.imageView13);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }
}






