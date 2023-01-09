package com.example.villafilomena.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Frontdesk.Guest_departure_Adapter;
import com.example.villafilomena.Frontdesk.Guest_details_model;
import com.example.villafilomena.Frontdesk.Request_Adapter;
import com.example.villafilomena.R;

import java.util.ArrayList;

public class Manager_PaymentHstry_Adapter extends RecyclerView.Adapter<Manager_PaymentHstry_Adapter.ViewHolder> {
    ArrayList<Guest_details_model> guestholder;
    ClickListener clickListener;

    public Manager_PaymentHstry_Adapter(ArrayList<Guest_details_model> guestholder, ClickListener clickListener) {
        this.guestholder = guestholder;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Manager_PaymentHstry_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_paymenthstry_list, parent, false);
        return new Manager_PaymentHstry_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Manager_PaymentHstry_Adapter.ViewHolder holder, int position) {
        Guest_details_model model = guestholder.get(position);

        holder.date.setText(model.getCheckIn_date() + " - "+ model.getCheckOut_date());
        holder.amount.setText(model.getTotal_cost());
        holder.account_name.setText(model.getGuest_name());
    }

    @Override
    public int getItemCount() {
        return guestholder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, amount, account_name, invoice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.paymenthstry_date);
            amount = itemView.findViewById(R.id.paymenthstry_amount);
            account_name = itemView.findViewById(R.id.paymenthstry_acountname);
            invoice = itemView.findViewById(R.id.paymenthstry_invoice);
            invoice.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onClick(View v, int position);
    }
}
