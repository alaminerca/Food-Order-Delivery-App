package com.example.foodorderapp.activities;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.foodorderapp.database.entities.AddressEntity;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import com.example.foodorderapp.database.entities.UserEntity;
import com.example.foodorderapp.database.repositories.DeliveryAgentRepository;
import com.example.foodorderapp.database.repositories.UserRepository;
import com.example.foodorderapp.database.repositories.AddressRepository;


public class RegisterViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private DeliveryAgentRepository deliveryAgentRepository;

    public RegisterViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        addressRepository = new AddressRepository(application);
        deliveryAgentRepository = new DeliveryAgentRepository(application);
    }

    public void register(UserEntity user, DeliveryAgentEntity agent, AddressEntity address,
                         RegistrationCallback callback) {
        userRepository.register(user, new UserRepository.OnRegistrationCallback() {
            @Override
            public void onSuccess(int userId) {
                if (agent != null) {
                    // Register delivery agent
                    deliveryAgentRepository.registerAgent(agent, new DeliveryAgentRepository.OnAgentCallback() {
                        @Override
                        public void onSuccess(int agentId) {
                            callback.onSuccess();
                        }

                        @Override
                        public void onError(String message) {
                            callback.onError(message);
                        }
                    });
                } else if (address != null) {
                    // Save customer address
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