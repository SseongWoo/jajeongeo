package com.example.csappjava.adapters;

import android.app.ActivityOptions;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.csappjava.Community.CommunityActivity2;
import com.example.csappjava.Marketplace.MarketplaceActivity;
import com.example.csappjava.R;
import com.example.csappjava.models.PostMarketplace;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.List;

public class PostAdapterMarketplace extends RecyclerView.Adapter<PostAdapterMarketplace.PostViewHolder> {           //중고거래 어뎁터

    private List<PostMarketplace> datas;

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://csapp-a3fce.appspot.com/");
    StorageReference storageRef = storage.getReference();

    public PostAdapterMarketplace(List<PostMarketplace> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostMarketplace data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.time.setText(data.getTime());

        if(!data.getTransaction().equals("true")){
            holder.price.setText(data.getPrice()+" 원");
        }
        else{
            holder.price.setText("거래 완료");
            holder.title.setPaintFlags(holder.title.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            //holder.contents.setPaintFlags(holder.contents.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        String img = data.getImg();
        String[] array;

        img = img.replace("[","");
        img = img.replace("]","");
        img = img.replaceAll(" ","");
        array = img.split(",");

        storageRef.child(array[0]).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(holder.imgView.getContext())
                        .load(uri)
                        .into(holder.imgView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(int pos);
    }

    private OnLongItemClickListener onLongItemClickListener = null;

    public void setOnLongItemClickListener(OnLongItemClickListener listener) {
        this.onLongItemClickListener = listener;
    }


    class PostViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView imgView;
        private TextView price, time;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title);
            price = itemView.findViewById(R.id.market_price);
            imgView = itemView.findViewById(R.id.marketplace_image);
            time = itemView.findViewById(R.id.item_post_mtime);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

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
}
