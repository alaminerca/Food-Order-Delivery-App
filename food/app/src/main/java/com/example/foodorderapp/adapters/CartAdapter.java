package com.example.foodorderapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private final CartItemClickListener listener;

    public interface CartItemClickListener {
        void onUpdateQuantity(CartItemEntity item, int newQuantity);
        void onRemoveItem(CartItemEntity item);
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
        holder.bind(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateItems(List<CartItemEntity> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    public List<CartItemEntity> getItems() {
        return cartItems;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView itemPrice;
        private final TextView quantity;
        private final ImageButton btnPlus;
        private final ImageButton btnMinus;
        private final ImageButton btnRemove;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.cart_item_name);
            itemPrice = itemView.findViewById(R.id.cart_item_price);
            quantity = itemView.findViewById(R.id.cart_item_quantity);
            btnPlus = itemView.findViewById(R.id.btn_increase);
            btnMinus = itemView.findViewById(R.id.btn_decrease);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        void bind(CartItemEntity item) {
            itemName.setText(item.getItemName());
            itemPrice.setText(String.format(Locale.getDefault(), "$%.2f",
                    item.getPricePerItem() * item.getQuantity()));
            quantity.setText(String.valueOf(item.getQuantity()));

            btnPlus.setOnClickListener(v ->
                    listener.onUpdateQuantity(item, item.getQuantity() + 1));

            btnMinus.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    listener.onUpdateQuantity(item, item.getQuantity() - 1);
                } else {
                    listener.onRemoveItem(item);
                }
            });

            btnRemove.setOnClickListener(v -> listener.onRemoveItem(item));
        }
    }
}