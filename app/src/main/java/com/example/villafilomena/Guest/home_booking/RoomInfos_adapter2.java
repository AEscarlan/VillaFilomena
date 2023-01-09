package com.example.villafilomena.Guest.home_booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.R;
import com.squareup.picasso.Picasso;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;

public class RoomInfos_adapter2 extends RecyclerView.Adapter<RoomInfos_adapter2.ViewHolder> {
    ArrayList<RoomInfos_model> roominfo_holder;
    int count = 0;

    public RoomInfos_adapter2(ArrayList<RoomInfos_model> roominfo_holder) {
        this.roominfo_holder = roominfo_holder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_booking2_roominfo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomInfos_adapter2.ViewHolder holder, int position) {
        final RoomInfos_model model = roominfo_holder.get(position);

        Picasso.get().load(model.getImageUrl()).into(holder.roomImage);
        holder.RoomName.setText(model.getName());
        holder.RoomCapacity.setText(model.getRoom_capacity());
        holder.RoomRate.setText(model.getRoom_rate());
    }

    @Override
    public int getItemCount() {
        return roominfo_holder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView RoomName, RoomCapacity, RoomRate;
        ImageView roomImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            roomImage = itemView.findViewById(R.id.roomInfo2_RoomImage);
            RoomName = itemView.findViewById(R.id.roomInfo2_Roomname);
            RoomCapacity = itemView.findViewById(R.id.roomInfo2_Roomcapacity);
            RoomRate = itemView.findViewById(R.id.roomInfo2_Roomrate);
        }
    }
}
