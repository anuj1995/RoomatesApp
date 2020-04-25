package edu.uga.cs.roomatesapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import edu.uga.cs.roomatesapp.R;

public class AddItemDialog extends DialogFragment {


    private EditText editText;
    private AddNewItemListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog,null);
        editText = view.findViewById(R.id.newItem);
        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("cancel", (dialog, which) -> {

                }).setPositiveButton("Ok",(dialog,which)->{
                    String itemName = editText.getText().toString();
                    listener.sendTexts(itemName);
                });
        return builder.create();
    }

    public interface AddNewItemListener{
        void sendTexts(String itemName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (AddNewItemListener)context;

    }
}
