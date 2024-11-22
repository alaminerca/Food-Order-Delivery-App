package com.example.foodorderapp.fragments;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.example.foodorderapp.database.repositories.MenuRepository;
import java.util.List;

public class MenuViewModel extends AndroidViewModel {
    private static final String TAG = "MenuViewModel";
    private MenuRepository repository;
    private LiveData<List<MenuItemEntity>> allMenuItems;

    public MenuViewModel(Application application) {
        super(application);
        repository = new MenuRepository(application);
        allMenuItems = repository.getAllMenuItems();
    }

    public LiveData<List<MenuItemEntity>> getAllMenuItems() {
        return allMenuItems;
    }

    public void addMenuItem(MenuItemEntity menuItem) {
        Log.d(TAG, "Adding menu item: " + menuItem.getName());
        repository.insert(menuItem);
    }

    public void updateMenuItem(MenuItemEntity menuItem) {
        repository.update(menuItem);
    }

    public void deleteMenuItem(MenuItemEntity menuItem) {
        repository.delete(menuItem);
    }
}