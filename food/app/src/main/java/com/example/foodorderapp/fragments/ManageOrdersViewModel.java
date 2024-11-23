package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.database.repositories.DeliveryAgentRepository;
import com.example.foodorderapp.database.repositories.OrderRepository;
import com.example.foodorderapp.utils.OrderStatus;
import java.util.List;

public class ManageOrdersViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;
    private final DeliveryAgentRepository deliveryAgentRepository;

    public ManageOrdersViewModel(Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        deliveryAgentRepository = new DeliveryAgentRepository(application);
    }

    public LiveData<List<OrderEntity>> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public LiveData<List<DeliveryAgentEntity>> getAvailableAgents() {
        return deliveryAgentRepository.getAvailableAgents();
    }

    public void assignDeliveryAgent(int orderId, int agentId, ManageOrdersFragment.OrderUpdateCallback callback) {
        // First update agent availability
        deliveryAgentRepository.updateAgentAvailability(agentId, false, new DeliveryAgentRepository.OnAgentCallback() {
            @Override
            public void onSuccess(int agentId) {
                // Then update order status and assign agent
                orderRepository.assignDeliveryAgent(orderId, agentId, new OrderRepository.OrderCallback() {
                    @Override
                    public void onSuccess(int orderId) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(String message) {
                        // If order update fails, revert agent availability
                        deliveryAgentRepository.updateAgentAvailability(agentId, true, new DeliveryAgentRepository.OnAgentCallback() {
                            @Override
                            public void onSuccess(int id) {}
                            @Override
                            public void onError(String error) {}
                        });
                        callback.onError(message);
                    }
                });
            }

            @Override
            public void onError(String message) {
                callback.onError("Failed to update agent availability: " + message);
            }
        });
    }

    public void markDelivered(int orderId, ManageOrdersFragment.OrderUpdateCallback callback) {
        orderRepository.updateOrderStatus(orderId, OrderStatus.DELIVERED, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                // Make agent available again after delivery
                orderRepository.getOrderById(orderId, new OrderRepository.OrderCallback() {
                    @Override
                    public void onSuccess(int agentId) {
                        deliveryAgentRepository.updateAgentAvailability(agentId, true, new DeliveryAgentRepository.OnAgentCallback() {
                            @Override
                            public void onSuccess(int id) {
                                callback.onSuccess();
                            }
                            @Override
                            public void onError(String error) {
                                callback.onError("Delivered but failed to update agent status: " + error);
                            }
                        });
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

    public void updateOrderStatus(int orderId, String status, ManageOrdersFragment.OrderUpdateCallback callback) {
        orderRepository.updateOrderStatus(orderId, status, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                callback.onSuccess();
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
}