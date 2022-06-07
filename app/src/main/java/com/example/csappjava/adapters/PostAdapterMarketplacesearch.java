package com.example.csappjava.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csappjava.R;
import com.example.csappjava.models.PostMarketplaceSearch;

import java.util.List;

public class PostAdapterMarketplacesearch extends RecyclerView.Adapter<PostAdapterMarketplacesearch.PostViewHolder> {           //중고거래 게시글 작성중 수업 데이터 검색 어뎁터

    private List<PostMarketplaceSearch> datas;

    public PostAdapterMarketplacesearch(List<PostMarketplaceSearch> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.marketplace_post_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostMarketplaceSearch data = datas.get(position);
        holder.professor.setText(data.getProfessor());
        holder.lecture.setText(data.getLecture());
        holder.department.setText(data.getDepartment());
        holder.year.setText(data.getYear());
        holder.month.setText(data.getMonth());
        Log.d("LOGTEST", "어댑터");
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
        private TextView professor, lecture, department, year, month;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            professor = itemView.findViewById(R.id.item_post_professor);
            lecture = itemView.findViewById(R.id.item_post_lecture);
            department = itemView.findViewById(R.id.item_post_department);
            year = itemView.findViewById(R.id.item_post_year);
            month = itemView.findViewById(R.id.item_post_month);

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
