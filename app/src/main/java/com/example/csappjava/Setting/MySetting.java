package com.example.csappjava.Setting;

import static com.example.csappjava.LoginActivity.context2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.csappjava.LoginActivity;
import com.example.csappjava.MainActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.models.ChatNicknameModel;
import com.example.csappjava.models.UserProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MySetting extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cs");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private ImageView imageView;
    private String userpass,sch,getprofiled;
    private TextView Mynickname;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    Uri imgUri;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysetting);


        imageView = findViewById(R.id.myprofile);
        Mynickname = findViewById(R.id.mynickname);

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
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");        //파이어베이스 스토리지 경로지정
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
                Mynickname.setText(chat_test.getNickNames());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        TextView btn_profile = findViewById(R.id.mysetting_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySetting.this,MySetting_profile.class);
                startActivity(intent);

            }
        });


        TextView btn_nicknamechange = findViewById(R.id.mysetting_nickname);
        btn_nicknamechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySetting.this, MySetting_nickname.class);
                startActivity(intent);
            }
        });

        // 로그아웃 하기
        TextView btn_logout = findViewById(R.id.mysetting_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pref = getSharedPreferences("Auto",Activity.MODE_PRIVATE);
                editor = pref.edit();
                editor.putBoolean("Autologin",false);
                editor.commit();
                mAuth.signOut();

                Intent intent = new Intent(MySetting.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MySetting.this,"로그아웃 완료",Toast.LENGTH_SHORT).show();
            }
        });


        // 탈퇴
        TextView btn_delete = findViewById(R.id.mysetting_deleteid);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pref = getSharedPreferences("Auto",Activity.MODE_PRIVATE);
                editor = pref.edit();
                editor.putBoolean("Autologin",false);
                editor.commit();

                mAuth.getCurrentUser().delete();
                Intent intent = new Intent(MySetting.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MySetting.this,"탈퇴 완료",Toast.LENGTH_SHORT).show();
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
