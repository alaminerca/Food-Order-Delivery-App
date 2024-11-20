package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foodorderapp.database.entities.UserEntity;
import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    long insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    UserEntity login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();  // Add this query
}