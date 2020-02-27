package com.automatoDev.gcdelivery.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.firebase.FirebaseOper;
import com.automatoDev.gcdelivery.provider.CarProvider;
import com.automatoDev.gcdelivery.provider.DishProvider;
import com.automatoDev.gcdelivery.provider.UserProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    public static boolean status;
    private DishProvider dishProvider;
    private CarProvider carProvider;
    private FirebaseFirestore fireStore;
    private FirebaseOper firebaseOper;
    private CarProvider carProviderGet;

    private ImageView img_cardapio_details;
    private TextView txt_name_dish;
    private TextView txt_desc_one;
    private TextView txt_desc_two;
    private TextView txt_desc_tree;
    private TextView txt_desc_four;
    private TextView txt_desc_five;
    private TextView txt_payment_details;
    private TextView txt_total_details;

    private double cash;
    private short qtd = 1;
    private boolean qtd_itens_server = false;
    private String uid;
    private NumberFormat format;
    private Locale locale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        img_cardapio_details = findViewById(R.id.img_cardaptio_details);
        txt_name_dish = findViewById(R.id.txt_name_dish);
        txt_desc_one = findViewById(R.id.txt_desc_one);
        txt_desc_two = findViewById(R.id.txt_desc_two);
        txt_desc_tree = findViewById(R.id.txt_desc_tree);
        txt_desc_four = findViewById(R.id.txt_desc_four);
        txt_desc_five = findViewById(R.id.txt_desc_five);
        txt_payment_details = findViewById(R.id.txt_payment_details);
        txt_total_details = findViewById(R.id.txt_total_details);

        locale = new Locale("pt", "BR");
        format = NumberFormat.getCurrencyInstance(locale);

        getData();
        uid = FirebaseAuth.getInstance().getUid();
        fireStore = FirebaseFirestore.getInstance();
        firebaseOper = new FirebaseOper(this);


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

    public void getData() {
        dishProvider = getIntent().getParcelableExtra("dish");
        if (dishProvider != null) {
            txt_name_dish.setText(dishProvider.getDishName());
            txt_desc_one.setText(dishProvider.getDishDescOne());
            txt_desc_two.setText(dishProvider.getDishDescTwo());
            txt_desc_tree.setText(dishProvider.getDishDescTree());
            txt_desc_four.setText(dishProvider.getDishDescFour());
            txt_desc_five.setText(dishProvider.getDishDescFive());
            cash = dishProvider.getDishValue();
            txt_payment_details.setText(format.format(cash));
            txt_total_details.setText(String.valueOf(qtd));

            Glide.with(this).load(dishProvider.getDishUrlPhoto()).transition(DrawableTransitionOptions.withCrossFade())
                    .into(img_cardapio_details);


        }
    }

    public void addClick(View view) {
        qtd++;
        txt_total_details.setText(String.valueOf(qtd));
        cash += dishProvider.getDishValue();
        txt_payment_details.setText(format.format(cash));

    }

    public void removeClick(View view) {
        if (qtd > 1) {
            qtd--;
            txt_total_details.setText(String.valueOf(qtd));
            cash -= dishProvider.getDishValue();
            txt_payment_details.setText(format.format(cash));

        }
    }

    public void addCarOrder() {
        carProvider = new CarProvider(dishProvider.getDishUid(), dishProvider.getDishName(),
                dishProvider.getDishDescOne(), (dishProvider.getDishValue() * qtd), qtd, dishProvider.getDishUrlPhoto());
        firebaseOper.fireSaveOrders(carProvider, FirebaseAuth.getInstance().getUid());
    }

    public void addCarButton(View view) {
        View v = getLayoutInflater().inflate(R.layout.layout_add_car,null);
        TextView txt_label = v.findViewById(R.id.txt_label);
        txt_label.setText("Aguarde...");

        final AlertDialog alerta = new AlertDialog.Builder(this).create();
        alerta.setView(v);
        alerta.setCancelable(false);
        alerta.show();
        fireStore.collection("userAdmin").document("admin").collection("pedidos")
                .get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        if (doc.exists()) {
                            UserProvider userProvider = doc.toObject(UserProvider.class);
                            if (userProvider.getUserUid().equals(uid)){
                                qtd_itens_server = true;
                                break;
                            }

                        }

                    }
                    if (qtd_itens_server){
                        alerta.dismiss();
                        AlertDialog.Builder alert = new AlertDialog.Builder(DetailsActivity.this);
                        alert.setTitle("Ops!");
                        alert.setMessage("Já existe um pedido em andamento!\n Favor aguardar a conclusão do pedido para inciar um outro :D");
                        alert.setPositiveButton("Entendi",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int witch){
                                finish();
                            }

                        });
                        alert.show();
                    }else{
                        addCarOrder();
                        alerta.dismiss();
                    }

                }

            }

        });
    }
}
