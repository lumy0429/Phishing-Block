package com.example.phishingblock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.newaccount).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.newaccount:
                    signUp();

                    break;
            }
        }
    };

    private void dataSend(){ //입력된 정보 firebase에 저장
        EditText emailEdit=(EditText)findViewById(R.id.id);
        String value=emailEdit.getText () .toString();

        EditText nameEdit=(EditText)findViewById(R.id.name);
        String v=nameEdit.getText () .toString();

        EditText idEdit=(EditText)findViewById(R.id.phone);
        String val=idEdit.getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance () ;
        DatabaseReference myRef = database. getReference ();
        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.d("uid", uid);
        myRef. child(  "user") .child (uid).child("전화번호").setValue (val);
        myRef. child(  "user") .child (uid).child("이름").setValue (v);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);

    }


    private void signUp(){
        String name=
                ((EditText)findViewById(R.id.name)).getText().toString();
        String id=
            ((EditText)findViewById(R.id.id)).getText().toString();
        String phone=
                ((EditText)findViewById(R.id.phone)).getText().toString();
        String password=
            ((EditText)findViewById(R.id.textView26)).getText().toString();
        String passwordCheck=
                ((EditText)findViewById(R.id.password)).getText().toString();



        if(name.length()>0 && id.length()>0 && phone.length()>0 && password.length()>0 && passwordCheck.length()>0) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dataSend();
                        } else {
                            Toast.makeText(SignupActivity.this, " 회원가입에 실패했습니다."
                                    , Toast.LENGTH_SHORT).show();
                        }

                    }


                });
            }
        }
    }
}




