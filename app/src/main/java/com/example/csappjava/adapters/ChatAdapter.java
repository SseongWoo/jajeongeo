package com.example.csappjava.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csappjava.Chatting.ChatActivity;
import com.example.csappjava.Mydata;
import com.example.csappjava.R;
import com.example.csappjava.models.ChatNicknameModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {                     //채팅방목록 어뎁터
    Context context;
    List<String> list;
    Activity activity;
    String userName,Nickname;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    public ChatAdapter(Context context, List<String> list, Activity activity, String userName,String Nickname) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
        this.Nickname = Nickname;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        //sch = ((LoginActivity)LoginActivity.context2).nextem2;
        reference.child("cs").child(Mydata.getMyschool()).child("user").child(list.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatNicknameModel chat_test = snapshot.getValue(ChatNicknameModel.class);
                String nick = chat_test.getNickNames();
                holder.textView.setText(nick);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.userID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("username", userName);
                intent.putExtra("othername", list.get(position).toString());
                activity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LinearLayout userID;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.userName);
            userID = itemView.findViewById(R.id.userID);
        }
    }
}
