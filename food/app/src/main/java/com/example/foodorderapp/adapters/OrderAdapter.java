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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderEntity> orders;
    private final OrderActionListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OrderActionListener {
        void onAcceptOrder(OrderEntity order);
        void onAssignDelivery(OrderEntity order);
        void onMarkDelivered(OrderEntity order);
    }

    public OrderAdapter(List<OrderEntity> orders, OrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
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

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderAmount = itemView.findViewById(R.id.order_amount);
            actionButton = itemView.findViewById(R.id.action_button);
        }

        public void bind(OrderEntity order) {
            orderId.setText("Order #" + order.getId());
            orderDate.setText(dateFormat.format(new Date(order.getOrderDate())));
            orderStatus.setText(order.getStatus());
            orderAmount.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

            switch (order.getStatus()) {
                case "PENDING":
                    if (order.isPaid()) {
                        actionButton.setText("Accept Order");
                        actionButton.setOnClickListener(v -> listener.onAcceptOrder(order));
                    } else {
                        actionButton.setText("Awaiting Payment");
                        actionButton.setEnabled(false);
                    }
                    break;
                case "ACCEPTED":
                    actionButton.setText("Assign Delivery");
                    actionButton.setOnClickListener(v -> listener.onAssignDelivery(order));
                    break;
                case "ASSIGNED":
                    actionButton.setText("Mark Delivered");
                    actionButton.setOnClickListener(v -> listener.onMarkDelivered(order));
                    break;
                case "DELIVERED":
                    actionButton.setText("Completed");
                    actionButton.setEnabled(false);
                    break;
            }
        }
    }
}