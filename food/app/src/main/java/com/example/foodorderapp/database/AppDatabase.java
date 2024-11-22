package com.example.foodorderapp.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.foodorderapp.database.DAO.*;
import com.example.foodorderapp.database.entities.*;

@Database(entities = {
        MenuItemEntity.class,
        CartItemEntity.class,
        OrderEntity.class,
        OrderItemEntity.class,
        UserEntity.class,
        AddressEntity.class
}, version = 2)  // Increased version number from 1 to 2
public abstract class AppDatabase extends RoomDatabase {
    public abstract MenuItemDAO menuItemDAO();
    public abstract CartDAO cartDAO();
    public abstract OrderDAO orderDAO();
    public abstract UserDAO userDAO();
    public abstract AddressDAO addressDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "food_order_db")
                            .fallbackToDestructiveMigration()  // This will delete the database and recreate it
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}