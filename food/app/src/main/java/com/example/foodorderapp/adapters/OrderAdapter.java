package com.example.foodorderapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.OrderEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderEntity> orders;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public OrderAdapter(List<OrderEntity> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEntity order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<OrderEntity> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderIdText;
        private TextView orderDateText;
        private TextView orderTotalText;
        private TextView orderStatusText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.order_id);
            orderDateText = itemView.findViewById(R.id.order_date);
            orderTotalText = itemView.findViewById(R.id.order_total);
            orderStatusText = itemView.findViewById(R.id.order_status);
        }

        public void bind(OrderEntity order) {
            orderIdText.setText("Order #" + order.getId());
            orderDateText.setText(dateFormat.format(new Date(order.getOrderDate())));
            orderTotalText.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));
            orderStatusText.setText(order.getOrderStatus());
        }
    }
}