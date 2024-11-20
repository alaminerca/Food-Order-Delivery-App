package com.example.foodorderapp.activities;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.UserEntity;
import com.example.foodorderapp.database.repositories.UserRepository;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository repository;

    public LoginViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<UserEntity> login(String email, String password) {
        return repository.login(email, password);
    }
}