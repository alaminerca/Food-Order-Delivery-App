package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.MenuAdapter;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import java.util.ArrayList;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemClickListener {
    private static final String TAG = "MenuFragment";
    private MenuViewModel menuViewModel;
    private MenuAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MenuAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called");

        menuViewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        menuViewModel.getAllMenuItems().observe(getViewLifecycleOwner(), menuItems -> {
            Log.d(TAG, "Received menu items: " + (menuItems != null ? menuItems.size() : 0));
            adapter.updateItems(menuItems);
        });
    }

    @Override
    public void onAddToCartClick(MenuItemEntity item) {
        Log.d(TAG, "Add to cart clicked for item: " + item.getName());
        menuViewModel.addToCart(item);
        Toast.makeText(getContext(),
                item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }
}