package com.example.foodorderapp.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.AddressDAO;
import com.example.foodorderapp.database.entities.AddressEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddressRepository {
    private AddressDAO addressDAO;
    private ExecutorService executorService;

    public AddressRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        addressDAO = database.addressDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(AddressEntity address, OnAddressCallback callback) {
        executorService.execute(() -> {
            try {
                long id = addressDAO.insert(address);
                if (id > 0) {
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to save address");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public LiveData<List<AddressEntity>> getAddressesForUser(int userId) {
        return addressDAO.getAddressesByUserId(userId);
    }

    public LiveData<AddressEntity> getDefaultAddress(int userId) {
        return addressDAO.getDefaultAddress(userId);
    }

    public interface OnAddressCallback {
        void onSuccess();
        void onError(String message);
    }
}