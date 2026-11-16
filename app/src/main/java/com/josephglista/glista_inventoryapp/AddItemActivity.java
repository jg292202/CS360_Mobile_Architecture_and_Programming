package com.josephglista.glista_inventoryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
    Activity for adding new items to the inventory.
    Allows user to enter name, SKU, category, and quantity.
    Input is validated and saved to the database.
 */

public class AddItemActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText skuField;
    private EditText categoryField;
    private EditText quantityField;

    private AppDatabaseHelper db;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Connect XML views to their respective IDs
        nameField = findViewById(R.id.nameField);
        skuField = findViewById(R.id.skuField);
        categoryField = findViewById(R.id.categoryField);
        quantityField = findViewById(R.id.quantityField);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Database helper
        db = new AppDatabaseHelper(this);

        saveButton.setOnClickListener(v -> saveItem());
        cancelButton.setOnClickListener(v -> finish());
    }

    /*
        Validates user input and saves the item to the database.
        Displays feedback to the user on success or failure.
     */
    private void saveItem() {
        String name = nameField.getText().toString().trim();
        String sku = skuField.getText().toString().trim();
        String category = categoryField.getText().toString().trim();
        String qtyText = quantityField.getText().toString().trim();

        // Ensures all fields are filled
        if (name.isEmpty() || sku.isEmpty() || category.isEmpty() || qtyText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate quantity input
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity. Must be a number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert item into database
        long result = db.addItem(name, sku, category, quantity);

        if (result != -1) {
            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Return to InventoryActivity
        } else {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show();
        }
    }
}
