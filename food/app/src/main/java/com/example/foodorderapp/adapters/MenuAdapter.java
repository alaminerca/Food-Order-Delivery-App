package com.example.foodorderapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<MenuItemEntity> menuItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAddToCartClick(MenuItemEntity item);  // Changed from MenuItem to MenuItemEntity
    }

    public MenuAdapter(List<MenuItemEntity> menuItems, OnItemClickListener listener) {
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItemEntity item = menuItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void updateItems(List<MenuItemEntity> items) {
        this.menuItems = items;
        notifyDataSetChanged();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodImage;
        private TextView foodName;
        private TextView foodDescription;
        private TextView foodPrice;
        private Button addToCartButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodDescription = itemView.findViewById(R.id.food_description);
            foodPrice = itemView.findViewById(R.id.food_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }

        public void bind(MenuItemEntity item, OnItemClickListener listener) {
            foodName.setText(item.getName());
            foodDescription.setText(item.getDescription());
            foodPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));

            // Load image using Glide
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.ic_menu)
                        .into(foodImage);
            } else {
                foodImage.setImageResource(R.drawable.ic_menu);
            }

            addToCartButton.setOnClickListener(v -> listener.onAddToCartClick(item));
        }
    }
}