package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foodorderapp.database.entities.CartItemEntity;
import java.util.List;

@Dao
public interface CartDAO {
    @Insert
    void insert(CartItemEntity item);

    @Update
    void update(CartItemEntity item);

    @Delete
    void delete(CartItemEntity item);

    @Query("SELECT * FROM cart_items")
    LiveData<List<CartItemEntity>> getAllCartItems();

    @Query("SELECT * FROM cart_items WHERE menuItemId = :menuItemId")
    LiveData<CartItemEntity> getCartItemByMenuItemId(int menuItemId);

    @Query("SELECT SUM(quantity * pricePerItem) FROM cart_items")
    LiveData<Double> getCartTotal();

    @Query("SELECT SUM(quantity) FROM cart_items")
    LiveData<Integer> getCartItemCount();

    @Query("DELETE FROM cart_items")
    void clearCart();
}