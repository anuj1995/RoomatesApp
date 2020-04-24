package edu.uga.cs.roomatesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment loginRegisterFragment = new LoginRegisterFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.MainContainer,loginRegisterFragment).commit();
    }
}
