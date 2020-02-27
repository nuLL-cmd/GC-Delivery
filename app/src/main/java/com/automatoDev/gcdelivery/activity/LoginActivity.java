package com.automatoDev.gcdelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.firebase.FirebaseOper;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_email_login;
    private EditText edt_passsword_login;
    private FirebaseOper firebaseOper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseOper = new FirebaseOper(this);
        firebaseOper.nextActivityMain();

        edt_email_login = findViewById(R.id.edt_email_login);
        edt_passsword_login = findViewById(R.id.edt_password_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void activityRegister(View view) {
        if (!RegisterActivity.status) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void loginMethodClick(View view) {
        String userEmail = edt_email_login.getText().toString();
        String userPassword = edt_passsword_login.getText().toString();

        if (userEmail.trim().isEmpty() || userPassword.trim().isEmpty()) {
            Toast.makeText(this, "Campos nao podem ser vazios!", Toast.LENGTH_SHORT).show();
        } else
            firebaseOper.fireLogin(userEmail, userPassword);
    }


}
