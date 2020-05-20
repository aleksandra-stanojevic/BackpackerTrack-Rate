package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.Models.User;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.Models.PersonModel;
import com.example.aleksandra.backpack.utils.Utils;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<User> list;
    private Context context;

    public RankAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_people_going_to_event, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getFirst_name()+" "+list.get(position).getLast_name());
        holder.state.setText(list.get(position).getEmail());
        holder.points.setText(list.get(position).getRank()+" points");
      //  Utils.loadImageWithGlideCircle(Uri.parse(list.get(position).getImage()), holder.image);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, state, points;
        private ImageView image;

        private ViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
            setListeners();
        }

        private void findViews(View itemView) {
            name = itemView.findViewById(R.id.tw_people_going_to_event_name);
            state = itemView.findViewById(R.id.tw_people_going_to_event_state);
            image = itemView.findViewById(R.id.iv_people_going_to_event_image);
            points = itemView.findViewById(R.id.tw_going);
        }

        private void setListeners() {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}

