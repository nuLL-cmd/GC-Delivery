package com.automatoDev.gcdelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;

import java.util.List;

public class FidelityAdapter extends RecyclerView.Adapter<FidelityAdapter.DataHandler> {
    private List<String> lists;

    public FidelityAdapter(List<String> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override

    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_celula_fidelity, parent, false);
        return new DataHandler(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        String data = lists.get(position);
        holder.txt_data_fidelity_celula.setText(data);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        private   TextView txt_data_fidelity_celula;
        public DataHandler(@NonNull View itemView) {
            super(itemView);
           txt_data_fidelity_celula = itemView.findViewById(R.id.txt_data_fidelity_celula);
        }
    }
}
