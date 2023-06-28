package com.example.ibookApp.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import com.example.ibookApp.R;
import com.example.ibookApp.databinding.ActivityMainBinding;
import com.example.ibookApp.fragments.FragmentEdit;
import com.example.ibookApp.fragments.FragmentHome;
import com.example.ibookApp.fragments.FragmentProfile;
import com.example.ibookApp.fragments.FragmentSearch;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());
        //initNavigation();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.FragHome:
                    replaceFragment(new FragmentHome());
                    break;
                case R.id.FragEdit:
                    replaceFragment(new FragmentEdit());
                    break;
                case R.id.FragPerson:
                    replaceFragment(new FragmentProfile());
                    break;
                case R.id.FragSearch:
                    replaceFragment(new FragmentSearch());
                    break;
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}