package com.example.foodorderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapters.ManageMenuAdapter;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

public class ManageMenuFragment extends Fragment {
    private ManageMenuViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ManageMenuAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_menu, container, false);

        recyclerView = view.findViewById(R.id.menu_items_recycler);
        fabAdd = view.findViewById(R.id.fab_add_menu_item);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(ManageMenuViewModel.class);

        adapter = new ManageMenuAdapter(
                this::showEditDialog,
                this::showDeleteConfirmation
        );
        recyclerView.setAdapter(adapter);

        viewModel.getAllMenuItems().observe(getViewLifecycleOwner(), adapter::submitList);

        fabAdd.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void showAddDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_menu_item, null);
        EditText nameInput = dialogView.findViewById(R.id.item_name_input);
        EditText descriptionInput = dialogView.findViewById(R.id.item_description_input);
        EditText priceInput = dialogView.findViewById(R.id.item_price_input);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Menu Item")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String description = descriptionInput.getText().toString().trim();
                    String priceStr = priceInput.getText().toString().trim();

                    if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double price = Double.parseDouble(priceStr);
                        viewModel.addMenuItem(name, description, price);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(MenuItemEntity item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_menu_item, null);
        EditText nameInput = dialogView.findViewById(R.id.item_name_input);
        EditText descriptionInput = dialogView.findViewById(R.id.item_description_input);
        EditText priceInput = dialogView.findViewById(R.id.item_price_input);

        nameInput.setText(item.getName());
        descriptionInput.setText(item.getDescription());
        priceInput.setText(String.valueOf(item.getPrice()));

        new AlertDialog.Builder(requireContext())
                .setTitle("Edit Menu Item")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String description = descriptionInput.getText().toString().trim();
                    String priceStr = priceInput.getText().toString().trim();

                    if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double price = Double.parseDouble(priceStr);
                        item.setName(name);
                        item.setDescription(description);
                        item.setPrice(price);
                        viewModel.updateMenuItem(item);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmation(MenuItemEntity item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Menu Item")
                .setMessage("Are you sure you want to delete " + item.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteMenuItem(item))
                .setNegativeButton("Cancel", null)
                .show();
    }
}