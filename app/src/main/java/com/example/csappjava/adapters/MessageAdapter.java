package com.example.csappjava.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csappjava.R;
import com.example.csappjava.models.MessageModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {                   //메세지 어뎁터
    Context context;
    List<MessageModel> list;
    Activity activity;
    String userName;
    Boolean state;
    int send =0, received=1;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");
    StorageReference storageRef = storage.getReference();

    public MessageAdapter(Context context, List<MessageModel> list, Activity activity, String userName) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
        state = false;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == send){
            view= LayoutInflater.from(context).inflate(R.layout.send_layout, parent, false);
            return new ViewHolder(view);
        }
        else {
            view= LayoutInflater.from(context).inflate(R.layout.received_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, final int position) {
        if(list.get(position).getText().toString().contains("mapmessage")){
            holder.textView.setText("지도확인하기");
            holder.textView.setTextColor(Color.BLUE);
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else if(list.get(position).getText().toString().contains("message/")){
            String uri = list.get(position).getText().toString();
            Log.d("LOGTEST",  uri);
            Uri u = Uri.parse(uri);
            holder.textView.setVisibility(View.GONE);
            holder.photo.setVisibility(View.VISIBLE);

            storageRef.child(uri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //이미지 로드 성공시
                    Glide.with(context)
                            .load(uri)
                            .into(holder.photo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                }
            });
        }
        else{
            holder.textView.setText(list.get(position).getText().toString());
        }



    }

    @Override
    public int getItemCount() { //listedekilerin uzunluğu, sayısı
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);
            if(state == true){
                textView = itemView.findViewById(R.id.sendView);
                photo = itemView.findViewById(R.id.sendimageview);
            }
            else {
                textView = itemView.findViewById(R.id.CreceivedView);
                photo = itemView.findViewById(R.id.recevidimageview);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (onLongItemClickListener != null) {
                            onLongItemClickListener.onLongItemClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getFrom().equals(userName)){
            state = true;
            return send;
        }
        else {
            state = false;
            return received;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private PostAdapterCommunity.OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(PostAdapterCommunity.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    public interface OnLongItemClickListener {
        void onLongItemClick(int pos);
    }

    private PostAdapterCommunity.OnLongItemClickListener onLongItemClickListener = null;

    public void setOnLongItemClickListener(PostAdapterCommunity.OnLongItemClickListener listener) {
        this.onLongItemClickListener = listener;
    }
}
