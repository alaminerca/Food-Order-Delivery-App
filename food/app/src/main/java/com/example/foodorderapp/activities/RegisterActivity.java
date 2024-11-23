package com.example.foodorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.AddressEntity;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import com.example.foodorderapp.database.entities.UserEntity;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput, emailInput, phoneInput, passwordInput;
    private EditText streetInput, cityInput, stateInput, zipInput;
    private RadioGroup roleGroup;
    private LinearLayout addressSection;
    private Button registerButton;
    private TextView loginLink;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initializeViews();
        setupListeners();
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
        roleGroup = findViewById(R.id.role_group);
        addressSection = findViewById(R.id.address_section);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);
    }

    private void setupListeners() {
        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Show/hide address section based on role
            if (checkedId == R.id.role_customer) {
                addressSection.setVisibility(View.VISIBLE);
            } else {
                addressSection.setVisibility(View.GONE);
            }
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
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine role
        String role;
        int checkedId = roleGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.role_admin) {
            role = "ADMIN";
        } else if (checkedId == R.id.role_delivery) {
            role = "DELIVERY";
        } else {
            role = "CUSTOMER";
        }

        // Create user
        UserEntity user = new UserEntity(name, email, password, phone, role);

        // Create DeliveryAgent if role is DELIVERY
        DeliveryAgentEntity deliveryAgent = null;
        if (role.equals("DELIVERY")) {
            deliveryAgent = new DeliveryAgentEntity(name, email, phone);
        }

        // Create address for customers only
        AddressEntity address = null;
        if (role.equals("CUSTOMER")) {
            String street = streetInput.getText().toString().trim();
            String city = cityInput.getText().toString().trim();
            String state = stateInput.getText().toString().trim();
            String zip = zipInput.getText().toString().trim();

            if (street.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()) {
                Toast.makeText(this, "Please fill all address fields", Toast.LENGTH_SHORT).show();
                return;
            }

            address = new AddressEntity(
                    0,
                    street,
                    city,
                    state,
                    zip,
                    "REGULAR",
                    true
            );
        }

        // Register user
        final DeliveryAgentEntity finalDeliveryAgent = deliveryAgent;
        final AddressEntity finalAddress = address;
        viewModel.register(user, finalDeliveryAgent, finalAddress, new RegisterViewModel.RegistrationCallback() {
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