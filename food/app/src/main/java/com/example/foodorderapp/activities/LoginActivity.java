package com.example.foodorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.UserEntity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView registerLink;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Set click listeners
        loginButton.setOnClickListener(v -> attemptLogin());
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.login(email, password).observe(this, user -> {
            if (user != null) {
                handleLoginSuccess(user);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(UserEntity user) {
        Log.d(TAG, "Login success - User ID: " + user.getId() + ", Role: " + user.getRole());
        Intent intent;

        switch (user.getRole()) {
            case "ADMIN":
                intent = new Intent(this, AdminActivity.class);
                break;
            case "DELIVERY":
                intent = new Intent(this, DeliveryActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }

        startActivity(intent);
        finish();
    }
}