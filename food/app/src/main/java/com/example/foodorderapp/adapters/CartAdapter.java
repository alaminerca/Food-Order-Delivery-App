package com.example.foodorderapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.CartItemEntity;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItemEntity> cartItems;
    private CartItemClickListener listener;

    public interface CartItemClickListener {
        void onPlusClick(CartItemEntity item);
        void onMinusClick(CartItemEntity item);
        void onRemoveClick(CartItemEntity item);
    }

    public CartAdapter(List<CartItemEntity> cartItems, CartItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemEntity item = cartItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateItems(List<CartItemEntity> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemQuantity;
        private ImageButton btnPlus;
        private ImageButton btnMinus;
        private ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.cart_item_name);
            itemPrice = itemView.findViewById(R.id.cart_item_price);
            itemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            btnPlus = itemView.findViewById(R.id.btn_increase);
            btnMinus = itemView.findViewById(R.id.btn_decrease);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        public void bind(CartItemEntity item, CartItemClickListener listener) {
            itemName.setText(item.getItemName());
            itemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getTotalPrice()));
            itemQuantity.setText(String.valueOf(item.getQuantity()));

            btnPlus.setOnClickListener(v -> listener.onPlusClick(item));
            btnMinus.setOnClickListener(v -> listener.onMinusClick(item));
            btnRemove.setOnClickListener(v -> listener.onRemoveClick(item));
        }
    }
}