package com.example.csappjava.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csappjava.LoginActivity;
import com.example.csappjava.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Pattern;

public class FindpwdActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private EditText mEmail;
    private Button sendEmail;
    private FirebaseAuth mFirebaseAuth;
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        mEmail = findViewById(R.id.pwd_email);
        sendEmail = findViewById(R.id.btn_sendEmail);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailAddress = mEmail.getText().toString();

                mFirebaseAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(pattern.matcher(emailAddress).matches()){
                                    if (task.isSuccessful()) {
                                        Toast.makeText(FindpwdActivity.this, "이메일 전송 완료. 비밀번호를 이메일을 통해 변경해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FindpwdActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                else{
                                    Toast.makeText(FindpwdActivity.this, "옳바른 이메일 형식을 입력해 주세요.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}
