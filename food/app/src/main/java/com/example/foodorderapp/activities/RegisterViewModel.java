package com.example.foodorderapp.activities;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import com.example.foodorderapp.database.entities.AddressEntity;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import com.example.foodorderapp.database.entities.UserEntity;
import com.example.foodorderapp.database.repositories.AddressRepository;
import com.example.foodorderapp.database.repositories.DeliveryAgentRepository;
import com.example.foodorderapp.database.repositories.UserRepository;

public class RegisterViewModel extends AndroidViewModel {
    private static final String TAG = "RegisterViewModel";
    private UserRepository userRepository;
    private DeliveryAgentRepository deliveryAgentRepository;
    private AddressRepository addressRepository;

    public RegisterViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        deliveryAgentRepository = new DeliveryAgentRepository(application);
        addressRepository = new AddressRepository(application);
    }

    public void register(UserEntity user, DeliveryAgentEntity agent, AddressEntity address,
                         RegistrationCallback callback) {
        Log.d(TAG, "Registering user with role: " + user.getRole());

        userRepository.register(user, new UserRepository.OnRegistrationCallback() {
            @Override
            public void onSuccess(int userId) {
                Log.d(TAG, "User registered with ID: " + userId);

                if (agent != null) {
                    // For delivery agents, set their ID same as user ID for consistency
                    agent.setId(userId);
                    deliveryAgentRepository.registerAgent(agent, new DeliveryAgentRepository.OnAgentCallback() {
                        @Override
                        public void onSuccess(int agentId) {
                            Log.d(TAG, "Delivery agent registered with ID: " + agentId);
                            callback.onSuccess();
                        }

                        @Override
                        public void onError(String message) {
                            Log.e(TAG, "Failed to register delivery agent: " + message);
                            callback.onError(message);
                        }
                    });
                } else if (address != null) {
                    // Handle customer address
                    address.setUserId(userId);
                    addressRepository.insert(address, new AddressRepository.OnAddressCallback() {
                        @Override
                        public void onSuccess() {
                            callback.onSuccess();
                        }

                        @Override
                        public void onError(String message) {
                            callback.onError(message);
                        }
                    });
                } else {
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public interface RegistrationCallback {
        void onSuccess();
        void onError(String message);
    }
}