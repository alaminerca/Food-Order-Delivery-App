package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.MenuAdapter;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.example.foodorderapp.database.entities.CartItemEntity;
import com.example.foodorderapp.database.repositories.CartRepository;
import java.util.ArrayList;

public class MenuFragment extends Fragment implements MenuAdapter.OnItemClickListener {
    private MenuViewModel viewModel;
    private MenuAdapter adapter;
    private RecyclerView recyclerView;
    private CartRepository cartRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        cartRepository = new CartRepository(requireActivity().getApplication());

        recyclerView = view.findViewById(R.id.menu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MenuAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        // Observe menu items
        viewModel.getAllMenuItems().observe(getViewLifecycleOwner(), menuItems -> {
            if (menuItems != null) {
                adapter.updateItems(menuItems);
            }
        });

        return view;
    }

    @Override
    public void onAddToCartClick(MenuItemEntity menuItem) {
        CartItemEntity cartItem = new CartItemEntity(
                menuItem.getId(),
                1,
                menuItem.getPrice(),
                menuItem.getName()
        );

        cartRepository.insert(cartItem);
        Toast.makeText(getContext(), menuItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }
}