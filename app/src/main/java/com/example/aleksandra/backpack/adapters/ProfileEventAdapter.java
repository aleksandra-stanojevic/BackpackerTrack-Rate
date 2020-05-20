package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.activities.EventActivity;
import com.example.aleksandra.backpack.Models.EventModel;

import java.util.List;

public class ProfileEventAdapter extends RecyclerView.Adapter<ProfileEventAdapter.ViewHolder> {
    private List<EventModel> list;
    private Context context;
    private static boolean join = false;

    public ProfileEventAdapter(List<EventModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProfileEventAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_profile_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileEventAdapter.ViewHolder holder, int position) {
     /*   holder.comment.setText(list.get(position).getComment());
        holder.title.setText(list.get(position).getPlaceName());
        holder.detail.setText(list.get(position).getPlaceLocation());
        holder.timePassed.setText(list.get(position).getTimePassed());*/
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, detail, comment, timePassed;
        private Button btnJoin;

        private ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tw_profile_title);
            detail = itemView.findViewById(R.id.tw_profile_detail);
            comment = itemView.findViewById(R.id.tw_profile_comment);
            timePassed = itemView.findViewById(R.id.tw_profile_time_passed);
            btnJoin = itemView.findViewById(R.id.bt_profile_join);
            itemView.setOnClickListener(this);

            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(join) {
                        btnJoin.setBackgroundColor(context.getResources().getColor(R.color.blue));
                        btnJoin.setText("Joined");
                        join = false;
                    } else {
                        btnJoin.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                        btnJoin.setText(R.string.join);
                        join = true;
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(BackpackApplication.getAppContext(), EventActivity.class);
            context.startActivity(i);
        }
    }
}


