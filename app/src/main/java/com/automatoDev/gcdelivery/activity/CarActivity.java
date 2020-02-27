package com.automatoDev.gcdelivery.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.adapter.CarAdapter;
import com.automatoDev.gcdelivery.firebase.FirebaseOper;
import com.automatoDev.gcdelivery.provider.CarProvider;
import com.automatoDev.gcdelivery.provider.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CarActivity extends AppCompatActivity {

    private CarProvider carProvider = new CarProvider();
    private FirebaseOper firebaseOper;
    private FirebaseAuth firebaseAuth;
    private UserProvider userProvider;
    private FirebaseFirestore fireStore;
    private List<CarProvider> carProviders;
    private CarAdapter carAdapter;

    public ProgressBar progress;
    public ProgressBar progress1;
    private RecyclerView recyclerView_car;
    private TextView txt_totalPayment_car;
    private TextView txt_qtd_order;
    private TextView txt_info_itens;

    private String typePayment;
    private String uid;
    private double totalOrders = 0;
    public static boolean status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();

        recyclerView_car = findViewById(R.id.recycler_car);
        txt_totalPayment_car = findViewById(R.id.txt_totalPayment_car);
        txt_qtd_order = findViewById(R.id.txt_qtd_order);
        progress = findViewById(R.id.progress);
        progress1 = findViewById(R.id.progress1);
        txt_info_itens = findViewById(R.id.txt_info_itens);

        firebaseOper = new FirebaseOper(this);

        fireGetOrders();
        showData();
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

    public void showData() {
        recyclerView_car.hasFixedSize();
        recyclerView_car.setLayoutManager(new LinearLayoutManager(this));
        carProviders = new ArrayList<>();
        carAdapter = new CarAdapter(carProviders, this);
        recyclerView_car.setAdapter(carAdapter);
    }

    private void fireGetOrders() {
        final Locale locale = new Locale("pt", "BR");
        final NumberFormat nb = NumberFormat.getCurrencyInstance(locale);
        fireStore.collection("users").document(uid)
                .collection("orders")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        carProviders.clear();
                        totalOrders = 0;
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            carProvider = doc.toObject(CarProvider.class);
                            totalOrders += carProvider.getTotalOrder();
                            carProviders.add(carProvider);
                        }
                        txt_qtd_order.setText(String.valueOf(carProviders.size()));
                        txt_totalPayment_car.setText(nb.format(totalOrders));
                        carAdapter.notifyDataSetChanged();

                        if (carProviders.size() == 0)
                            txt_info_itens.setText("Nada aqui :D");

                        progress.setVisibility(View.GONE);
                        progress1.setVisibility(View.GONE);

                    }
                });
    }

    public void clearOrders(View view) {
        if (carProviders.size() == 0) {
            Toast.makeText(this, "Nada aqui :D", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Aviso!");
        alert.setMessage("Limpar seu carrinho?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int witch) {
                firebaseOper.delOrders(uid);
            }
        });
        alert.setNegativeButton("Voltar", null);
        alert.show();

    }

    public void buyFinish(View view) {
        if (carProviders.size() == 0){
            Toast.makeText(this, "Adicione um item no carrinho \n antes de finalizar :D", Toast.LENGTH_LONG).show();
            return;
        }
        final BottomSheetDialog paymentDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View payment = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_payment, null);
        Button btn_endOrder_payment = payment.findViewById(R.id.btn_endOrder_payment);
        final RadioButton rd_card = payment.findViewById(R.id.rd_card);
        final RadioButton rd_cash = payment.findViewById(R.id.rd_cash);

        final View progress = getLayoutInflater().inflate(R.layout.layout_add_car, null);
        TextView txt_label = progress.findViewById(R.id.txt_label);
        txt_label.setText("Criando seu pedido...\nAguarde");
        final AlertDialog alerta = new AlertDialog.Builder(this).create();
        alerta.setCancelable(false);
        alerta.setView(progress);

        final String orderUid = UUID.randomUUID().toString();

        btn_endOrder_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rd_card.isChecked())
                    typePayment = rd_card.getText().toString();
                else
                    typePayment = rd_cash.getText().toString();

                alerta.show();
                paymentDialog.dismiss();
                fireStore.collection("users").document(uid)
                        .get().addOnCompleteListener(CarActivity.this, new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            userProvider = doc.toObject(UserProvider.class);
                            userProvider.setQtdItens(carProviders.size());
                            userProvider.setStatus("Pendente");
                            userProvider.setTypePayement(typePayment);
                            userProvider.setTotalPayment(totalOrders);
                            //userProvider.setTimestamp(FieldValue.serverTimestamp());

                            fireStore.collection("userAdmin").document("admin")
                                    .collection("pedidos")
                                    .document(uid)
                                    .set(userProvider)
                                    .addOnCompleteListener(CarActivity.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            fireStore.collection("userAdmin").document("admin")
                                                    .collection("pedidos").document(uid).update("timestamp", FieldValue.serverTimestamp())
                                                    .addOnCompleteListener(CarActivity.this, new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                for (int i = 0; i < carProviders.size(); i++) {
                                                                    fireStore.collection("userAdmin").document("admin")
                                                                            .collection("pedidos")
                                                                            .document(uid)
                                                                            .collection(orderUid)
                                                                            .document(UUID.randomUUID().toString()).set(carProviders.get(i));
                                                                }
                                                            }
                                                        }
                                                    });

                                            alerta.dismiss();
                                            final AlertDialog.Builder alert = new AlertDialog.Builder(CarActivity.this);
                                            alert.setTitle("Tudo certo!");
                                            alert.setMessage("Seu pedido foi criado com sucesso!\nFique de prontidão para quando ele chegar :D");
                                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    firebaseOper.delOrders(uid);
                                                    finish();
                                                }
                                            }).show();


                                        }
                                    });
                        }

                    }
                });

            }

        });
        paymentDialog.setContentView(payment);
        paymentDialog.show();
    }
}
