package com.example.foodorderapp.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.UserDAO;
import com.example.foodorderapp.database.entities.UserEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDAO userDAO;
    private ExecutorService executorService;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDAO = database.userDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<UserEntity> login(String email, String password) {
        return userDAO.login(email, password);
    }

    public LiveData<UserEntity> getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public void register(UserEntity user, OnRegistrationCallback callback) {
        executorService.execute(() -> {
            try {
                long userId = userDAO.insert(user);
                if (userId > 0) {
                    callback.onSuccess((int) userId);
                } else {
                    callback.onError("Registration failed");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void update(UserEntity user, OnUpdateCallback callback) {
        executorService.execute(() -> {
            try {
                userDAO.update(user);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public interface OnRegistrationCallback {
        void onSuccess(int userId);
        void onError(String message);
    }

    public interface OnUpdateCallback {
        void onSuccess();
        void onError(String message);
    }
}