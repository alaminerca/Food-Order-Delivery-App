package com.example.foodorderapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activities.MainActivity;

public class OrderNotificationService {
    private static final String CHANNEL_ID = "order_status_channel";
    private static final String CHANNEL_NAME = "Order Status";
    private static final String CHANNEL_DESC = "Food Order Status Updates";

    private final Context context;
    private final NotificationManager notificationManager;

    public OrderNotificationService(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showOrderStatusNotification(int orderId, String status) {
        String title;
        String message;

        switch (status) {
            case OrderStatus.ACCEPTED:
                title = "Order Accepted";
                message = "Your order #" + orderId + " has been accepted and is being prepared.";
                break;
            case OrderStatus.ASSIGNED:
                title = "Delivery Assigned";
                message = "A delivery agent has been assigned to your order #" + orderId;
                break;
            case OrderStatus.DELIVERING:
                title = "Out for Delivery";
                message = "Your order #" + orderId + " is on its way!";
                break;
            case OrderStatus.DELIVERED:
                title = "Order Delivered";
                message = "Your order #" + orderId + " has been delivered. Enjoy your meal!";
                break;
            case OrderStatus.CANCELLED:
                title = "Order Cancelled";
                message = "Your order #" + orderId + " has been cancelled.";
                break;
            default:
                title = "Order Update";
                message = "Your order #" + orderId + " status: " + status;
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, orderId, intent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(orderId, builder.build());
    }

    public void cancelNotification(int orderId) {
        notificationManager.cancel(orderId);
    }
}