package com.example.foodorderapp.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.UserDAO;
import com.example.foodorderapp.database.entities.UserEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDAO userDAO;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private static final String TAG = "UserRepository";

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDAO = database.userDAO();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public LiveData<UserEntity> login(String email, String password) {
        MutableLiveData<UserEntity> result = new MutableLiveData<>();
        executorService.execute(() -> {
            UserEntity user = userDAO.login(email, password);
            Log.d(TAG, "Login attempt for: " + email + " - " + (user != null ? "Success" : "Failed"));
            mainHandler.post(() -> result.setValue(user));
        });
        return result;
    }

    public void register(UserEntity user, OnRegistrationCallback callback) {
        executorService.execute(() -> {
            try {
                UserEntity existingUser = userDAO.getUserByEmail(user.getEmail());
                if (existingUser != null) {
                    Log.d(TAG, "Registration failed: Email exists: " + user.getEmail());
                    mainHandler.post(() -> callback.onError("Email already registered"));
                    return;
                }

                long userId = userDAO.insert(user);
                Log.d(TAG, "User registered: " + user.getEmail() + ", Role: " + user.getRole() + ", ID: " + userId);
                mainHandler.post(() -> callback.onSuccess((int) userId));

                // Log all users after registration
                logAllUsers();
            } catch (Exception e) {
                Log.e(TAG, "Registration error: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    private void logAllUsers() {
        try {
            List<UserEntity> users = userDAO.getAllUsers();
            Log.d(TAG, "Total users in database: " + users.size());
            for (UserEntity user : users) {
                Log.d(TAG, String.format("User ID: %d, Email: %s, Role: %s",
                        user.getId(), user.getEmail(), user.getRole()));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error logging users: " + e.getMessage());
        }
    }

    public interface OnRegistrationCallback {
        void onSuccess(int userId);
        void onError(String message);
    }

    public void getAllUsers() {
        executorService.execute(() -> {
            List<UserEntity> users = userDAO.getAllUsers();
            Log.d("UserRepository", "Total users in database: " + users.size());
            for (UserEntity user : users) {
                Log.d("UserRepository", String.format("User ID: %d, Email: %s, Role: %s",
                        user.getId(), user.getEmail(), user.getRole()));
            }
        });
    }


}