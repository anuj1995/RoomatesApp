package edu.uga.cs.roomatesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginRegisterFragment extends Fragment {
    private RegisterLoginAdapter registerLoginAdapter;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login_register,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerLoginAdapter = new RegisterLoginAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(registerLoginAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if(position == 0){
                        tab.setText("Login");
                    }
                    else tab.setText("Register");
                }
        ).attach();

    }
}

class RegisterLoginAdapter extends FragmentStateAdapter {
    public RegisterLoginAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        if(position ==0) {
            //Bundle args = new Bundle();
            return new LoginFragment();
        }
        else{
            //Bundle args = new Bundle();
            return new RegisterFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

