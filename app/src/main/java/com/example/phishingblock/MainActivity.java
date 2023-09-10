package com.example.phishingblock;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static com.example.phishingblock.MainAdapter.main_record_list;
import static com.example.phishingblock.MyService.getNotifyId;
import static com.example.phishingblock.MyThread.result;
import static com.example.phishingblock.MyThread.time;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static RecyclerView recyclerview_name;
    public static MainAdapter adapter_main;
    public static String uid, name_1;
    public static String number;
    public static DatabaseReference mDatabase;
    public HashSet<String> words = new HashSet<>();
    static public HashMap<String, String> friends = new HashMap<>();
    public static String message;
    public static Context context;


    MyThread.AuthSampleThread asT = new MyThread.AuthSampleThread();
    MyThread.RequestThread rT = new MyThread.RequestThread();
    MyThread.GetTransThread gT = new MyThread.GetTransThread();

    static String mLastState;
    Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한 한번에 띄우기 위함
        context = this;
        permissionCheck(new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.SEND_SMS,
                Manifest.permission.POST_NOTIFICATIONS,

        });

        //감지내역 업데이트
        adapter_main = new MainAdapter(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainRecordItem item) {
                return;
            }
        });
        adapter_main.setList(main_record_list);
        recyclerview_name = (RecyclerView) findViewById(R.id.recyclerview11);
        recyclerview_name.setAdapter(adapter_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerview_name.setLayoutManager(linearLayoutManager);
        adapter_main.notifyDataSetChanged();


    }

    //권한 한번에 띄우기 위함
    @Override
    protected void onResume() {
        super.onResume();
        permissionCheck(new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.SEND_SMS,
                Manifest.permission.POST_NOTIFICATIONS,
        });
    }

    public void main() {
        System.out.println("Create");
        System.out.println(friends);


        //foreground 실행
        Intent serviceIntent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(serviceIntent);
        else startService(serviceIntent);

        //uid 가져옴
        uid = getIntent().getStringExtra("uid");
        if (uid == null) {
            uid = FirebaseAuth.getInstance().getUid();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //firebase에서 데이터 가져오기
        mDatabase.child("user").child(uid).child("이름").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String value = snapshot.getValue().toString();
                name_1 = value;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mDatabase.child("user").child("word").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot t : dataSnapshot.getChildren()) {
                    words.add(t.getKey());
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, String.valueOf(databaseError.toException()), Toast.LENGTH_SHORT).show();

            }
        });

        System.out.println(uid);
        mDatabase.child("user").child(uid).child("friend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot t : dataSnapshot.getChildren()) {

                    if (t != null) {
                        friends.put(t.getKey(), t.getValue().toString());
                    }


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, String.valueOf(databaseError.toException()), Toast.LENGTH_SHORT).show(); // 에러문 출력
            }
        });

        mDatabase.child("user").child(uid).child("기록").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                main_record_list.clear();

                for (DataSnapshot d : snapshot.getChildren()) {
                    MainRecordItem item = d.getValue(MainRecordItem.class);
                    item.pos = main_record_list.size();
                    main_record_list.add(item); //감지내용 업데이트

                }
                adapter_main.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Button recordButton = findViewById(R.id.button0);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });


        Button familyButton = findViewById(R.id.button4);
        familyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(friends);
                Integer cnt = friends.size(); //가족 목록 유지 위함
                Intent intent;
                if (cnt == 0) {
                    intent = new Intent(getApplicationContext(), Family1Activity.class);
                } else {
                    intent = new Intent(getApplicationContext(), Family2Activity.class);
                }
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        Button settingButton = findViewById(R.id.button5);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        //전화 상태 변화 확인
        TelephonyManager telephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                String mState = String.valueOf(state);
                if (mState.equals(mLastState)) { // 두번 호출되는 문제 해결
                    return;
                } else {
                    mLastState = mState;
                }

                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE: //대기중, 통화 중이 아닐 때
                        System.out.println("CALL_IDLE");
                        Toast.makeText(MainActivity.this, "CALL_IDLE", Toast.LENGTH_SHORT).show();
                        if (flag) { //MyThread(STT) 실행
                            flag = false;
                            asT.start();
                            try {
                                asT.join();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            rT.start();
                            try {
                                rT.join();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            gT.start();
                            try {
                                gT.join();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int flag = 0;
                            MainRecordItem item = null;

                            for (String c : words) {
                                if (result.contains(c)) { //의심단어가 통화 내용에 포함되어 있는지 확인
                                    if (flag == 0) {
                                        System.out.println(words);


                                        uid = getIntent().getStringExtra("uid");
                                        if (uid == null) {
                                            uid = FirebaseAuth.getInstance().getUid();
                                        }

                                        for (String num : friends.values()) {
                                            System.out.println(num);
                                            String line1 = name_1 + "님의 전화에서 " + time + "에 보이스피싱 의심단어 감지되었습니다.";
                                            String line2 = "감지된 전화번호 : " + number;
                                            String line3 = "감지된 단어 : " + c;
                                            message = line1 + "\n" + line2 + "\n" + line3;
                                            sendSMS(num, message); //감지 문자 전송
                                            System.out.println("success");
                                        }
                                        notifyDanger(message); //감지 푸쉬 알람 전송
                                        flag = 1;
                                        String[] a = result.split("msg");
                                        System.out.println(a[1]);
                                        String[] b = a[1].split("\"");
                                        System.out.println(b[2]);

                                        item = new MainRecordItem(number, time, c, new ArrayList<String>(),b[2], main_record_list.size()); //감지내역 업데이트
                                        item.bords_list2.add(c); //감지단어 리스트에 감지된 단어 모두 추가


                                    } else {
                                        item.bords_list2.add(c);

                                    }
                                }


                            }
                            if (flag == 1) {
                                item.pos = main_record_list.size();
                                System.out.println("1234"+main_record_list.size());
                                main_record_list.add(item);
                                for (MainRecordItem s : main_record_list) {
                                    System.out.println("bords_list2" + s.bords_list2);
                                }
                                //firebase에 감지내역 추가
                                mDatabase.child("user").child(uid).child("기록").setValue(main_record_list);
                            }

                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK: //통화 중일때
                        System.out.println("CALL_OFFHOOK");
                        Toast.makeText(MainActivity.this, "CALL_OFFHOOK", Toast.LENGTH_SHORT).show();
                        number = incomingNumber; //걸려온 전화번호 가져오기
                        System.out.println(number);
                        flag = true;
                        break;

                    case TelephonyManager.CALL_STATE_RINGING: //통화 벨 울릴때
                        System.out.println("CALL_RINGING");
                        Toast.makeText(MainActivity.this, "CALL_RINGING", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);


    }

    public void notifyDanger(String text) { //푸쉬 알람 전송 함수

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Phishing Block");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        notificationManager.notify(getNotifyId(), builder.build());

    }


    private void permissionCheck(String[] allPermissionNeeded) { //권한 체크 함수
        if (Build.VERSION.SDK_INT > 22) {
            List<String> permissionNeeded = new ArrayList<>();
            for (String permission : allPermissionNeeded) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                    permissionNeeded.add(permission);
            }
            if (permissionNeeded.size() > 0) { //거절된 권한에 대해 다시 요청
                requestPermissions(permissionNeeded.toArray(new String[0]), 1);
            } else {
                main();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> repermissions = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1) {
                repermissions.add(permissions[i]);
                System.out.println(permissions[i]);
            }
        }
        System.out.println(repermissions.isEmpty());
        if (repermissions.isEmpty()) {
            main();
        } else {
            permissionCheck(repermissions.toArray(new String[0]));
        }
    }

    public void sendSMS(String num, String message) { //문자 발송 함수
        SmsManager sms = SmsManager.getDefault();
        List<String> messages = sms.divideMessage(message);
        for (String msg : messages) {
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), FLAG_IMMUTABLE);
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toast.makeText(MainActivity.this, "전송 완료", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            // 전송 실패
                            Toast.makeText(MainActivity.this, "전송 실패", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_SENT_ACTION"));

            sms.sendTextMessage(num, null, msg, pi, null);
        }
    }
}









