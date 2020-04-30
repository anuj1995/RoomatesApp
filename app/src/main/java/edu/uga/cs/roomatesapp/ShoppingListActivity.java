package edu.uga.cs.roomatesapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uga.cs.roomatesapp.models.PurchasedListItem;
import edu.uga.cs.roomatesapp.models.ShoppingListItem;
import edu.uga.cs.roomatesapp.models.UserOwe;

public class ShoppingListActivity extends AppCompatActivity implements AddItemDialog.AddNewItemListener {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<ShoppingListItem> shoppingList;
    ShoppingListAdapter shoppingListAdapter;
    FloatingActionButton fab;
    LinearLayoutManager linearLayoutManager;
    Parcelable listState;
    HashMap<Pair<String,String>,Double> checkedItems;
    FloatingActionButton fab2;
    int count = 0;
    String userName;
    DatabaseReference purchasedListRef;
    DatabaseReference owingDatabaseReference;
    ArrayList<PurchasedListItem> purchasedListItems = new ArrayList<>();
    ArrayList<String> userList = new ArrayList<>();
    DatabaseReference userListReference;
    long purchasedCount;
    long owingListCount;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);
        owingDatabaseReference = FirebaseDatabase.getInstance().getReference("Owing");
        purchasedListRef = FirebaseDatabase.getInstance().getReference("PurchasedListItems");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShoppingListItems");
        recyclerView = findViewById(R.id.my_recycler_view);
        linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        shoppingList = new ArrayList<>();
        fab = findViewById(R.id.floatingActionButton);
        fab2 = findViewById(R.id.floatingActionButton2);
        checkedItems = new HashMap<>();

        userName = getIntent().getStringExtra("User");
        userListReference = FirebaseDatabase.getInstance().getReference("Users");
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingList, new ShoppingListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(ShoppingListItem item, double price) {
                checkedItems.put(new Pair<>(item.getItemId(),item.getItemName()),price);
            }

            @Override
            public void onItemUncheck(ShoppingListItem item) {
                checkedItems.remove(new Pair<>(item.getItemId(),item.getItemName()));
            }
        });
        fab.setOnClickListener(v -> openDialog());

        fab2.setOnClickListener(v -> {
            if(checkedItems.size()==0){
                Toast.makeText(this,"No selected entries",Toast.LENGTH_SHORT).show();
            }else {
                AlertDialog alertDialog  = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Move the selection to purchased");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",(dialog, which) -> {});
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Okay",(dialog, which) -> {
                            moveToPurchased();
                        });
                alertDialog.show();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count =(int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ShoppingListItem item ;
                item = dataSnapshot.getValue(ShoppingListItem.class);
                shoppingListAdapter.addNewData(item);
                recyclerView.setAdapter(shoppingListAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                ShoppingListItem item = dataSnapshot.getValue(ShoppingListItem.class);
                shoppingListAdapter.removeData(item);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getChildren().forEach(dataSnapshot1 -> {
                    userList.add(dataSnapshot1.getValue(String.class));
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        owingDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                owingListCount = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        purchasedListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasedCount = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void sendTexts(String itemName) {
        String x = databaseReference.push().getKey();
        ShoppingListItem item = new ShoppingListItem(itemName,x);
        item.setPurchased(false);
        databaseReference.child(x).setValue(item);
    }

    private void openDialog(){
        AddItemDialog addItemDialog = new AddItemDialog();
        addItemDialog.show(getSupportFragmentManager(),"Add Item Dialog");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("checked List",checkedItems);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        checkedItems = (HashMap<Pair<String, String>, Double>) savedInstanceState.getSerializable("checked List");
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingListAdapter.setCheckedItems(checkedItems);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    /**
     * method to move the item to purchased list and remove the item form shopping list
     * Also put a charge on other roommates for the item purchased
     */
    private void moveToPurchased(){

        for(Map.Entry<Pair<String, String>, Double> itemPrice : checkedItems.entrySet()){
            //remove item form shopping list
            databaseReference.child(String.valueOf(itemPrice.getKey().first)).removeValue();

            // Add item to the purchased list
            PurchasedListItem p = new PurchasedListItem(itemPrice.getKey().second,itemPrice.getValue(),userName);
            p.setItemId((int) (purchasedCount));
            purchasedListRef.child(String.valueOf(purchasedCount)).setValue(p);
            purchasedCount++;

            // put a charge on roommates
            userList.forEach(s -> {
                if(!s.equals(userName)){
                    owingDatabaseReference.child(getUserName(userName))
                            .child(getUserName(s)).push().setValue((itemPrice.getValue()/userList.size()));
                    owingDatabaseReference.child(getUserName(s))
                            .child(getUserName(userName)).push().setValue(-(itemPrice.getValue()/userList.size()));
                }
            });

        }
        //clear the selection map
        checkedItems.clear();

    }

    /**
     *
     * @param s
     * @return a string email without "@example.com"
     */
    private String getUserName(String s){
        return s.substring(0,s.indexOf("@"));
    }


    @Override
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
