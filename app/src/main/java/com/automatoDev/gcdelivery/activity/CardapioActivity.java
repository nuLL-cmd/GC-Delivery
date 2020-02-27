package com.automatoDev.gcdelivery.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.adapter.DishAdapter;
import com.automatoDev.gcdelivery.provider.DishProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity {
    private List<DishProvider> dishProviderList;
    private DishAdapter dishAdapter;
    private FirebaseFirestore fireStore;

    public static boolean status;

    private RecyclerView recycler_cardapio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        recycler_cardapio = findViewById(R.id.recycler_cardapio);

        fireStore = FirebaseFirestore.getInstance();

        showData();
        onClick();
    }

    @Override
    protected void onStop() {
        super.onStop();
        status = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        fireGetDish();
    }

    private void showData() {
        dishProviderList = new ArrayList<>();
        recycler_cardapio.hasFixedSize();
        recycler_cardapio.setLayoutManager(new LinearLayoutManager(this));
        dishAdapter = new DishAdapter(dishProviderList, this);
        recycler_cardapio.setAdapter(dishAdapter);
    }

    private void fireGetDish() {
        fireStore.collection("userAdmin").document("cardapio").collection("pratos")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        dishProviderList.clear();
                        assert queryDocumentSnapshots != null;
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            dishProviderList.add(doc.toObject(DishProvider.class));
                        }

                        dishAdapter.notifyDataSetChanged();

                    }
                });
    }

    public void onClick() {
        dishAdapter.setOnItemClickListener(new DishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                dishAdapter.notifyItemChanged(position);
                DishProvider dishProvider = dishProviderList.get(position);
                Intent intent = new Intent(CardapioActivity.this, DetailsActivity.class);
                intent.putExtra("dish", dishProvider);
                startActivity(intent);

            }
        });
    }
}