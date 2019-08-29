package com.app.komo.pertaminamanagementapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.komo.pertaminamanagementapp.Object.Order;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by macbook on 22/04/18.
 */

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.MyViewHolder> {

    private List<Order> transaksiList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tr_nama, tr_item_pembelian_3kg, tr_no_telepon, tr_total_harga, tr_item_pembelian_5kg, tr_item_pembelian_12kg;

        public MyViewHolder(View view) {
            super(view);
            tr_nama = (TextView) view.findViewById(R.id.tr_nama);
            tr_item_pembelian_3kg = (TextView) view.findViewById(R.id.tr_item_pembelian_3kg);
            tr_no_telepon = (TextView) view.findViewById(R.id.tr_no_telepon);
            tr_total_harga = (TextView) view.findViewById(R.id.tr_total_harga);
            tr_item_pembelian_5kg = (TextView) view.findViewById(R.id.tr_item_pembelian_5kg);
            tr_item_pembelian_12kg = (TextView) view.findViewById(R.id.tr_item_pembelian_12kg);
        }
    }

    public AdapterTransaksi(List<Order> transaksiList) {
        this.transaksiList = transaksiList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaksi, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = transaksiList.get(position);
        holder.tr_nama.setText(order.getCustomer().getName());
        holder.tr_no_telepon.setText(order.getCustomer().getPhone());
        holder.tr_item_pembelian_3kg.setText(""+order.getDetail().getTigakg());
        holder.tr_item_pembelian_5kg.setText(""+order.getDetail().getLimakg());
        holder.tr_item_pembelian_12kg.setText(""+order.getDetail().getDuabelaskg());
        holder.tr_total_harga.setText("Rp" + priceWithoutDecimal((double) order.getTotal()));
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public static String priceWithoutDecimal (Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
        return formatter.format(price);
    }
}
