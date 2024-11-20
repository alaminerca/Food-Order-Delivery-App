package com.example.foodorderapp.fragments;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.example.foodorderapp.database.repositories.MenuRepository;
import java.util.List;

public class ManageMenuViewModel extends AndroidViewModel {
    private MenuRepository repository;
    private LiveData<List<MenuItemEntity>> allMenuItems;

    public ManageMenuViewModel(Application application) {
        super(application);
        repository = new MenuRepository(application);
        allMenuItems = repository.getAllMenuItems();
    }

    public LiveData<List<MenuItemEntity>> getAllMenuItems() {
        return allMenuItems;
    }

    public void addMenuItem(String name, String description, double price) {
        MenuItemEntity menuItem = new MenuItemEntity(name, description, price, "", "General");
        repository.insert(menuItem);
    }

    public void updateMenuItem(MenuItemEntity menuItem) {
        repository.update(menuItem);
    }

    public void deleteMenuItem(MenuItemEntity menuItem) {
        repository.delete(menuItem);
    }
}