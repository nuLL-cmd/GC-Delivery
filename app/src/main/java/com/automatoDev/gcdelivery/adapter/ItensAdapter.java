package com.automatoDev.gcdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.provider.ItensProvider;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ItensAdapter extends RecyclerView.Adapter<ItensAdapter.DataHandler>{

    private List<ItensProvider> itensProviderList;
    private SimpleDateFormat sp;
    private Locale locale;
    private NumberFormat nb;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longListener){
        this.longListener = longListener;
    }

    public ItensAdapter(List<ItensProvider> itensProviderList) {
        this.itensProviderList = itensProviderList;
        locale = new Locale("pt","BR");
        sp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",locale);
        nb = NumberFormat.getCurrencyInstance(locale);

    }

    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_celula_pedidos,parent,false);
        return new DataHandler(convertView,listener,longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        ItensProvider  itensProvider = itensProviderList.get(position);
        holder.txt_data_celula.setText(sp.format(itensProvider.getTimestamp().toDate()));
        holder.txt_price_celula.setText(nb.format(itensProvider.getTotalPayment()));
    }

    @Override
    public int getItemCount() {
        return itensProviderList.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        private TextView txt_price_celula;
        private TextView txt_data_celula;

        public DataHandler(@NonNull View itemView,final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            super(itemView);
            txt_price_celula = itemView.findViewById(R.id.txt_price_celula);
            txt_data_celula = itemView.findViewById(R.id.txt_data_celula);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            longListener.onItemLongClick(position);
                    }
                    return true;
                }
            });
        }
    }
}
