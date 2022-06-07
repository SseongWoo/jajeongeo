package com.example.csappjava.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity2 extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        Button btemail = findViewById(R.id.mailtest);
        Button emailcheck = findViewById(R.id.emailcheck);

        Intent intent = getIntent();
        String school = intent.getStringExtra("school");

        btemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity2.this, "이메일보냄.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText( RegisterActivity2.this, "이메일못보냄.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        emailcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reload();
                if (!user.isEmailVerified()) {
                    Toast.makeText(RegisterActivity2.this, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    // 이메일 미인증시
                }else{
                    Toast.makeText(RegisterActivity2.this, "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    // 이메일이 인증 되었을 때
                    Intent intent2 = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                    intent2.putExtra("school",school);
                    startActivity(intent2);
                    finish();
                }
                user.reload();
                if (!user.isEmailVerified()) {
                    Toast.makeText(RegisterActivity2.this, "인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    // 이메일 미인증시
                }else{
                    Toast.makeText(RegisterActivity2.this, "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    // 이메일이 인증 되었을 때
                    Intent intent2 = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                    intent2.putExtra("school",school);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }

}