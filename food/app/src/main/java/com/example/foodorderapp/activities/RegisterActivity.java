package com.example.foodorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.AddressEntity;
import com.example.foodorderapp.database.entities.UserEntity;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput, emailInput, phoneInput, passwordInput;
    private EditText streetInput, cityInput, stateInput, zipInput;
    private CheckBox addHangoutCheckbox;
    private LinearLayout hangoutAddressSection;
    private Button registerButton;
    private TextView loginLink;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupListeners();

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    private void initializeViews() {
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        phoneInput = findViewById(R.id.phone_input);
        passwordInput = findViewById(R.id.password_input);
        streetInput = findViewById(R.id.street_input);
        cityInput = findViewById(R.id.city_input);
        stateInput = findViewById(R.id.state_input);
        zipInput = findViewById(R.id.zip_input);
        addHangoutCheckbox = findViewById(R.id.add_hangout_checkbox);
        hangoutAddressSection = findViewById(R.id.hangout_address_section);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);
    }

    private void setupListeners() {
        addHangoutCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hangoutAddressSection.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        registerButton.setOnClickListener(v -> attemptRegistration());

        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        // Get user input
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user
        UserEntity user = new UserEntity(name, email, password, phone, "CUSTOMER");

        // Create address
        AddressEntity address = new AddressEntity(
                0, // userId will be set after user creation
                streetInput.getText().toString(),
                cityInput.getText().toString(),
                stateInput.getText().toString(),
                zipInput.getText().toString(),
                "REGULAR",
                true // set as default address
        );

        // Register user
        viewModel.register(user, address, new RegisterViewModel.RegistrationCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this,
                        "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RegisterActivity.this,
                        "Registration failed: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}