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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderEntity> orders;
    private final OrderActionListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final boolean isAdminView;

    public interface OrderActionListener {
        void onAcceptOrder(OrderEntity order);
        void onPayOrder(OrderEntity order);
        void onAssignDelivery(OrderEntity order);
        void onMarkDelivered(OrderEntity order);
        void onCancelOrder(OrderEntity order);
    }

    public OrderAdapter(List<OrderEntity> orders, OrderActionListener listener, boolean isAdminView) {
        this.orders = orders;
        this.listener = listener;
        this.isAdminView = isAdminView;
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
        private final Button secondaryButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderAmount = itemView.findViewById(R.id.order_amount);
            actionButton = itemView.findViewById(R.id.action_button);
            secondaryButton = itemView.findViewById(R.id.secondary_button);
        }

        public void bind(OrderEntity order) {
            orderId.setText("Order #" + order.getId());
            orderDate.setText(dateFormat.format(new Date(order.getOrderDate())));
            orderStatus.setText(order.getStatus());
            orderAmount.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

            actionButton.setVisibility(View.VISIBLE);
            secondaryButton.setVisibility(View.VISIBLE);

            if (isAdminView) {
                setupAdminActions(order);
            } else {
                setupCustomerActions(order);
            }
        }

        private void setupAdminActions(OrderEntity order) {
            switch (order.getStatus()) {
                case OrderStatus.PENDING:
                    if (order.isPaid()) {
                        actionButton.setText("Accept Order");
                        actionButton.setEnabled(true);
                        actionButton.setOnClickListener(v -> listener.onAcceptOrder(order));

                        secondaryButton.setText("Cancel");
                        secondaryButton.setEnabled(true);
                        secondaryButton.setOnClickListener(v -> listener.onCancelOrder(order));
                    } else {
                        actionButton.setText("Awaiting Payment");
                        actionButton.setEnabled(false);
                        secondaryButton.setVisibility(View.GONE);
                    }
                    break;

                case OrderStatus.ACCEPTED:
                    actionButton.setText("Assign Delivery");
                    actionButton.setEnabled(true);
                    actionButton.setOnClickListener(v -> listener.onAssignDelivery(order));
                    secondaryButton.setVisibility(View.GONE);
                    break;

                case OrderStatus.ASSIGNED:
                    actionButton.setText("Mark Delivered");
                    actionButton.setEnabled(true);
                    actionButton.setOnClickListener(v -> listener.onMarkDelivered(order));
                    secondaryButton.setVisibility(View.GONE);
                    break;

                case OrderStatus.DELIVERED:
                    actionButton.setText("Completed");
                    actionButton.setEnabled(false);
                    secondaryButton.setVisibility(View.GONE);
                    break;

                case OrderStatus.CANCELLED:
                    actionButton.setText("Cancelled");
                    actionButton.setEnabled(false);
                    secondaryButton.setVisibility(View.GONE);
                    break;
            }
        }

        private void setupCustomerActions(OrderEntity order) {
            secondaryButton.setVisibility(View.GONE);

            if (!order.isPaid() && OrderStatus.PENDING.equals(order.getStatus())) {
                actionButton.setText("Pay Now");
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(v -> listener.onPayOrder(order));
            } else if (OrderStatus.PENDING.equals(order.getStatus()) && !order.isPaid()) {
                actionButton.setText("Cancel Order");
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(v -> listener.onCancelOrder(order));
            } else {
                actionButton.setVisibility(View.GONE);
            }
        }
    }
}