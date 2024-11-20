package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foodorderapp.database.entities.AddressEntity;
import java.util.List;

@Dao
public interface AddressDAO {
    @Insert
    long insert(AddressEntity address);

    @Update
    void update(AddressEntity address);

    @Query("SELECT * FROM addresses WHERE userId = :userId")
    LiveData<List<AddressEntity>> getAddressesByUserId(int userId);

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    LiveData<AddressEntity> getDefaultAddress(int userId);

    @Query("SELECT * FROM addresses WHERE userId = :userId AND addressType = :type")
    LiveData<List<AddressEntity>> getAddressesByType(int userId, String type);
}