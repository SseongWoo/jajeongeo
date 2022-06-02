package com.example.csappjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.Login.FindpwdActivity;
import com.example.csappjava.Login.RegisterActivity;
import com.example.csappjava.Login.RegisterActivity2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.CheckBox;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    public FirebaseUser user;
    //private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스
    private EditText mEtEmail,mEtpwd;          // 회원가입 입력필드
    public static Context context2;
    public String nextem2,sch,myuid;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    public CheckBox checkBox;
    Boolean check;

    private  Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context2 = this;
        //nextem2 = "kangwon";      // 임시 테스트용
        //FirebaseUser user = null;

        if(!FirebaseAuth.getInstance().equals(null)){
            mFirebaseAuth = FirebaseAuth.getInstance();
            user = mFirebaseAuth.getCurrentUser();
        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cs");


        mEtEmail = findViewById(R.id.et_email);
        mEtpwd = findViewById(R.id.et_pwd);
        btn_login = findViewById(R.id.btn_login);

        checkBox = findViewById(R.id.autologin_check);
        btn_login.setEnabled(false);

        pref = getSharedPreferences("Auto", Activity.MODE_PRIVATE);
        editor = pref.edit();

        check = pref.getBoolean("Autologin",false);

        checkBox.setChecked(check);

        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable emailE) {
                if(emailE.length()>0)
                {
                    mEtpwd.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void afterTextChanged(Editable pwdP) {
                            if(pwdP.length()>5 && emailE.length()>0){
                                btn_login.setEnabled(true);
                                btn_login.setBackgroundColor(Color.BLUE);
                            }
                            else{
                                btn_login.setEnabled(false);
                                btn_login.setBackgroundColor(Color.GRAY);
                            }
                        }
                    });
                }
                else{
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundColor(Color.GRAY);
                }
            }
        });

        if(user != null &&checkBox.isChecked())
        {
            if (user != null && !user.isEmailVerified()) {
                Toast.makeText(LoginActivity.this, "로그인 실패, 이메일 인증을 하세요.", Toast.LENGTH_SHORT).show();
                editor.putBoolean("Autologin",false);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, RegisterActivity2.class);
                startActivity(intent);
                finish(); // 현재 엑티비티 파괴 / 돌아오기 불가능
                // 이메일 미인증시
            } else {
                Intent intent = new Intent(LoginActivity.this, MainLoadingActivity.class);
                //Intent intent = new Intent(LoginActivity.this, ChatMainActivity.class);
                if (user != null) {
                    myuid = user.getUid();
                }

                intent.putExtra("userName", myuid);
                startActivity(intent);
                finish(); // 현재 엑티비티 파괴 / 돌아오기 불가능 / 이메일 인증시
            }
        }
        else{
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 로그인 요청
                    mFirebaseAuth = FirebaseAuth.getInstance();

                    String strEmail =mEtEmail.getText().toString();
                    String strPwd = mEtpwd.getText().toString();
                    sch = strEmail;
                    mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // 로그인 성공
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                myuid = user.getUid();
                                if (!user.isEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "로그인 실패, 이메일 인증을 하세요.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, RegisterActivity2.class);
                                    startActivity(intent);
                                    finish(); // 현재 엑티비티 파괴 / 돌아오기 불가능
                                    // 이메일 미인증시
                                }
                                else{
                                    if(checkBox.isChecked())
                                    {
                                        editor.putBoolean("Autologin",checkBox.isChecked());        // 체크시 값 저장 true

                                    }
                                    else{
                                        editor.putBoolean("Autologin",checkBox.isChecked());        // 체크시 값 저장 false
                                    }
                                    editor.commit();                                                   // commit 해줘야지 값 저장 됨


                                    Intent intent = new Intent(LoginActivity.this, MainLoadingActivity.class);
                                    //Intent intent = new Intent(LoginActivity.this, ChatMainActivity.class);
                                    intent.putExtra("userName",myuid);
                                    startActivity(intent);
                                    finish(); // 현재 엑티비티 파괴 / 돌아오기 불가능 / 이메일 인증시
                                }
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "로그인 실패, 아이디 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                                btn_login.setEnabled(false);
                                btn_login.setBackgroundColor(Color.GRAY);
                                mEtEmail.setText(null);
                                mEtpwd.setText(null);
                            }
                        }
                    });
                }
            });
            Button btn_register = findViewById(R.id.btn_register);
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    // 회원가입으로 이동
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            Button btn_findpwd = findViewById(R.id.btn_lostpwd);
            btn_findpwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    // 비밀번호찾기로 이동
                    Intent intent = new Intent(LoginActivity.this, FindpwdActivity.class);
                    startActivity(intent);
                }
            });

        }


    }
}