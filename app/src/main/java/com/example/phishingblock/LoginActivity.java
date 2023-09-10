package com.example.phishingblock;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText editTextId;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private Button buttonSignUp;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //파일 접근 권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                this.startActivity(intent);
            }
        }


        firebaseAuth = FirebaseAuth.getInstance();

        editTextId = (EditText) findViewById(R.id.id);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonSignUp = (Button) findViewById(R.id.newaccount);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);

            }
        });

        buttonLogIn = (Button) findViewById(R.id.loginbutton);

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextId.getText().toString().trim();
                String pwd = editTextPassword.getText().toString().trim();
                if (email.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter login and password", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) { //로그인 성공
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        String uid = firebaseAuth.getUid(); //uid get
                                        intent.putExtra("uid", uid);

                                        startActivity(intent);
                                    } else { //로그인 실패
                                        Toast.makeText(LoginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

        });
        //로그인 단축키
        buttonLogIn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String email = "suomi7lumi@naver.com";
                String pwd = "20080429";
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    String uid = firebaseAuth.getUid();
                                    intent.putExtra("uid", uid);

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "로그인 오류", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return true;
            }
        });


    }
}