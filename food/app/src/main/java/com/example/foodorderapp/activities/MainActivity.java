package com.example.foodorderapp.activities;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.foodorderapp.R;
import com.example.foodorderapp.fragments.MenuFragment;
import com.example.foodorderapp.fragments.CartFragment;
import com.example.foodorderapp.fragments.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        Log.d(TAG, "Setting up BottomNavigationView");

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            Log.d(TAG, "Navigation item selected: " + itemId);

            if (itemId == R.id.navigation_menu) {
                selectedFragment = new MenuFragment();
            } else if (itemId == R.id.navigation_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.navigation_orders) {
                selectedFragment = new OrdersFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            Log.d(TAG, "Setting default fragment");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MenuFragment())
                    .commit();
        }
    }
}