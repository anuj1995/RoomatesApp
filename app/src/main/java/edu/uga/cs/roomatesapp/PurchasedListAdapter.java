package edu.uga.cs.roomatesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uga.cs.roomatesapp.models.PurchasedListItem;

public class PurchasedListAdapter extends RecyclerView.Adapter<PurchasedListAdapter.PurchasedListViewHolder> {
    private ArrayList<PurchasedListItem> purchasedList;
    private Context context;

    public PurchasedListAdapter(Context context, ArrayList<PurchasedListItem> purchasedListItem) {
        this.context =context;
        this.purchasedList = purchasedListItem;

    }

    @NonNull
    @Override
    public PurchasedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PurchasedListAdapter.PurchasedListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.cardview_purchasedlist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedListViewHolder holder, int position) {
        if(!purchasedList.isEmpty()){
            holder.itemName.setText(purchasedList.get(position).getItemName());
            holder.purchasedBy.setText(purchasedList.get(position).getPurchasedBy());
            holder.price.setText(String.valueOf( purchasedList.get(position).getPrice()));
        }

    }

    /**
     *
     * @param item
     * adds the new data item to the list and notifies the adapter
     */

    public void addNewData(PurchasedListItem item){
        purchasedList.add(item);
        notifyDataSetChanged();
    }

    public void removeData(PurchasedListItem item){
        int i=0;
        for(PurchasedListItem it: purchasedList ){
            if(it.getItemName().equals(item.getItemName())){
                purchasedList.remove(i);
                break;
            }
            i++;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return purchasedList.size();
    }

    class PurchasedListViewHolder extends RecyclerView.ViewHolder{

        EditText itemName;
        EditText price;
        EditText purchasedBy;
        public PurchasedListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            purchasedBy = itemView.findViewById(R.id.purchasedBy);
            price = itemView.findViewById(R.id.price);
        }


    }
}
