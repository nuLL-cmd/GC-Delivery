package com.automatoDev.gcdelivery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.firebase.FirebaseOper;
import com.automatoDev.gcdelivery.provider.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    public static boolean status;
    private String uid;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private UserProvider userProvider;
    private FirebaseOper firebaseOper;

    private ProgressBar progressBar4;
    private ProgressBar progressBar5;

    private EditText edt_name_profile;
    private EditText edt_phone_profile;
    private EditText edt_email_profile;
    private EditText edt_user_profile;
    private EditText edt_street_profile;
    private EditText edt_number_profile;
    private EditText edt_cep_profile;
    private EditText edt_complemento_profile;
    private EditText edt_bairro_profile;
    private EditText edt_city_profile;
    private EditText edt_state_profile;
    private Button btn_save_profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        firebaseOper = new FirebaseOper(this);

        edt_name_profile  = findViewById(R.id.edt_name_profile);
        edt_phone_profile  = findViewById(R.id.edt_phone_profile);
        edt_email_profile  = findViewById(R.id.edt_email_profile);
        edt_user_profile  = findViewById(R.id.edt_user_profile);
        edt_street_profile  = findViewById(R.id.edt_street_profile);
        edt_number_profile  = findViewById(R.id.edt_number_profile);
        edt_cep_profile  = findViewById(R.id.edt_cep_profile);
        edt_complemento_profile  = findViewById(R.id.edt_complemento_profile);
        edt_bairro_profile  = findViewById(R.id.edt_bairro_profile);
        edt_city_profile  = findViewById(R.id.edt_city_profile);
        edt_state_profile  = findViewById(R.id.edt_state_profile);
        btn_save_profile = findViewById(R.id.btn_save_profile);

        progressBar4 = findViewById(R.id.progressBar4);
        progressBar5 = findViewById(R.id.progressBar5);

        uid = firebaseAuth.getUid();

        fireGetUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        status= false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        status = true;
    }

    private void fireGetUser(){
        fireStore.collection("users").document(uid)
                .get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()){
                    userProvider = doc.toObject(UserProvider.class);
                    edt_name_profile.setText(userProvider.getUserName());
                    edt_phone_profile.setText(userProvider.getUserPhone());
                    edt_email_profile.setText(userProvider.getUserEmail());
                    edt_user_profile.setText(userProvider.getUserUser());
                    edt_street_profile.setText(userProvider.getUserStreet());
                    edt_number_profile.setText(userProvider.getUserNumberHome());
                    edt_cep_profile.setText(convertCep(userProvider.getUserCep()));
                    edt_complemento_profile.setText(userProvider.getUserComplement());
                    edt_bairro_profile.setText(userProvider.getUserSector());
                    edt_city_profile.setText(userProvider.getUserCity());
                    edt_state_profile.setText(userProvider.getUserState());
                }
                try{
                    Thread.sleep(200);
                }catch(Exception error){
                    error.printStackTrace();
                }
                progressBar4.setVisibility(View.GONE);
                progressBar5.setVisibility(View.GONE);
                btn_save_profile.setEnabled(true);
            }
        });
    }

    public String convertCep(int value){
        String cep = String.valueOf(value);
        return cep.substring(0,5)+"-"+cep.substring(5,8);
    }

    public void validateEdits() {
        String[] fields = new String[11];
        int count = 0;
        fields[0] = edt_name_profile.getText().toString();
        fields[1] = edt_phone_profile.getText().toString();
        fields[2] = edt_email_profile.getText().toString();
        fields[3] = edt_user_profile.getText().toString();
        fields[4] = edt_street_profile.getText().toString();
        fields[5] = edt_number_profile.getText().toString();
        fields[6] = edt_cep_profile.getText().toString();
        fields[7] = edt_complemento_profile.getText().toString();
        fields[8] = edt_bairro_profile.getText().toString();
        fields[9]= edt_city_profile.getText().toString();
        fields[10]= edt_state_profile.getText().toString();


        for (String f : fields) {
            if (f.trim().isEmpty())
                count++;
        }
        if (count != 0) {
            Toast.makeText(this, "Campos não podem ser vazios!", Toast.LENGTH_SHORT).show();
            count = 0;
        }
        else{
           UserProvider userUpdate = new UserProvider(fields[0],fields[2],fields[3],
                    fields[1],uid,userProvider.getUserUrlPhoto(),fields[4],fields[5],convertCepForInt(fields[6]),
                    fields[7],fields[8],fields[9],fields[10]);

            firebaseOper.fireUpdateUser(userUpdate,uid);
        }


    }
    public void updateUser(View view0){
       validateEdits();
    }
    public int convertCepForInt(String value){
        value = value.substring(0,5)+value.substring(6,9);
        return  Integer.parseInt(value);
    }

}
