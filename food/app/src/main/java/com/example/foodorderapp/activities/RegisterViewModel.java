package com.example.foodorderapp.activities;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.foodorderapp.database.entities.AddressEntity;
import com.example.foodorderapp.database.entities.UserEntity;
import com.example.foodorderapp.database.repositories.UserRepository;
import com.example.foodorderapp.database.repositories.AddressRepository;

public class RegisterViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private AddressRepository addressRepository;

    public RegisterViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        addressRepository = new AddressRepository(application);
    }

    public void register(UserEntity user, AddressEntity address, RegistrationCallback callback) {
        userRepository.register(user, new UserRepository.OnRegistrationCallback() {
            @Override
            public void onSuccess(int userId) {
                // Set the userId for the address
                address.setUserId(userId);

                // Save the address
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