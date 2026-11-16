package com.josephglista.glista_inventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
    Activity responsible for displaying the inventory list and handling FAB click.
    Provides navigation to AddItemActivity for adding new items.
    Refreshes list when returning from AddItemActivity.
 */

public class InventoryActivity extends AppCompatActivity {
    private RecyclerView inventoryRecyclerView;
    private FloatingActionButton addItemFab;
    private AppDatabaseHelper db;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Connect views to their respective IDs
        inventoryRecyclerView = findViewById(R.id.inventoryRecyclerView);
        addItemFab = findViewById(R.id.addItemFab);

        // Database helper
        db = new AppDatabaseHelper(this);

        // Set up RecyclerView
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadItems();

        // FAB click listener to add new item screen
        addItemFab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddItemActivity.class));
        });
    }

    /*
         Loads all items from the database and updates the RecyclerView
     */
    private void loadItems() {
        List<Item> items = db.getAllItems();
        adapter = new ItemAdapter(items, db, this::loadItems);
        inventoryRecyclerView.setAdapter(adapter);
    }

    /*
        Refreshes the RecyclerView when returning from AddItemActivity or updates
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
}
