package com.example.phishingblock;

import static com.example.phishingblock.MainActivity.adapter_main;
import static com.example.phishingblock.MainActivity.recyclerview_name;
import static com.example.phishingblock.MainActivity.uid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    static ArrayList<String> words_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        uid = getIntent().getStringExtra("uid");

        recyclerview_name = (RecyclerView) findViewById(R.id.recyclerview111);
        adapter_main =  new MainAdapter(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainRecordItem item) { //목록 클릭 시 상세내용 확인
                Intent intent = new Intent(getApplicationContext(),RecordDetailActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                intent.putExtra("num", item.num);
                intent.putExtra("time", item.time);
                intent.putExtra("pos",""+item.pos);
                System.out.println(item.pos);
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                mDatabase.getReference().child("user").child(uid).child("기록").child(""+item.pos)
                        .child("bords_list2")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot ds: snapshot.getChildren()){
                                    item.bords_list2.add(ds.getValue(String.class));
                                }
                                System.out.println("RecordActivity"+item.bords_list2);
                                words_list = item.bords_list2;
                                System.out.println("aaaa"+words_list);
                                intent.putStringArrayListExtra("hhh", item.bords_list2);

                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
        });

        recyclerview_name.setAdapter(adapter_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerview_name.setLayoutManager(linearLayoutManager);
        adapter_main.notifyDataSetChanged();



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
    }
}
