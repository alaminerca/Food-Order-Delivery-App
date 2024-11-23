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
        AddressEntity.class,
        DeliveryAgentEntity.class
}, version = 4)  // Increased version for deliveryAgentId type change
public abstract class AppDatabase extends RoomDatabase {
    public abstract MenuItemDAO menuItemDAO();
    public abstract CartDAO cartDAO();
    public abstract OrderDAO orderDAO();
    public abstract UserDAO userDAO();
    public abstract AddressDAO addressDAO();
    public abstract DeliveryAgentDAO deliveryAgentDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "food_order_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}