package com.example.foodorderapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.foodorderapp.R;
import com.example.foodorderapp.fragments.ManageMenuFragment;
import com.example.foodorderapp.fragments.ManageOrdersFragment;
import com.example.foodorderapp.fragments.ManageUsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_manage_menu) {
                selectedFragment = new ManageMenuFragment();
            } else if (itemId == R.id.nav_manage_orders) {
                selectedFragment = new ManageOrdersFragment();
            } else if (itemId == R.id.nav_manage_users) {
                selectedFragment = new ManageUsersFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, new ManageMenuFragment())
                    .commit();
        }
    }
}