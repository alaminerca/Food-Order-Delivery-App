package com.example.foodorderapp.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.foodorderapp.database.entities.DeliveryAgentEntity;
import java.util.List;

@Dao
public interface DeliveryAgentDAO {
    @Insert
    long insert(DeliveryAgentEntity agent);

    @Update
    void update(DeliveryAgentEntity agent);

    @Query("SELECT * FROM delivery_agents WHERE isAvailable = 1")
    LiveData<List<DeliveryAgentEntity>> getAvailableAgents();

    @Query("SELECT * FROM delivery_agents WHERE id = :agentId")
    LiveData<DeliveryAgentEntity> getAgentById(int agentId);

    @Query("UPDATE delivery_agents SET isAvailable = :isAvailable WHERE id = :agentId")
    void updateAgentAvailability(int agentId, boolean isAvailable);

    @Query("SELECT * FROM delivery_agents WHERE email = :email")
    DeliveryAgentEntity getAgentByEmail(String email);
}