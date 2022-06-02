package com.example.csappjava.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.Community.CommunityActivity2;
import com.example.csappjava.FirebaseID;
import com.example.csappjava.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스
    public EditText mEtEmail, mEtpwd,mEtmail2, mnickname;    // 회원가입 입력필드
    public static Context context_next;         // 값을 다른 액티비티로 넘기기
    public String nextem;                       // 넘길 변수

    private Button mBtnReister;                  // 회원가입 버튼
    Pattern pattern = Patterns.EMAIL_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cs");
        mEtEmail = findViewById(R.id.et_email);
        mEtmail2 = findViewById(R.id.school);
        mEtpwd = findViewById(R.id.et_pwd);
        mBtnReister = findViewById(R.id.btn_register);      // 가입완료 버튼
        nextem = mEtmail2.getText().toString();
        //mnickname = findViewById(R.id.nick);
        context_next = this;
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()                    // 에메일 링크...?
                .setUrl("https://cs.page.link/N8fh")
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.example.cs",true,null)
                .build();

        mBtnReister.setEnabled(false);                  // 회원가입 버튼 비활성회

        mEtEmail.addTextChangedListener(new TextWatcher() {                 // email 텍스트 감지
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {               // email = editable
                if(editable.length()>0){                                    // 이메일이 빈칸이 아닐때
                    mEtpwd.addTextChangedListener(new TextWatcher() {           // pwd 텍스트 감지
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                        @Override
                        public void afterTextChanged(Editable editables) {          // pwd = editables
                            if(editables.length()>5 && editable.length()>0)         // 이메을 0 초과, 비밀번호 5 초과일때만
                            { mBtnReister.setEnabled(true);                         // 버튼 활성화화
                                mBtnReister.setBackgroundColor(Color.BLUE);}
                            else { mBtnReister.setEnabled(false);                   // 아닐때 버튼 비활성화
                                mBtnReister.setBackgroundColor(Color.GRAY); }
                        }
                    });
                }
                else { mBtnReister.setEnabled(false);
                    mBtnReister.setBackgroundColor(Color.GRAY); }                     // 이메일이 0일때 버튼 비활성화
            }
        });

        mBtnReister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 시작
                mStore =  FirebaseFirestore.getInstance();
                String strEmail =mEtEmail.getText().toString() + "@" + mEtmail2.getText().toString() + ".ac.kr";
                // String strEmail =mEtEmail.getText().toString() + "@" + mEtmail2.getText().toString() + ".com";
                String strPwd = mEtpwd.getText().toString();
                //String strnick = mnickname.getText().toString();
                String strurl = "null";                          //포인트, 이미지경로
                String school = mEtmail2.getText().toString();
                String point = "0";

                EditText et_pwd,et_pwdck;
                et_pwd = findViewById(R.id.et_pwd);
                et_pwdck = findViewById(R.id.et_pwdck);
                String ckpwd1,ckpwd2;

                ckpwd1 = et_pwd.getText().toString();
                ckpwd2 = et_pwdck.getText().toString();

                if(ckpwd1.equals(ckpwd2)){
                    //Firebase 인증처리
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                                UserAccount account = new UserAccount();
                                account .setIdToken(firebaseUser.getUid());
                                account .setEmailID(firebaseUser.getEmail());
                                account .setNickNames("테스트계정");
                                account .setPassword(strPwd);
                                account.setprofile("null");

                                Map<String,Object> userMapR = new HashMap<>();
                                userMapR.put(FirebaseID.userId, firebaseUser.getUid());
                                userMapR.put(FirebaseID.email, firebaseUser.getEmail());
                                userMapR.put(FirebaseID.school, school);
                                //userMapR.put(FirebaseID.password,strPwd);
                                userMapR.put(FirebaseID.nickname,"");
                                userMapR.put(FirebaseID.department,"");
                                userMapR.put(FirebaseID.campus,"");
                                userMapR.put(FirebaseID.affiliation,"");
                                userMapR.put(FirebaseID.point,point);
                                userMapR.put(FirebaseID.img,strurl);

                                mDatabaseRef.child(mEtmail2.getText().toString()+"/user").child(firebaseUser.getUid()).setValue(account);         // 경로
                                //mDatabaseRef.child("kangwon/user").child(firebaseUser.getUid()).setValue(account );         // 경로

                                mStore.collection(FirebaseID.user).document(firebaseUser.getUid()).set(userMapR, SetOptions.merge());
                                finish();

                                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this,RegisterActivity2.class);
                                intent.putExtra("school",school);
                                startActivity(intent);
                            }
                            else                                              // 로그인 실패시
                            {
                                if(pattern.matcher(strEmail).matches()){        // 중복된 이메일이면
                                    mBtnReister.setBackgroundColor(Color.GRAY);
                                    Toast.makeText(RegisterActivity.this, "중복된 이메일이 있습니다.", Toast.LENGTH_SHORT).show();
                                }

                                else{
                                    Toast.makeText(RegisterActivity.this, "중복된 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                }

                                mBtnReister.setEnabled(false);                  // 버튼 비활성화
                                mBtnReister.setBackgroundColor(Color.GRAY);
                                mEtpwd.setText(null);                           // 비밀번호 텍스트 초기화
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterActivity.this, "비밀번호가 맞지않습니다. 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                    et_pwd.setText("");
                    et_pwdck.setText("");
                }

            }
        });
    }
}