package com.example.villafilomena.Guest.home_booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CottageInfos_adapter extends RecyclerView.Adapter<CottageInfos_adapter.ViewHolder> {
    ArrayList<CottageInfos_model> cottageinfo_holder;

    public CottageInfos_adapter(ArrayList<CottageInfos_model> cottageinfo_holder) {
        this.cottageinfo_holder = cottageinfo_holder;
    }

    @NonNull
    @Override
    public CottageInfos_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_booking_roominfo, parent, false);
        return new CottageInfos_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CottageInfos_adapter.ViewHolder holder, int position) {
        final CottageInfos_model model = cottageinfo_holder.get(position);

        if(holder.RoomImage.getDrawable() == null){
            holder.room_Progress.setVisibility(View.VISIBLE);
        }else{
            holder.room_Progress.setVisibility(View.GONE);
        }
        holder.RoomName.setText(model.getName());
        holder.RoomFee.setText(model.getCottage_rate());
        holder.RoomCapacity.setText(model.getCottage_capacity());
        Picasso.get().load(model.getImageUrl()).into(holder.RoomImage);

        holder.RoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.room_Check.getVisibility() == View.GONE){
                    holder.room_Check.setVisibility(View.VISIBLE);
                }else {
                    holder.room_Check.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cottageinfo_holder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView room_Check;
        ProgressBar room_Progress;
        ImageView RoomImage;
        TextView RoomName, RoomCapacity, RoomFee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_Check = itemView.findViewById(R.id.roomInfo_Check);
            room_Progress = itemView.findViewById(R.id.roomInfo_Progess);
            RoomImage = itemView.findViewById(R.id.roomInfo_RoomImage);
            RoomName = itemView.findViewById(R.id.roomInfo_RoomName);
            RoomCapacity = itemView.findViewById(R.id.roomInfo_RoomCapacity);
            RoomFee = itemView.findViewById(R.id.roomInfo_RoomFee);
        }
    }
}
