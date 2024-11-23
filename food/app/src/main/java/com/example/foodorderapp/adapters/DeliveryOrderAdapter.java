package com.example.foodorderapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.database.entities.OrderEntity;
import com.example.foodorderapp.utils.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeliveryOrderAdapter extends RecyclerView.Adapter<DeliveryOrderAdapter.OrderViewHolder> {
    private List<OrderEntity> orders;
    private final DeliveryOrderListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface DeliveryOrderListener {
        void onStartDelivery(OrderEntity order);
        void onMarkDelivered(OrderEntity order);
        void onViewDetails(OrderEntity order);
    }

    public DeliveryOrderAdapter(List<OrderEntity> orders, DeliveryOrderListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position));
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
        private final TextView orderId;
        private final TextView orderDate;
        private final TextView orderStatus;
        private final TextView orderAmount;
        private final Button actionButton;
        private final Button detailsButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.delivery_order_id);
            orderDate = itemView.findViewById(R.id.delivery_order_date);
            orderStatus = itemView.findViewById(R.id.delivery_order_status);
            orderAmount = itemView.findViewById(R.id.delivery_order_amount);
            actionButton = itemView.findViewById(R.id.delivery_action_button);
            detailsButton = itemView.findViewById(R.id.delivery_details_button);
        }

        public void bind(OrderEntity order) {
            orderId.setText("Order #" + order.getId());
            orderDate.setText(dateFormat.format(new Date(order.getOrderDate())));
            orderStatus.setText(order.getStatus());
            orderAmount.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

            detailsButton.setOnClickListener(v -> listener.onViewDetails(order));

            if (OrderStatus.ASSIGNED.equals(order.getStatus())) {
                actionButton.setText("Start Delivery");
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(v -> listener.onStartDelivery(order));
            } else if (OrderStatus.DELIVERING.equals(order.getStatus())) {
                actionButton.setText("Mark Delivered");
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(v -> listener.onMarkDelivered(order));
            } else {
                actionButton.setText(order.getStatus());
                actionButton.setEnabled(false);
            }
        }
    }
}