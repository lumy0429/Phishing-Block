package com.example.phishingblock;

import static com.example.phishingblock.MainActivity.recyclerview_name;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordDetailActivity extends AppCompatActivity {
    private WordAdapter adapter_word = new WordAdapter();

    ArrayList<String> words_list12 = new ArrayList<>();

    TextView detail_num , detail_time;
    String num,time;
    String result_1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorddetail);
        String pos = getIntent().getStringExtra("pos");


        ImageButton backbutton=findViewById(R.id.imageView13);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button changeButton=findViewById(R.id.button2);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SummaryActivity.class);
                String uid = getIntent().getStringExtra("uid");
                intent.putExtra("uid", uid);
                System.out.println(pos);
                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                mDatabase.getReference().child("user").child(uid).child("기록").child(""+pos).child("result")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String value = snapshot.getValue().toString();
                                result_1 = value;
                                System.out.println(result_1);
                                summary(result_1, intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }

        });


        detail_num = findViewById(R.id.textView47);
        detail_time = findViewById(R.id.textView49);

        Intent intent = getIntent();

        num = intent.getExtras().getString("num");
        time= intent.getExtras().getString("time");
        words_list12.clear();
        words_list12 = intent.getExtras().getStringArrayList("hhh");

        detail_num.setText(num);
        detail_time.setText(time);

        System.out.println("www"+words_list12);
        adapter_word.setList(words_list12);
        for(String s: words_list12){
            System.out.print(s+", ");
        }
        recyclerview_name = (RecyclerView) findViewById(R.id.recyclerview6);
        recyclerview_name.setAdapter(adapter_word);
        recyclerview_name.setLayoutManager(new LinearLayoutManager(RecordDetailActivity.this));
        adapter_word.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void summary(String result_1, Intent intent) {
        String prompt = result_1+"을 요약해줘";
        new Thread(() ->{
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            httpConn.setRequestProperty("Authorization", "Bearer " + System.getenv("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"));


            InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                    ? httpConn.getInputStream()
                    : httpConn.getErrorStream();
            Scanner s = new Scanner(responseStream).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            System.out.println(response);
        } catch(Exception e){
            e.printStackTrace();
        }}).start();

        //startActivity(intent);
    }

    public void summary1(String result_1, Intent intent){
        Gpt3Client gpt3Client = new Gpt3Client();

        String prompt = result_1+"을 요약해줘";

        Call<Gpt3Response> call = gpt3Client.generateText(prompt);
        call.enqueue(new Callback<Gpt3Response>() {
            @Override
            public void onResponse(Call<Gpt3Response> call, Response<Gpt3Response> response) {
                if (response.isSuccessful()) {
                    Gpt3Response gpt3Response = response.body();
                    if (gpt3Response != null && !gpt3Response.getChoices().isEmpty()) {
                        String generatedText = gpt3Response.getChoices().get(0).getText();
                        System.out.println(generatedText);
                        // 생성된 텍스트를 처리하는 코드 작성

                        startActivity(intent);
                    }
                } else {
                    System.out.println("실패");
                    // API 요청이 실패한 경우 처리
                }
            }

            @Override
            public void onFailure(Call<Gpt3Response> call, Throwable t) {
                // 네트워크 오류 등으로 인한 실패 처리
            }
        });
    }
}