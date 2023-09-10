package com.example.phishingblock;

import static com.example.phishingblock.MainActivity.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Family2Activity extends AppCompatActivity {
    public String name;
    public String phone;

    RecyclerView recyclerview_name;
    static public FamilyAdapter adapter_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family2);

        String uid = getIntent().getStringExtra("uid"); //모든 페이지 마다 uid를 전달해줘야함

        //recyclerview 사용
        recyclerview_name = (RecyclerView) findViewById(R.id.recyclerview);
        adapter_name = new FamilyAdapter();

        recyclerview_name.setAdapter(adapter_name);
        recyclerview_name.setLayoutManager(new LinearLayoutManager(this));

        //추가한 친구 유지 위해 업데이트
        for(String key:friends.keySet()){
            adapter_name.updateList(new FamilyItem(key, friends.get(key)));
        }

        Button addbutton=findViewById(R.id.button12);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),NewgroupActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });








        ImageButton backbutton=findViewById(R.id.imageView13);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        Button Addbutton=findViewById(R.id.button12);
        Addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),NewgroupActivity.class);
                startActivity(intent);
            }
        });

    }
}