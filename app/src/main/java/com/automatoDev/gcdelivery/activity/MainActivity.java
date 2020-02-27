package com.automatoDev.gcdelivery.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.firebase.FirebaseOper;
import com.automatoDev.gcdelivery.provider.UserProvider;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static boolean status;
    private FirebaseOper firebaseOper;
    private Uri uri;
    private String uid;

    private TextView txt_name_main;
    private TextView txt_email_main;
    private TextView txt_user_main;
    private TextView txt_phone_main;
    private CircleImageView img_perfil_main;
    private FirebaseFirestore fireStore;
    private ProgressBar progress2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_perfil_main = findViewById(R.id.img_perfil_main);
        txt_email_main = findViewById(R.id.txt_email_main);
        txt_name_main = findViewById(R.id.txt_name_main);
        txt_user_main = findViewById(R.id.txt_user_main);
        txt_phone_main = findViewById(R.id.txt_phone_main);
        progress2 = findViewById(R.id.progress2);


        firebaseOper = new FirebaseOper(this);
        fireStore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getUid();

        fireGetUser();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void logoutMethod(View view){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Não me deixe!!");
        alerta.setMessage("Tem certeza que deseja sair?\nVocê pode voltar quando quiser!!");
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseOper.fireLogout();
            }
        }).setNegativeButton("Naão :)", null);
        alerta.show();


    }

    public void nextActivityCardaptio(View view){
        if (!CardapioActivity.status){
            Intent intent = new Intent(this, CardapioActivity.class);
            startActivity(intent);
        }
    }

    public void nextActivityMyItens(View view){
        if (!ItensActivity.status){
            Intent intent = new Intent(this, ItensActivity.class);
            startActivity(intent);
        }
    }

    public void nextActivityFidelity(View view){
        if (!FidelityActivity.status){
            Intent intent = new Intent(this, FidelityActivity.class);
            startActivity(intent);
        }
    }

    public void nextActivityContact(View view){
        if (!ContactActivity.status){
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }
    }
    public void nextActivityProfile(View view){
        if (!ProfileActivity.status){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }
    public void nextActivityCar(View view){
        if (!CarActivity.status){
            Intent intent = new Intent(this, CarActivity.class);
            startActivity(intent);
        }
    }

    public void setPicUser(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            if (data != null){
                uri = data.getData();
                Glide.with(MainActivity.this).load(uri).into(img_perfil_main);
                firebaseOper.fireUpdatePic(uid,uri);
            }
        }
    }
    public void fireGetUser() {

        fireStore.collection("users").document(uid).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                if (documentSnapshot.exists()){
                    UserProvider userProvider = documentSnapshot.toObject(UserProvider.class);
                    assert userProvider != null;
                    txt_email_main.setText(userProvider.getUserEmail());
                    txt_user_main.setText(userProvider.getUserUser());
                    txt_name_main.setText(userProvider.getUserName());
                    txt_phone_main.setText(formatPhone(userProvider.getUserPhone()));
                    Glide.with(MainActivity.this).load(userProvider.getUserUrlPhoto()).placeholder(R.drawable.person2).
                            into(img_perfil_main);
                    try {
                        Thread.sleep(200);

                    }catch(Exception error){
                        error.printStackTrace();
                    }
                    progress2.setVisibility(View.GONE);

                }
            }
        });

    }
    private String formatPhone(String phone) {
        phone = phone.substring(0, 5) + "-" + phone.substring(5, 9);
        return phone;
    }

}
