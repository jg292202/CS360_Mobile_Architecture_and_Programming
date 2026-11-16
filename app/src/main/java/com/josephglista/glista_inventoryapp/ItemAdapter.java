package com.josephglista.glista_inventoryapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
    RecyclerView Adapter for the inventory list.
    Handles delete, increase quantity, and decrease quantity buttons, low stock SMS alerts.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private AppDatabaseHelper db;
    private Runnable refreshCallback;

    public ItemAdapter(List<Item> itemList, AppDatabaseHelper db, Runnable refreshCallback) {
        this.itemList = itemList;
        this.db = db;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        // Bind data to views
        holder.itemName.setText(item.name);
        holder.itemSKU.setText(item.sku);
        holder.itemCategory.setText(item.category);
        holder.itemQuantity.setText("Qty: " + item.quantity);

        // DELETE BUTTON
        holder.deleteButton.setOnClickListener(v -> {
            db.deleteItem(item.id);
            Toast.makeText(v.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            refreshCallback.run();
        });

        // INCREASE QUANTITY
        holder.increaseButton.setOnClickListener(v -> {
            int newQty = item.quantity + 1;
            db.updateQuantity(item.id, newQty);
            item.quantity = newQty;
            holder.itemQuantity.setText("Qty: " + newQty);
        });

        // DECREASE QUANTITY
        holder.decreaseButton.setOnClickListener(v -> {
            if (item.quantity > 0) {
                int newQty = item.quantity - 1;
                db.updateQuantity(item.id, newQty);
                item.quantity = newQty;
                holder.itemQuantity.setText("Qty: " + newQty);

                // If quantity reaches zero, send SMS notification
                if (newQty == 0) {
                    sendLowStockSMS(v.getContext(), item.name);
                }

            } else {
                Toast.makeText(v.getContext(), "Quantity cannot be negative", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /*
        Sends SMS notification when inventory reaches zero.
        Uses SmsManager API on Android 12 and above.
     */
    private void sendLowStockSMS(Context context, String itemName) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            SmsManager smsManager;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // For Android 12 and above, use the new SmsManager
                smsManager = context.getSystemService(SmsManager.class);
            } else {
                // Older Android versions use the deprecated SmsManager
                smsManager = SmsManager.getDefault();
            }
            smsManager.sendTextMessage(
                    "6505551212",  // Emulator's phone number.
                    null,
                    "Low stock alert! " + itemName + " has reached quantity: 0.",
                    null,
                    null
            );

            Toast.makeText(context, "SMS alert sent", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "SMS permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /*
        ViewHolder for item_inventory.xml
        Holds references to views in the layout for single item.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemSKU;
        TextView itemCategory;
        TextView itemQuantity;
        ImageButton deleteButton;
        Button increaseButton;
        Button decreaseButton;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemSKU = itemView.findViewById(R.id.itemSKU);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);

            deleteButton = itemView.findViewById(R.id.deleteButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}
