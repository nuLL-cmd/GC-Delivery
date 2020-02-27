package com.automatoDev.gcdelivery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.firebase.FirebaseOper;
import com.automatoDev.gcdelivery.provider.UserProvider;

public class RegisterActivity extends AppCompatActivity {

    public static boolean status;
    private EditText edt_cad_nome;
    private EditText edt_cad_phone;
    private EditText edt_cad_email;
    private EditText edt_cad_usuario;
    private EditText edt_cad_password;
    private EditText edt_street_register;
    private EditText edt_number_register;
    private EditText edt_cep_register;
    private EditText edt_complemento_register;
    private EditText edt_setor_register;
    private EditText edt_city_register;
    private EditText edt_estado_register;
    private FirebaseOper firebaseOper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_cad_nome = findViewById(R.id.edt_cad_nome);
        edt_cad_phone = findViewById(R.id.edt_cad_phone);
        edt_cad_email = findViewById(R.id.edt_cad_email);
        edt_cad_usuario = findViewById(R.id.edt_cad_usuario);
        edt_cad_password = findViewById(R.id.edt_cad_password);
        edt_street_register = findViewById(R.id.edt_street_reister);
        edt_number_register = findViewById(R.id.edt_number_register);
        edt_cep_register = findViewById(R.id.edt_cep_register);
        edt_complemento_register = findViewById(R.id.edt_complemento_register);
        edt_setor_register = findViewById(R.id.edt_setor_register);
        edt_city_register = findViewById(R.id.edt_city_register);
        edt_estado_register = findViewById(R.id.edt_estado_register);

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

    public void validateEdits() {
        String[] fields = new String[12];
        int count = 0;
        fields[0] = edt_cad_nome.getText().toString();
        fields[1] = edt_cad_phone.getText().toString();
        fields[2] = edt_cad_email.getText().toString();
        fields[3] = edt_cad_usuario.getText().toString();
        fields[4] = edt_cad_password.getText().toString();
        fields[5] = edt_street_register.getText().toString();
        fields[6] = edt_number_register.getText().toString();
        fields[7] = edt_cep_register.getText().toString();
        fields[8] = edt_complemento_register.getText().toString();
        fields[9] = edt_setor_register.getText().toString();
        fields[10] = edt_city_register.getText().toString();
        fields[11] = edt_estado_register.getText().toString();

        UserProvider userProvider;

        for (String f : fields) {
            if (f.trim().isEmpty())
                count++;
        }
        if (count != 0) {
            Toast.makeText(this, "Campos não podem ser vazios!", Toast.LENGTH_SHORT).show();
            count = 0;
        }
        else{
            userProvider = new UserProvider(fields[0],fields[2],fields[3],
                    fields[1],null,fields[5],fields[6],Integer.parseInt(fields[7]),
                    fields[8],fields[9],fields[10],fields[11]);
            firebaseOper.fireCreateUser(userProvider, fields[2],fields[4]);
        }


    }
    public void createUser(View view0){
        validateEdits();
    }
}
