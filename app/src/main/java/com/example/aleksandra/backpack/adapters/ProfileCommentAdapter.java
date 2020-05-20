package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.activities.EventActivity;
import com.example.aleksandra.backpack.Models.CommentModel;

import java.util.List;

public class ProfileCommentAdapter extends RecyclerView.Adapter<ProfileCommentAdapter.ViewHolder> {
    private List<CommentModel> list;
    private Context context;

    public ProfileCommentAdapter(List<CommentModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProfileCommentAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_profile_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileCommentAdapter.ViewHolder holder, int position) {
        holder.comment.setText(list.get(position).getComment());
        holder.title.setText(list.get(position).getTitle());
        holder.detail.setText(list.get(position).getDetails());
        holder.timePassed.setText(list.get(position).getTimePassed());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, detail, comment, timePassed;

        private ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tw_profile_title);
            detail = itemView.findViewById(R.id.tw_profile_detail);
            comment = itemView.findViewById(R.id.tw_profile_comment);
            timePassed = itemView.findViewById(R.id.tw_profile_time_passed);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            PopupMenu pm=new PopupMenu();

            Intent i = new Intent(BackpackApplication.getAppContext(), EventActivity.class);
            context.startActivity(i);
        }

    }
}


