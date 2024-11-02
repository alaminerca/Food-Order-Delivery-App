package com.example.foodorderapp.database.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.foodorderapp.database.AppDatabase;
import com.example.foodorderapp.database.DAO.MenuItemDAO;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuRepository {
    private MenuItemDAO menuItemDAO; // Changed from menuItemDao to menuItemDAO
    private LiveData<List<MenuItemEntity>> allMenuItems;
    private ExecutorService executorService;

    public MenuRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        menuItemDAO = database.menuItemDAO(); // Changed to match the new method name
        allMenuItems = menuItemDAO.getAllMenuItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<MenuItemEntity>> getAllMenuItems() {
        return allMenuItems;
    }

    public void insert(MenuItemEntity menuItem) {
        executorService.execute(() -> menuItemDAO.insert(menuItem));
    }

    public void update(MenuItemEntity menuItem) {
        executorService.execute(() -> menuItemDAO.update(menuItem));
    }

    public void delete(MenuItemEntity menuItem) {
        executorService.execute(() -> menuItemDAO.delete(menuItem));
    }
}