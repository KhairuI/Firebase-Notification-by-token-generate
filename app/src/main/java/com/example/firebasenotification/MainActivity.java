package com.example.firebasenotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView;
    private Button logout, allUser;

    //Firebase
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore fireStore= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView= findViewById(R.id.profileName);
        logout= findViewById(R.id.logoutButtonId);
        logout.setOnClickListener(this);
        allUser= findViewById(R.id.alUserButtonId);
        allUser.setOnClickListener(this);

        // retrieve user name...
        fireStore.collection("User").document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds= task.getResult();
                    if(ds!= null){
                        String name= ds.getString("name");
                        textView.setText("Name: "+name);
                    }

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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();
        if(currentUser==null){
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent intent= new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.logoutButtonId){
            firebaseAuth.signOut();
            goToLoginActivity();
        }
        else if(view.getId()==R.id.alUserButtonId){
            Intent intent= new Intent(MainActivity.this,AllUsers.class);
            startActivity(intent);
        }
    }

    private void showSnackBar(String s) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, s, Snackbar.LENGTH_LONG).show();
    }
}