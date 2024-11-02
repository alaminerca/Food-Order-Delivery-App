package com.example.foodorderapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.foodorderapp.database.DAO.MenuItemDAO;
import com.example.foodorderapp.database.DAO.CartDAO;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.example.foodorderapp.database.entities.CartItemEntity;

@Database(entities = {MenuItemEntity.class, CartItemEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MenuItemDAO menuItemDAO();
    public abstract CartDAO cartDAO();  // Make sure this matches the case in CartRepository

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "food_order_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}