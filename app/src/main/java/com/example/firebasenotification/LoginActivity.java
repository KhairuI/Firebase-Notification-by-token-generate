package com.example.firebasenotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, pass;
    private Button button;
    private TextView textView;

    // Firebase
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.loginEmailId);
        pass= findViewById(R.id.loginPasswordId);
        button= findViewById(R.id.loginButtonId);
        button.setOnClickListener(this);
        textView=findViewById(R.id.createAccountId);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.createAccountId){
            Intent intent= new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
        else if (view.getId()==R.id.loginButtonId){
            if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())){
                login(email.getText().toString(),pass.getText().toString());
            }
            else {
                View contextView = findViewById(android.R.id.content);
                Snackbar.make(contextView, "Please fill up all field", Snackbar.LENGTH_LONG).show();
            }

        }

    }

    private void login(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                showSnackBar("Login Successfully");
                goToMainActivity();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar(e.toString());
            }
        });
    }

    private void goToMainActivity() {
        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSnackBar(String s) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, s, Snackbar.LENGTH_LONG).show();
    }
}