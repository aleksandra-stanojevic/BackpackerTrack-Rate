package com.example.aleksandra.backpack.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.activities.EventActivity;
import com.example.aleksandra.backpack.Models.PersonModel;
import com.example.aleksandra.backpack.Models.PlaceModel;
import com.example.aleksandra.backpack.utils.Utils;

import java.util.List;

public class ProfileNewPlaceAdapter extends RecyclerView.Adapter<ProfileNewPlaceAdapter.ViewHolder> {
    private List<PlaceModel> list;
    private Context context;

    public ProfileNewPlaceAdapter(List<PlaceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ProfileNewPlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProfileNewPlaceAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_profile_new_place, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileNewPlaceAdapter.ViewHolder holder, int position) {
        holder.comment.setText(list.get(position).getComment());
        holder.title.setText(list.get(position).getPlaceName());
        holder.detail.setText(list.get(position).getPlaceState());
        holder.locationAdress.setText(list.get(position).getPlaceLocation());
        holder.locationPhone.setText(list.get(position).getPlacePhone());
        holder.timePassed.setText(list.get(position).getTimePassed());
        Utils.loadImageWithGlide(Uri.parse("https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg"), holder.locationImage);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title, detail, comment, locationAdress, locationPhone, timePassed;
        private ImageView locationImage;

        private ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tw_profile_title);
            detail = itemView.findViewById(R.id.tw_profile_detail);
            comment = itemView.findViewById(R.id.tw_profile_comment);
            locationAdress = itemView.findViewById(R.id.tw_profile_location_address);
            locationPhone = itemView.findViewById(R.id.tw_profile_phone);
            locationImage = itemView.findViewById(R.id.iv_profile_location_image);
            timePassed = itemView.findViewById(R.id.tw_profile_time_passed);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(BackpackApplication.getAppContext(), EventActivity.class);
            context.startActivity(i);
        }
    }
}


