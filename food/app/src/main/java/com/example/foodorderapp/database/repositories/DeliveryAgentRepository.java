package com.example.foodorderapp.database.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.DeliveryAgentDAO;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeliveryAgentRepository {
    private static final String TAG = "DeliveryAgentRepository";
    private final DeliveryAgentDAO deliveryAgentDAO;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public DeliveryAgentRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        deliveryAgentDAO = database.deliveryAgentDAO();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void registerAgent(DeliveryAgentEntity agent, OnAgentCallback callback) {
        executorService.execute(() -> {
            try {
                // Check if agent already exists
                DeliveryAgentEntity existingAgent = deliveryAgentDAO.getAgentByEmail(agent.getEmail());
                if (existingAgent != null) {
                    mainHandler.post(() -> callback.onError("Email already registered"));
                    return;
                }

                // Set initial availability to true
                agent.setAvailable(true);
                long id = deliveryAgentDAO.insert(agent);
                mainHandler.post(() -> callback.onSuccess((int) id));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public void updateAgentAvailability(int agentId, boolean isAvailable, OnAgentCallback callback) {
        Log.d(TAG, "Updating agent " + agentId + " availability to: " + isAvailable);
        executorService.execute(() -> {
            try {
                deliveryAgentDAO.updateAgentAvailability(agentId, isAvailable);
                Log.d(TAG, "Successfully updated agent availability");
                mainHandler.post(() -> callback.onSuccess(agentId));
            } catch (Exception e) {
                Log.e(TAG, "Error updating agent availability: " + e.getMessage());
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public LiveData<List<DeliveryAgentEntity>> getAvailableAgents() {
        return deliveryAgentDAO.getAvailableAgents();
    }

    public void getAgentByEmail(String email, OnAgentCallback callback) {
        executorService.execute(() -> {
            try {
                DeliveryAgentEntity agent = deliveryAgentDAO.getAgentByEmail(email);
                if (agent != null) {
                    mainHandler.post(() -> callback.onSuccess(agent.getId()));
                } else {
                    mainHandler.post(() -> callback.onError("Agent not found"));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public interface OnAgentCallback {
        void onSuccess(int agentId);
        void onError(String message);
    }
}