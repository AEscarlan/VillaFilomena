package com.example.villafilomena.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Frontdesk.Guest_details_model;
import com.example.villafilomena.R;

import java.util.ArrayList;

public class Manager_SalesReport_Adapter extends RecyclerView.Adapter<Manager_SalesReport_Adapter.ViewHolder> {
    ArrayList<Guest_details_model> guestholder;

    public Manager_SalesReport_Adapter(ArrayList<Guest_details_model> guestholder) {
        this.guestholder = guestholder;
    }

    @NonNull
    @Override
    public Manager_SalesReport_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_sales_report_list, parent, false);
        return new Manager_SalesReport_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Manager_SalesReport_Adapter.ViewHolder holder, int position) {
        Guest_details_model model = guestholder.get(position);
    }

    @Override
    public int getItemCount() {
        return guestholder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_time, room_cottage, name, bill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date_time = itemView.findViewById(R.id.salesReport_date_time);
            room_cottage = itemView.findViewById(R.id.salesReport_room_cottage);
            name = itemView.findViewById(R.id.salesReport_Name);
            bill = itemView.findViewById(R.id.salesReport_bill);
        }
    }
}
