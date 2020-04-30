package edu.uga.cs.roomatesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.uga.cs.roomatesapp.models.PurchasedListItem;

public class PurchasedListActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<PurchasedListItem> purchasedList;
    PurchasedListAdapter purchasedListAdapter;
    LinearLayoutManager linearLayoutManager;
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasedlist);

        // init views and database references
        userName = getIntent().getStringExtra("UserName");
        databaseReference = FirebaseDatabase.getInstance().getReference("PurchasedListItems");
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        purchasedList = new ArrayList<>();
        purchasedListAdapter = new PurchasedListAdapter(this,purchasedList);


        // shopping list updating the purchased list
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PurchasedListItem item;
                item = dataSnapshot.getValue(PurchasedListItem.class);
                purchasedListAdapter.addNewData(item);
                recyclerView.setAdapter(purchasedListAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                PurchasedListItem item;
                item = dataSnapshot.getValue(PurchasedListItem.class);
                purchasedListAdapter.removeData(item);
                recyclerView.setAdapter(purchasedListAdapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // up navigation implemented
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent i = new Intent(this,MainActivity.class);
            i.putExtra("UserName",userName);
            navigateUpTo(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
