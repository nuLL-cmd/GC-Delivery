package com.automatoDev.gcdelivery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.provider.CarProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.DataHandler>{
    List<CarProvider> carProviderList;
    Activity context;

    public CarAdapter(List<CarProvider> carProviderList, Activity context) {
        this.carProviderList = carProviderList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orders,parent, false);
        return new DataHandler(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        Locale locale = new Locale("pt","BR");
        NumberFormat nb = NumberFormat.getCurrencyInstance(locale);

        holder.txt_ingredienteOrder_layout.setText(carProviderList.get(position).getDishDescOne());
        holder.txt_itemOrder_layout.setText(carProviderList.get(position).getDishName());
        holder.txt_priceOrder_layout.setText(nb.format(carProviderList.get(position).getTotalOrder()));
        holder.txt_qtdOrder_layout.setText(String.valueOf(carProviderList.get(position).getQtdOrder()));
        Glide.with(context).load(carProviderList.get(position).getDishUrlPhoto())
                .transition(DrawableTransitionOptions.withCrossFade()).into(holder.img_dishOrder_layout);
    }

    @Override
    public int getItemCount() {
        return carProviderList.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        private TextView txt_itemOrder_layout;
        private TextView txt_priceOrder_layout;
        private TextView txt_ingredienteOrder_layout;
        private TextView txt_qtdOrder_layout;
        private ImageView img_dishOrder_layout;

        public DataHandler(@NonNull View itemView) {
            super(itemView);

            txt_itemOrder_layout = itemView.findViewById(R.id.txt_itemOrder_layout);
            txt_priceOrder_layout = itemView.findViewById(R.id.txt_priceOrder_layout);
            txt_ingredienteOrder_layout = itemView.findViewById(R.id.txt_ingredienteOrder_layout);
            txt_qtdOrder_layout = itemView.findViewById(R.id.txt_qtdOrder_layout);
            img_dishOrder_layout = itemView.findViewById(R.id.img_dishOrder_layout);
        }
    }
}
