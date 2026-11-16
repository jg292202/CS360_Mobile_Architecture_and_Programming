package com.josephglista.glista_inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
    Login screen for the Inventory App.
    Allows users to login with a username and password, or create account.
    Successful registration navigates to SMS permission screen.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private Button createAccountButton;
    private AppDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect XML views to their respective IDs
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        // Database helper
        db = new AppDatabaseHelper(this);

        // Login button click listener and validate user credentials
        loginButton.setOnClickListener(v -> loginUser());

        // Create account button click listener and register new user
        createAccountButton.setOnClickListener(v -> registerUser());
    }

    /*
        Try logging the user in by validating their credentials.
        If successful, navigate to InventoryActivity.
     */
    private void loginUser() {
        String user = usernameField.getText().toString().trim();
        String pass = passwordField.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.validateUser(user, pass)) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, InventoryActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        Register a new user in the database.
        If successful, navigate to SMS permission screen.
     */
    private void registerUser() {
        String user = usernameField.getText().toString().trim();
        String pass = passwordField.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.registerUser(user, pass);

        if (success) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

            // Navigate to SMS permission screen
            startActivity(new Intent(this, SMSPermissionActivity.class));

            // Clear fields
            usernameField.setText("");
            passwordField.setText("");

        } else {
            Toast.makeText(this, "Registration failed, username already exists.", Toast.LENGTH_SHORT).show();
        }
    }
}