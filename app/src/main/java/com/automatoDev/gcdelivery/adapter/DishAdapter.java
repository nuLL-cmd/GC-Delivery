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
import com.automatoDev.gcdelivery.provider.DishProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DataHandler>{
    private List<DishProvider> dishProviderList;
    private Activity context;
    public OnItemClickListener listener;
    public OnItemLongClickListener longListner;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longListner){
        this.longListner = longListner;
    }
    public DishAdapter(List<DishProvider> dishProviderList, Activity context) {
        this.dishProviderList = dishProviderList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_celula_cardapio,parent,false);
        return new DataHandler(convertView,listener, longListner);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHandler holder, int position) {
        Locale localeBR = new Locale("pt","BR");
        NumberFormat cashFormat = NumberFormat.getCurrencyInstance(localeBR);
        DishProvider dishProvider = dishProviderList.get(position);
        holder.txt_item_title_layout_celula.setText(dishProvider.getDishName());
        holder.txt_ingrediente.setText(dishProvider.getDishDescOne());
        holder.txt_price_layout_celula.setText(cashFormat.format(dishProvider.getDishValue()));

        Glide.with(context).load(dishProvider.getDishUrlPhoto()).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.img_dish_layout_celula);
    }

    @Override
    public int getItemCount() {
        return dishProviderList.size();
    }

    public class DataHandler extends RecyclerView.ViewHolder {
        private TextView txt_item_title_layout_celula;
        private TextView txt_ingrediente;
        private TextView txt_price_layout_celula;
        private ImageView img_dish_layout_celula;

        public DataHandler(@NonNull final View itemView, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            super(itemView);
            txt_item_title_layout_celula = itemView.findViewById(R.id.txt_item_title_layout_celula);
            txt_ingrediente = itemView.findViewById(R.id.txt_ingrediente);
            txt_price_layout_celula = itemView.findViewById(R.id.txt_price_layout_celula);
            img_dish_layout_celula = itemView.findViewById(R.id.img_dish_layout_celula);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
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
                            longListener.onItemClick(position);
                    }
                    return true;
                }
            });
        }
    }
}
