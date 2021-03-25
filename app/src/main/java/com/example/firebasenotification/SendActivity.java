package com.example.firebasenotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendActivity extends AppCompatActivity {

    private static final String TAG = "SendActivity";

    private TextView textView;
    private EditText editText,title;
    private Button button;
    private String userId;
    private String userName;
    private String currentUser;
    private ProgressBar progressBar;
    private APIService apiService;

    private FirebaseFirestore fireStore= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        userId= getIntent().getStringExtra("user_Id");
        userName= getIntent().getStringExtra("user_name");
        textView= findViewById(R.id.idText);
        title= findViewById(R.id.notificationTitleId);
        textView.setText("Sent to "+userName);
        currentUser= FirebaseAuth.getInstance().getUid();
        editText= findViewById(R.id.notificationTextId);
        progressBar= findViewById(R.id.progressId);


        apiService=  Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressBar.setVisibility(View.INVISIBLE);
        button= findViewById(R.id.notificationButtonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value= editText.getText().toString();
                String value2= title.getText().toString();
                if(!TextUtils.isEmpty(value) && !TextUtils.isEmpty(value2) ){

                    fireStore.collection("Tokens").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.isSuccessful()){
                                DocumentSnapshot ds= task.getResult();
                                String val = ds.getString("token");

                                sentNotification(val,value,value2);
                            }
                            else
                            {
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
                else {
                    showSnackBar("Enter text");
                }
            }
        });

        updateToken();

    }

    private void sentNotification(String val, String value, String value2) {

        Data data = new Data(value, value2);
        NotificationSender sender = new NotificationSender(data, val);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code()==200){
                    if(response.body().success != 1){
                        showSnackBar("failed");
                    }
                    else {
                        showSnackBar("Success");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

                showSnackBar("Request Fail");
            }
        });


    }

    private void showSnackBar(String s) {
        View contextView = findViewById(android.R.id.content);
        Snackbar.make(contextView, s, Snackbar.LENGTH_LONG).show();
    }

    private void updateToken(){

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String value= FirebaseInstanceId.getInstance().getToken();
        Map<String,String> tokenMap= new HashMap<>();
        tokenMap.put("token",value);
        fireStore.collection("Tokens").document(firebaseUser.getUid()).set(tokenMap);

    }

}