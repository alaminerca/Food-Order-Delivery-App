package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import java.util.List;

@Dao
public interface MenuItemDAO {
    @Insert
    void insert(MenuItemEntity menuItem);

    @Update
    void update(MenuItemEntity menuItem);

    @Delete
    void delete(MenuItemEntity menuItem);

    @Query("SELECT * FROM menu_items")
    LiveData<List<MenuItemEntity>> getAllMenuItems();

    @Query("SELECT * FROM menu_items WHERE category = :category")
    LiveData<List<MenuItemEntity>> getMenuItemsByCategory(String category);

    @Query("SELECT * FROM menu_items WHERE id = :id")
    LiveData<MenuItemEntity> getMenuItem(int id);
}