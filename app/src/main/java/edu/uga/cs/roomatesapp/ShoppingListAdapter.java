package edu.uga.cs.roomatesapp;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uga.cs.roomatesapp.models.ShoppingListItem;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private ArrayList<ShoppingListItem> shoppingList;
    private Context context;
    private HashMap<Pair<String,String>,Double> checkedItemsMap;


    public interface OnItemCheckListener {
        void onItemCheck(ShoppingListItem item,double price);
        void onItemUncheck(ShoppingListItem item);
    }

    @NonNull
    private OnItemCheckListener onItemCheckListener;
    public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> shoppingList,OnItemCheckListener onItemCheckListener){
        this.context =context;
        this.shoppingList = shoppingList;
        this.onItemCheckListener = onItemCheckListener;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.cardview_shoppinglist,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        if(!shoppingList.isEmpty()){
            holder.name.setText(shoppingList.get(position).getItemName());}

        // restore state of the selection
        if(checkedItemsMap!=null
            && checkedItemsMap.size()!=0
            && checkedItemsMap.get(new Pair(shoppingList.get(position).getItemId(),shoppingList.get(position).getItemName()))!= null){
            holder.price.setText(String.valueOf(
                    checkedItemsMap.get(
                            new Pair(shoppingList.get(position).getItemId(),shoppingList.get(position).getItemName()))));
            holder.purchased.setChecked(true);
        }

        // implement onCheckListener for items
        holder.purchased.setOnClickListener(v -> {
            if(holder.purchased.isChecked()){
                if(holder.price.getText().toString().isEmpty()){
                    Toast.makeText(context,"Please enter the price",Toast.LENGTH_SHORT).show();
                    holder.purchased.setChecked(false);
                }
                else onItemCheckListener.onItemCheck(shoppingList.get(position)
                        , Double.parseDouble(holder.price.getText().toString()));
            }
            else{
                onItemCheckListener.onItemUncheck(shoppingList.get(position));
            }
        });
    }

    void setCheckedItems(HashMap<Pair<String,String>,Double> checkedItems){
        checkedItemsMap = checkedItems;
    }
    void addNewData(ShoppingListItem item){
        shoppingList.add(item);
        notifyDataSetChanged();
    }

   void removeData(ShoppingListItem item){
        int i=0;
        for(ShoppingListItem it: shoppingList ){
            if(it.getItemName().equals(item.getItemName())){
                shoppingList.remove(i);
                break;
            }
            i++;
        }
        notifyDataSetChanged();
   }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    class ShoppingListViewHolder extends RecyclerView.ViewHolder{

        EditText name;
        CheckBox purchased;
        EditText price;
        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            purchased = itemView.findViewById(R.id.purchased);
            price = itemView.findViewById(R.id.price);
        }


    }
}
