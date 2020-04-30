package edu.uga.cs.roomatesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import edu.uga.cs.roomatesapp.models.PurchasedListItem;
import edu.uga.cs.roomatesapp.models.UserOwe;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button shoppingList;
    Button purchasedLsit;
    Button settleUp;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference archivedReference;
    DatabaseReference purchasedListReference;
    DatabaseReference userListReference;
    OwingListAdapter owingListAdapter;
    ArrayList<UserOwe> userOweList;
    DatabaseReference owingListReference;
    DatabaseReference allOwingReference;
    ArrayList<String> users = new ArrayList<>();
    HashMap<String,Double> owingMap = new HashMap<>();
    ArrayList<PurchasedListItem> purchasedListItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init views and database references
        archivedReference = FirebaseDatabase.getInstance().getReference().child("Archive");
        purchasedListReference = FirebaseDatabase.getInstance().getReference().child("PurchasedListItems");
        userListReference = FirebaseDatabase.getInstance().getReference().child("Users");
        shoppingList = findViewById(R.id.viewShoppingList);
        settleUp = findViewById(R.id.settleUp);
        tv = findViewById(R.id.UserName);
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        purchasedLsit = findViewById(R.id.viewPurchasedList);
        recyclerView.setLayoutManager(linearLayoutManager);
        userOweList = new ArrayList<>();
        owingListAdapter = new OwingListAdapter(this,userOweList);
        allOwingReference = FirebaseDatabase.getInstance().getReference().child("Owing");
        //recyclerView.setAdapter(owingListAdapter);
        String userName = getIntent().getStringExtra("UserName");
        tv.setText( userName);
        owingListReference =FirebaseDatabase.getInstance().getReference().child("Owing")
                .child(getUserName(userName));


        // listeners to buttons
        shoppingList.setOnClickListener(v -> {
            Intent i = new Intent(this,ShoppingListActivity.class);
            i.putExtra("User",userName);
            startActivity(i);
        });
        purchasedLsit.setOnClickListener(v -> {
            Intent i = new Intent(this,PurchasedListActivity.class);
            i.putExtra("UserName",userName);
            startActivity(i);
        });
        settleUp.setOnClickListener(v -> {
            ArrayList<PurchasedListItem> purchasedListItems = new ArrayList<>();

            // Move the purchased list to archived
            purchasedListReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    archivedReference.removeValue();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            purchasedListItems.add(dataSnapshot1.getValue(PurchasedListItem.class));

                    }
                    archivedReference.child(userName.substring(0,userName.indexOf("@"))).setValue(purchasedListItems);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            purchasedListReference.removeValue();
            allOwingReference.removeValue();

        });


        // user added handled
        userListReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(!users.contains(dataSnapshot.getValue(String.class)))
                    users.add(dataSnapshot.getValue(String.class));
                if(!userName.equals(dataSnapshot.getValue(String.class))){
                    UserOwe userOwe = new UserOwe(dataSnapshot.getValue(String.class));
                    owingListAdapter.updateData(userOwe);
                    recyclerView.setAdapter(owingListAdapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // display the updated owing
        owingListReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<Double> userOwesList = new ArrayList<>();
                boolean contains = false;
                for (String user : users) {
                    if (getUserName(user).matches(dataSnapshot.getKey())){
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        userOwesList.add(dataSnapshot2.getValue(Double.class));
                    }
                    UserOwe userOwe = new UserOwe(dataSnapshot.getKey());
                    userOwe.setAmount(userOwesList.stream().mapToDouble(i -> i).sum());
                    owingListAdapter.updateData(userOwe);
                    recyclerView.setAdapter(owingListAdapter);
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArrayList<Double> userOwesList = new ArrayList<>();
                boolean contains = false;
                for (String user : users) {
                    if (getUserName(user).matches(dataSnapshot.getKey())){
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        userOwesList.add(dataSnapshot2.getValue(Double.class));
                    }
                    UserOwe userOwe = new UserOwe(dataSnapshot.getKey());
                    userOwe.setAmount(userOwesList.stream().mapToDouble(i -> i).sum());
                    owingListAdapter.updateData(userOwe);
                    recyclerView.setAdapter(owingListAdapter);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                users.forEach(s -> {
                    if (!s.matches(userName)){
                        UserOwe userOwe = new UserOwe(s);
                        owingListAdapter.updateData(userOwe);
                        recyclerView.setAdapter(owingListAdapter);
                    }
                });
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /**
     *
     * @param s
     * @return a string email with out @example.com
     */
    private String getUserName(String s){
        return s.substring(0,s.indexOf("@"));
    }
}
