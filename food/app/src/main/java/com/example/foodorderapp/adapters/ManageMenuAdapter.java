package com.example.foodorderapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.MenuItemEntity;
import java.util.Locale;

public class ManageMenuAdapter extends ListAdapter<MenuItemEntity, ManageMenuAdapter.MenuItemViewHolder> {
    private final OnItemClickListener editListener;
    private final OnItemClickListener deleteListener;

    public interface OnItemClickListener {
        void onItemClick(MenuItemEntity item);
    }

    public ManageMenuAdapter(OnItemClickListener editListener, OnItemClickListener deleteListener) {
        super(DIFF_CALLBACK);
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    private static final DiffUtil.ItemCallback<MenuItemEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<MenuItemEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull MenuItemEntity oldItem, @NonNull MenuItemEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MenuItemEntity oldItem, @NonNull MenuItemEntity newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPrice() == newItem.getPrice();
        }
    };

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_menu, parent, false);
        return new MenuItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItemEntity currentItem = getItem(position);
        holder.bind(currentItem);
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;
        private ImageButton editButton;
        private ImageButton deleteButton;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.menu_item_name);
            descriptionTextView = itemView.findViewById(R.id.menu_item_description);
            priceTextView = itemView.findViewById(R.id.menu_item_price);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(MenuItemEntity item) {
            nameTextView.setText(item.getName());
            descriptionTextView.setText(item.getDescription());
            priceTextView.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));

            editButton.setOnClickListener(v -> editListener.onItemClick(item));
            deleteButton.setOnClickListener(v -> deleteListener.onItemClick(item));
        }
    }
}