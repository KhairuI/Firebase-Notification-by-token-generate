package com.example.firebasenotification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> userList;
    private MyAdapter adapter;


    // Firebase...
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore fireStore= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        recyclerView = findViewById(R.id.userRecycleId);
        userList= new ArrayList<>();
        adapter= new MyAdapter(userList,this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userList.clear();
        fireStore.collection("User").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value!= null){
                    for(DocumentChange doc: value.getDocumentChanges()){

                        if(doc.getType() == DocumentChange.Type.ADDED){

                            String userId= doc.getDocument().getId();

                            User user= doc.getDocument().toObject(User.class).withId(userId);
                            userList.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }
}