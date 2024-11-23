package com.example.foodorderapp;

import android.app.Application;
import com.example.foodorderapp.database.repositories.MenuRepository;
import com.example.foodorderapp.database.repositories.DeliveryAgentRepository;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;

public class FoodOrderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize database with sample data
        MenuRepository menuRepository = new MenuRepository(this);
        DeliveryAgentRepository agentRepository = new DeliveryAgentRepository(this);

        // Add sample menu items
//        menuRepository.insert(new MenuItemEntity(
//                "Margherita Pizza",
//                "Fresh tomatoes, mozzarella, basil",
//                12.99,
//                "",
//                "Pizza"
//        ));
//
//        menuRepository.insert(new MenuItemEntity(
//                "Chicken Burger",
//                "Grilled chicken breast with lettuce and mayo",
//                8.99,
//                "",
//                "Burger"
//        ));

        // Add sample delivery agents
//        DeliveryAgentEntity agent1 = new DeliveryAgentEntity("John Delivery", "john@delivery.com", "123-456-7890");
//        DeliveryAgentEntity agent2 = new DeliveryAgentEntity("Mike Rider", "mike@delivery.com", "098-765-4321");

//        agentRepository.registerAgent(agent1, new DeliveryAgentRepository.OnAgentCallback() {
//            @Override
//            public void onSuccess(int agentId) {}
//            @Override
//            public void onError(String message) {}
//        });

//        agentRepository.registerAgent(agent2, new DeliveryAgentRepository.OnAgentCallback() {
//            @Override
//            public void onSuccess(int agentId) {}
//            @Override
//            public void onError(String message) {}
//        });
    }
}