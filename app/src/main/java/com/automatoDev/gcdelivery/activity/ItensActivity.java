package com.automatoDev.gcdelivery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.adapter.ItensAdapter;
import com.automatoDev.gcdelivery.provider.ItensProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItensActivity extends AppCompatActivity {

    public static boolean status;

    private RecyclerView recycler_pedido;
    private ProgressBar progressBar2;
    private ProgressBar progressBar1;
    private TextView txt_data_pedido;
    private TextView txt_situacao_pedido;
    private TextView txt_valor_pedido;
    private TextView txt_data_sit_pedido;
    private TextView txt_sit_valor_pedido;
    private TextView txt_sit_status_pedido;

    private RelativeLayout relative_status_order;

    private FirebaseFirestore firestore;
    private List<ItensProvider> itensProviderList;
    private ItensProvider itemdoing;
    private ItensProvider itensProvider;
    private ItensAdapter adapter;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);

        recycler_pedido = findViewById(R.id.recycler_pedido);
        progressBar2 = findViewById(R.id.progressBar2);
        txt_data_pedido = findViewById(R.id.txt_data_pedido);
        txt_situacao_pedido = findViewById(R.id.txt_situacao_pedido);
        txt_valor_pedido = findViewById(R.id.txt_valor_pedido);
        relative_status_order = findViewById(R.id.relative_status_order);
        progressBar1 = findViewById(R.id.progressBar1);
        txt_data_sit_pedido = findViewById(R.id.txt_data_sit_pedido);
        txt_sit_valor_pedido = findViewById(R.id.txt_sit_valor_pedido);
        txt_sit_status_pedido = findViewById(R.id.txt_sit_status_pedido);

        firestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getUid();

        fireGetItens();
        showData();
        fireGetAtualOrders();
        onClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;


    }

    @Override
    protected void onStop() {
        super.onStop();
        status = false;
    }

    private void fireGetItens() {
        itensProviderList = new ArrayList<>();
        firestore.collection("users").document(uid)
                .collection("pedidos").orderBy("timestamp")
                .get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                itensProviderList.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        itensProvider = doc.toObject(ItensProvider.class);
                        itensProviderList.add(itensProvider);
                    }
                }
                adapter.notifyDataSetChanged();


                if (itensProviderList.size() != 0) {
                    txt_data_pedido.setText(convertTimeStamp(itensProviderList.get(0).getTimestamp()));
                    txt_situacao_pedido.setText(itensProviderList.get(0).getStatus());
                    txt_valor_pedido.setText(convertDecimalFormat(itensProviderList.get(0).getTotalPayment()));
                }
                progressBar2.setVisibility(View.GONE);
            }
        });
    }

    public void showData() {
        recycler_pedido.hasFixedSize();
        recycler_pedido.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItensAdapter(itensProviderList);
        recycler_pedido.setAdapter(adapter);
    }


    public void onClick() {
        adapter.setOnItemClickListener(new ItensAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                adapter.notifyItemChanged(position);
            }
        });
    }

    public void fireGetAtualOrders() {
        firestore.collection("userAdmin").document("admin").collection("pedidos")
                .document(uid)
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot.exists()) {
                            itemdoing = documentSnapshot.toObject(ItensProvider.class);
                            txt_sit_status_pedido.setText(itemdoing.getStatus());
                            txt_data_sit_pedido.setText(convertTimeStamp(itemdoing.getTimestamp()));
                            txt_sit_valor_pedido.setText(convertDecimalFormat(itemdoing.getTotalPayment()));

                        } else
                            relative_status_order.setVisibility(View.VISIBLE);

                        progressBar1.setVisibility(View.GONE);

                    }
                });
    }

    public String convertTimeStamp(Timestamp timestamp) {

        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat sp = new SimpleDateFormat("EEE, d MMM yyyy", locale);

        return sp.format(timestamp.toDate());
    }

    public String convertDecimalFormat(double value) {
        Locale locale = new Locale("pt", "BR");
        NumberFormat nb = NumberFormat.getCurrencyInstance(locale);

        return nb.format(value);

    }
}
