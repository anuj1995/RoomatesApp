package edu.uga.cs.roomatesapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.roomatesapp.models.UserOwe;

public class OwingListAdapter extends RecyclerView.Adapter<OwingListAdapter.OwingListViewHolder> {
    private List<UserOwe> userOweList;
    private Context context;

    public OwingListAdapter(Context context,ArrayList<UserOwe> userOweList){
        this.userOweList = userOweList;
        this.context = context;
    }

    @NonNull
    @Override
    public OwingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OwingListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.cardview_owings,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OwingListViewHolder holder, int position) {
        if(!userOweList.isEmpty()){
            holder.userName.setText(userOweList.get(position).getUsername());
            if(userOweList.get(position).getAmount() < 0){
                holder.oweAmountLayout.setHint("You owe");
                holder.owe.setText(new DecimalFormat("##.##").format(Math.abs(userOweList.get(position).getAmount())));
            }else if(userOweList.get(position).getAmount() > 0){
                holder.owe.setText(new DecimalFormat("##.##").format((Math.abs(userOweList.get(position).getAmount()))));
                holder.oweAmountLayout.setHint("Owes you");
            }
            else{
                holder.owe.setText("");
                holder.oweAmountLayout.setHint("Settled");
            }

        }
    }

    @Override
    public int getItemCount() {
        return userOweList.size();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateData(UserOwe userOwe){
        boolean contains = false;
        String user="";
        if(userOwe.getUsername().contains("@"))user = getUserName(userOwe.getUsername());
        else user = userOwe.getUsername();

        for(UserOwe userOwe1:userOweList){
            if(getUserName(userOwe1.getUsername()).equals(user)){
                contains = true;
                userOwe1.setAmount(userOwe.getAmount());
                notifyDataSetChanged();
                break;
            }
        }
        if(!contains){
            userOweList.add(userOwe);
            notifyDataSetChanged();
        }
    }

    /**
     *
     * @param userOwe
     * adds the new data item to the list and notifies the adapter
     */
    public void addNewData(UserOwe userOwe){
        userOweList.add(userOwe);
        notifyDataSetChanged();
    }

    class OwingListViewHolder extends RecyclerView.ViewHolder{
        TextInputLayout oweAmountLayout;
        EditText userName;
        EditText owe;
        public OwingListViewHolder(@NonNull View itemView) {
            super(itemView);
            oweAmountLayout = itemView.findViewById(R.id.oweAmountLayout);
            userName = itemView.findViewById(R.id.userName);
            owe = itemView.findViewById(R.id.owe);
        }
    }


    /**
     *
     * @param s
     * @return a string email without @example.com
     */
    private String getUserName(String s){
        return s.substring(0,s.indexOf("@"));
    }
}
