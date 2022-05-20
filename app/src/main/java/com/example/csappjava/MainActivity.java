package com.example.csappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.Login.RegisterActivity2;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        startActivity(intent);
        finish(); // 현재 엑티비티 파괴 / 돌아오기 불가능
    }

}
