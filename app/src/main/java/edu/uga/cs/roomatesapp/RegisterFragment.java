package edu.uga.cs.roomatesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmnet_register,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText id = view.findViewById(R.id.userId);
        final EditText pw = view.findViewById(R.id.password);
        final EditText rePw = view.findViewById(R.id.reTypePassword);
        Button register = view.findViewById(R.id.textButton);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final TextInputLayout pwLayout = view.findViewById(R.id.passwordLayout);
        final TextInputLayout rePwLayout = view.findViewById(R.id.reTypePasswordLayout);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = id.getText().toString();
                String password = pw.getText().toString() ;
                String rePassword = rePw.getText().toString();
                if(password.matches(rePassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "registered", Toast.LENGTH_SHORT).show();
                            } else {
                                {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else {
                    rePwLayout.setError("The passwords do not match");
                    pwLayout.setError("The passwords do not match");
                }
            }
        });
    }
}
