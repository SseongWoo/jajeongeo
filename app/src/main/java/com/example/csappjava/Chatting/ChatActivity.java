package com.example.csappjava.Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.adapters.MessageAdapter;
import com.example.csappjava.models.ChatNicknameModel;
import com.example.csappjava.models.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String userName,otherName;
    TextView chatUserName;
    ImageView backImage , sendImage;
    EditText chatEditText;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView chatRecyclerView;
    MessageAdapter messageAdapter;
    List<MessageModel> list;
    String sch;
    String emails;

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

        list = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();


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
               // Log.v("TTTTEEEESSSSTTTT",userName);
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
}