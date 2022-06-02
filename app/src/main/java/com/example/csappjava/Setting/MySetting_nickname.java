package com.example.csappjava.Setting;

import static com.example.csappjava.LoginActivity.context2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.csappjava.LoginActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.models.ChatNicknameModel;
import com.example.csappjava.models.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MySetting_nickname extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cs");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private ImageView imageView;
    private String userpass,sch,getprofiled;
    private EditText ednick;
    private Button btn_change;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysetting_nick);

        imageView = findViewById(R.id.mysetting_nick_profile);
        ednick = findViewById(R.id.mynickname_change);
        btn_change = findViewById(R.id.mysetting_nickecheck);
        userpass = mAuth.getCurrentUser().getUid();
        sch = Mydata.getMyschool();

        Toolbar toolbar = findViewById (R.id.setoolbar);
        setSupportActionBar (toolbar); //액티비티의 앱바(App Bar)로 지정
        ActionBar actionBar = getSupportActionBar (); //앱바 제어를 위해 툴바 액세스
        actionBar.setDisplayHomeAsUpEnabled (true);


        // 유저 이미지 링크 가져오기
        reference.child(sch+"/user").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                getprofiled  = userProfile.getprofile();

                if(getprofiled!="null"){
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-8184e.appspot.com/");        //파이어베이스 스토리지 경로지정
                    StorageReference storageRef = storage.getReference();

                    storageRef.child(getprofiled).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .into(imageView);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // 유저 닉네임 가져오기기
        reference.child(sch+"/user").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatNicknameModel chat_test = snapshot.getValue(ChatNicknameModel.class);
                ednick.setText(chat_test.getNickNames());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ednick.getText().toString().length() < 2 ||ednick.getText().toString()==null) {
                    Toast.makeText(getApplicationContext(), "글자수가 적거나 공백입니다.", Toast.LENGTH_SHORT).show();
                }
                else if (ednick.getText().toString().length() > 6){
                    Toast.makeText(getApplicationContext(), "글자수가 많습니다 6글자 이하로 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else{
                    reference.child(sch + "/user").child(mAuth.getUid()).child("nickNames").setValue(ednick.getText().toString());         // 경로
                    finish();
                    Toast.makeText(getApplicationContext(), "닉네임 변경 완료.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate (R.menu.mainoption, menu);

        return true;
    }

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                finish ();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected (item);
        }
    }


}