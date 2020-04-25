package edu.uga.cs.roomatesapp;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uga.cs.roomatesapp.models.ShoppingListItem;

public class ShoppingListActivity extends AppCompatActivity implements AddItemDialog.AddNewItemListener {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<ShoppingListItem> shoppingList;
    ShoppingListAdapter shoppingListAdapter;
    FloatingActionButton fab;
    LinearLayoutManager linearLayoutManager;
    Parcelable listState;
    HashMap<Integer,Double> checkedItems;
    FloatingActionButton fab2;
    int count = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("ShoppingListItems");
        recyclerView = findViewById(R.id.my_recycler_view);
        linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        shoppingList = new ArrayList<>();
        fab = findViewById(R.id.floatingActionButton);
        fab2 = findViewById(R.id.floatingActionButton2);
        checkedItems = new HashMap<>();
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingList, (item, price) -> {
          checkedItems.put(item.getItemId(),price);
        });
        fab.setOnClickListener(v -> openDialog());

        fab2.setOnClickListener(v -> {
            AlertDialog alertDialog  = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Move th selection to purchased");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",(dialog, which) -> {});
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Okay",(dialog, which) -> {
                        moveToPurchased();
                    });
            alertDialog.show();
        });


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ShoppingListItem item =null;
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    item = dataSnapshot1.getValue(ShoppingListItem.class);
                    if (item != null) {
                        item.setItemId(count);
                        count++;
                    }
                }
                shoppingListAdapter.addNewData(item);
                recyclerView.setAdapter(shoppingListAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ShoppingListItem item = dataSnapshot1.getValue(ShoppingListItem.class);
                    shoppingListAdapter.removeData(item);
                    recyclerView.setAdapter(shoppingListAdapter);
                    count--;
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void sendTexts(String itemName) {
        ShoppingListItem item = new ShoppingListItem(itemName,count+1);
        item.setPurchased(false);
        databaseReference.child(String.valueOf(count)).push().setValue(item);
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
        checkedItems = (HashMap<Integer, Double>) savedInstanceState.getSerializable("checked List");
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingListAdapter.setCheckedItems(checkedItems);
    }

    private void moveToPurchased(){
        for(Map.Entry<Integer, Double> itemPrice : checkedItems.entrySet()){
            databaseReference.child(String.valueOf(itemPrice.getKey())).removeValue();
        }


    }
}
