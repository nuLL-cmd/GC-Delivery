package com.automatoDev.gcdelivery.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.adapter.FidelityAdapter;

import java.util.ArrayList;
import java.util.List;

public class FidelityActivity extends AppCompatActivity {
    public static boolean status;
    private List<String> list;
    private RecyclerView recycler_itens_card;
    private FidelityAdapter fidelityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidelity);

        createRecycler();
    }

    public void createRecycler(){
        recycler_itens_card = findViewById(R.id.recycler_itens_card);
        recycler_itens_card.hasFixedSize();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        recycler_itens_card.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        list.add("20-12-2020");
        list.add("19-12-2036");
        list.add("21-02-2020");
        list.add("25-03-2020");
        list.add("25-04-2020");
        list.add("20-12-2020");
        list.add("19-12-2036");
        list.add("21-02-2020");
        list.add("25-03-2020");
        list.add("25-04-2020");

        fidelityAdapter = new FidelityAdapter(list);
        recycler_itens_card.setAdapter(fidelityAdapter);
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
}
