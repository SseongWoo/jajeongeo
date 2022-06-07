package com.example.csappjava.Chatting;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csappjava.Community.CommunityActivity2;
import com.example.csappjava.Mappoint;
import com.example.csappjava.Maptest;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.Setting.MySetting_profile;
import com.example.csappjava.adapters.MessageAdapter;
import com.example.csappjava.adapters.PostAdapterCommunity;
import com.example.csappjava.models.ChatNicknameModel;
import com.example.csappjava.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ChatActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;

    String userName,otherName;
    TextView chatUserName;
    ImageView backImage , sendImage, addmenu;
    EditText chatEditText;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView chatRecyclerView;
    MessageAdapter messageAdapter;
    List<MessageModel> list;
    String sch;
    String emails;
    Button send_image, send_mypoint, send_point;
    LinearLayout add;
    Dialog dialogimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

       // userName = getIntent().getExtras().getString("username");
      //  otherName =  getIntent().getStringExtra("document");
        //userName  = ((LoginActivity)LoginActivity.context2).myuid;
        otherName = getIntent().getExtras().getString("othername");
        userName = mAuth.getCurrentUser().getUid();


        sch = Mydata.getMyschool();

        chatUserName = (TextView)findViewById(R.id.chatUserName);
        backImage = (ImageView)findViewById(R.id.backImage);
        sendImage = (ImageView)findViewById(R.id.sendImage);
        chatEditText = (EditText)findViewById(R.id.chatEditText);

        addmenu = findViewById(R.id.addmenu);
        send_image = findViewById(R.id.send_image);
        send_mypoint = findViewById(R.id.send_mypoint);
        send_point = findViewById(R.id.send_point);
        add = findViewById(R.id.add);
        add.setVisibility(View.GONE);

        list = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        dialogimg = new Dialog(ChatActivity.this);       // Dialog 초기화
        dialogimg.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        dialogimg.setContentView(R.layout.dialog_photo);             // xml 레이아웃 파일과 연결
        imgdialog = dialogimg.findViewById(R.id.imagedialog);
        dialogimg.setCancelable(false);


        // 닉네임 채팅창에 표시하기
        reference.child("cs").child(sch).child("user").child(otherName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatNicknameModel chat_test = snapshot.getValue(ChatNicknameModel.class);
                String nick = chat_test.getNickNames();
                chatUserName.setText(nick);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //-------------------------

        send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add.getVisibility() == View.GONE){
                    add.setVisibility(View.VISIBLE);
                }
                else{
                    add.setVisibility(View.GONE);
                }
            }
        });

        send_mypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });

        send_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, Mappoint.class);
                startActivity(intent);
            }
        });



        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatMainActivity.class);
                intent.putExtra("UserName", userName);
                startActivity(intent);
            }
        });

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String message = chatEditText.getText().toString();
                    chatEditText.setText("");
                    sendMessage(message);
            }
        });

            loadMessage();

        chatRecyclerView = (RecyclerView)findViewById(R.id.chatRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this, 1);
        chatRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this, list, ChatActivity.this,userName);
        chatRecyclerView.setAdapter(messageAdapter);

        messageAdapter.setOnItemClickListener(new PostAdapterCommunity.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                MessageModel mm = list.get(pos);
                String text = mm.getText();

                if(text.contains("mapmessage")){
                    String mapdata = text.replace("mapmessage","");
                    Intent intent = new Intent(ChatActivity.this, Maptest.class);
                    intent.putExtra("mapdata", mapdata);
                    startActivity(intent);
                }
                else if(text.contains("message/")){
                    showdialogimg(text);
                }
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!Mydata.getSend().equals("")){
            sendMessage(Mydata.getSend());
            Mydata.setSend("");
        }
    }

    public void sendMessage(String text){
        final String key = reference.child("cs").child(sch).child("Chat").child(userName).child(otherName).push().getKey();
        final Map messageMap = new HashMap();
        messageMap.put("text" , text);
        messageMap.put("from" , userName);
       // sendname.setText(userName);
        reference.child("cs").child(sch).child("Chat").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reference.child("cs").child(sch).child("Chat").child(otherName).child(userName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                }
            }
        });

    }

    public void loadMessage(){
        reference.child("cs").child(sch).child("Chat").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                list.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(list.size()-1);
            }
            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) { }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) { }
        });
    }

    private void map(){                 //자신의 위치를 얻는 작업
        double cur_lat, cur_lon;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc_Current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        cur_lat = loc_Current.getLatitude();                //위도
        cur_lon = loc_Current.getLongitude();               //경도

        String stdb1 = Double.toString(cur_lat);
        String stdb2 = Double.toString(cur_lon);

        sendMessage("mapmessage"+stdb1+","+stdb2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {         //갤러리에서 사진을 고르는 작업
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    Uri uri = data.getData();
                    uploadImage(uri);

                }catch(Exception e)
                { }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImage(Uri uri){                                          //갤러리에서 고른 사진을 파이어베이스서버에 올리는 작업
        StorageReference storageRef = firebaseStorage.getReference();
        Uri file = uri;
        String suri = "message/" + System.currentTimeMillis() +".png";

        // stroage images에 절대경로파일 저장
        StorageReference riversRef = storageRef.child(suri);
        UploadTask uploadTask = riversRef.putFile(file);
        sendMessage(suri);

    }

    private ImageView imgdialog;
    private PhotoViewAttacher mAttacher;    //이미지 확대하는 기능

    void showdialogimg(String pos){                                                             //이미지 다이얼로그를 생성하는 작업
        dialogimg.show(); // 다이얼로그 띄우기
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");
        StorageReference storageRef = storage.getReference();
        mAttacher = new PhotoViewAttacher(imgdialog);

        storageRef.child(pos).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imgdialog);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
            }
        });

        dialogimg.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogimg.dismiss();
            }
        });
    }
}