package com.example.foodorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.foodorderapp.R;
import com.example.foodorderapp.fragments.ManageMenuFragment;
import com.example.foodorderapp.fragments.ManageOrdersFragment;
import com.example.foodorderapp.fragments.ManageUsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private static final String TAG = "AdminActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_admin);

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_navigation);
        Log.d(TAG, "Setting up BottomNavigationView");

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            Log.d(TAG, "Navigation item selected: " + itemId);

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
            Log.d(TAG, "Setting default fragment");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, new ManageMenuFragment())
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // Clear any stored preferences
            getSharedPreferences("delivery_prefs", MODE_PRIVATE).edit().clear().apply();
            // Return to login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_help) {
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Help")
                .setMessage("Contact support at:\nEmail: support@foodorder.com\nPhone: +1234567890")
                .setPositiveButton("OK", null)
                .show();
    }
}