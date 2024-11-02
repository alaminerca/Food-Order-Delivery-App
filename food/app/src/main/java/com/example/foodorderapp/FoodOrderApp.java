package com.example.foodorderapp;

import android.app.Application;
import com.example.foodorderapp.database.repositories.MenuRepository;
import com.example.foodorderapp.database.entities.MenuItemEntity;

public class FoodOrderApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize database with sample data
        MenuRepository repository = new MenuRepository(this);

        // Add sample items
        repository.insert(new MenuItemEntity(
                "Margherita Pizza",
                "Fresh tomatoes, mozzarella, basil",
                12.99,
                "",
                "Pizza"
        ));

        repository.insert(new MenuItemEntity(
                "Chicken Burger",
                "Grilled chicken breast with lettuce and mayo",
                8.99,
                "",
                "Burger"
        ));

        repository.insert(new MenuItemEntity(
                "Caesar Salad",
                "Fresh romaine lettuce, croutons, parmesan",
                7.99,
                "",
                "Salad"
        ));
    }
}