package com.josephglista.glista_inventoryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/*
    Activity for checking and requesting SMS permission.
    Displays status of SMS permission.
    Allows for user to proceed to InventoryActivity once permission is granted.
 */

public class SMSPermissionActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_REQUEST_CODE = 123;

    private TextView smsStatus;
    private Button checkPermissionButton;
    private Button requestPermissionButton;

    private Button continueButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        // Connect XML views to their respective IDs
        smsStatus = findViewById(R.id.smsStatus);
        checkPermissionButton = findViewById(R.id.checkPermissionButton);
        requestPermissionButton = findViewById(R.id.requestPermissionButton);
        continueButton = findViewById(R.id.continueButton);


        // BUTTON: Check current permission status
        checkPermissionButton.setOnClickListener(v -> updatePermissionStatus());

        // BUTTON: Request SMS permission
        requestPermissionButton.setOnClickListener(v -> requestSMSPermission());

        // BUTTON: Continue to Inventory screen
        continueButton.setOnClickListener(v -> startActivity(new Intent(this, InventoryActivity.class)));


        // Show initial permission status
        updatePermissionStatus();
    }


    /*
        Updates the TextView to show if SMS permission is granted or not.
        Shows or hides the Continue button based on permission status.
     */
    private void updatePermissionStatus() {
        boolean granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;

        if (granted) {
            smsStatus.setText("Permission status: Granted");
            continueButton.setVisibility(View.VISIBLE);
        } else {
            smsStatus.setText("Permission status: Denied");
            continueButton.setVisibility(View.GONE);
        }
    }

    /*
        Requests SMS permission from the user.
     */
    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                SMS_PERMISSION_REQUEST_CODE);
    }

    /*
        Handles the result of the permission request.
        Refreshes the permission status TextView.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            updatePermissionStatus();
        }
    }
}
