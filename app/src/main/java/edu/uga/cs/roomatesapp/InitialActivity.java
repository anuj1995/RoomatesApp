package edu.uga.cs.roomatesapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class InitialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Fragment loginRegisterFragment = new LoginRegisterFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.loginContainer,loginRegisterFragment).commit();
    }
}
