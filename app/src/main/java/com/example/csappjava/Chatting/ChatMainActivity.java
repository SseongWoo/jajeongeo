package com.example.csappjava.Chatting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csappjava.Community.CommunityActivity;
import com.example.csappjava.MainActivity;
import com.example.csappjava.Marketplace.MarketplaceActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.Setting.SettingMain;
import com.example.csappjava.adapters.ChatAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class ChatMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> list;
    String username,Nickname;
    RecyclerView userList;
    ChatAdapter userAdapter;
    String sch;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmain);

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbarchat);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("채팅");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {    //네비게이션 메뉴 누를때 발생하는 이벤트
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                int id = item.getItemId();
                Intent intent;
                // 각 메뉴 클릭시 이뤄지는 이벤트
                switch (id) {
                    case R.id.nav_home:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(ChatMainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_community:
                        intent = new Intent(ChatMainActivity.this, CommunityActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                        break;

                    case R.id.nav_marketplace:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        intent = new Intent(ChatMainActivity.this, MarketplaceActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    case R.id.nav_chatroome:
                        //Toast.makeText(CommunityActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                        Toast.makeText(ChatMainActivity.this, "채팅방입니다.", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_settings:
                        intent = new Intent(ChatMainActivity.this, SettingMain.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });


        //---------------
        username = mAuth.getCurrentUser().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        list = new ArrayList<>();
        userList = (RecyclerView)findViewById(R.id.userList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatMainActivity.this,1);
        userList.setLayoutManager(layoutManager);
        userAdapter = new ChatAdapter(ChatMainActivity.this,list, ChatMainActivity.this, username, Nickname);
        userList.setAdapter(userAdapter);

        sch = Mydata.getMyschool();

        list();

    }




        //----------------------
    public void list (){
        reference.child("cs").child(sch).child("Chat").child(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                if(!snapshot.getKey().equals(username)){
                    list.add(snapshot.getKey());
                    userAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}