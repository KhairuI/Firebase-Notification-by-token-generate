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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, name, pass;
    private Button button;
    private TextView textView;

    // Firebase...
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore fireStore= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email= findViewById(R.id.registerEmailId);
        name= findViewById(R.id.registerNameId);
        pass= findViewById(R.id.registerPasswordId);
        textView= findViewById(R.id.loginTextId);
        textView.setOnClickListener(this);
        button= findViewById(R.id.registerButtonId);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.registerButtonId){
            if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(email.getText().toString())
                    && !TextUtils.isEmpty(pass.getText().toString())){
                register(name.getText().toString(),email.getText().toString(),pass.getText().toString());
            }
            else {
                showSnackBar("Please fill all field");
            }
        }
        else if(view.getId()==R.id.loginTextId){
            Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private void register(String name, String email, String pass) {
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Map<String,String> userMap= new HashMap<>();
                    userMap.put("name",name);

                    String uId= firebaseAuth.getCurrentUser().getUid();
                    fireStore.collection("User").document(uId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showSnackBar("Register Successfully");
                            goToMainActivity();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showSnackBar(e.toString());
                        }
                    });

                }
                else {
                    showSnackBar(task.getException().toString());
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar(e.toString());
            }
        });


    }

    private void goToMainActivity() {
        Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showSnackBar(String s) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, s, Snackbar.LENGTH_LONG).show();
    }
}