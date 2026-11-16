package com.josephglista.glista_inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/*
    SQLite database helper for user accounts and inventory items.
    Handles login, registration, and CRUD operations, and quantity updates.
 */
public class AppDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory_app.db";
    private static final int DATABASE_VERSION = 1;

    // USER TABLE
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";


    // INVENTORY TABLE
    public static final String TABLE_ITEMS = "items";
    public static final String COL_ITEM_ID = "id";
    public static final String COL_ITEM_NAME = "name";
    public static final String COL_ITEM_SKU = "sku";
    public static final String COL_ITEM_CATEGORY = "category";
    public static final String COL_ITEM_QUANTITY = "quantity";

    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
        CREATE TABLES
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "(" +
            COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
            COL_PASSWORD + " TEXT NOT NULL);";

        // Create items table
        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + "(" +
            COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ITEM_NAME + " TEXT NOT NULL, " +
            COL_ITEM_SKU + " TEXT NOT NULL, " +
            COL_ITEM_CATEGORY + " TEXT NOT NULL, " +
            COL_ITEM_QUANTITY + " INTEGER NOT NULL);";

        db.execSQL(createUsersTable);
        db.execSQL(createItemsTable);
    }

    /*
        UPGRADE HANDLER
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    /*
        REGISTER A NEW USER, RETURNS TRUE IF SUCCESSFUL
    */
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }

    /*
        VALIDATE USER LOGIN, RETURNS TRUE IF SUCCESSFUL
     */
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COL_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean isValid = cursor.moveToFirst();

        cursor.close();
        db.close();

        return isValid;
    }


    /*
         ADD ITEM TO INVENTORY, RETURNS ITEM ID
     */
    public long addItem(String name, String sku, String category, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ITEM_NAME, name);
        values.put(COL_ITEM_SKU, sku);
        values.put(COL_ITEM_CATEGORY, category);
        values.put(COL_ITEM_QUANTITY, quantity);

        long id = db.insert(TABLE_ITEMS, null, values);
        db.close();

        return id;
    }

    /*
        UPDATE ITEM QUANTITY
     */
    public int updateQuantity(long itemId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ITEM_QUANTITY, newQuantity);

        int rows = db.update(TABLE_ITEMS, values, COL_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});

        db.close();
        return rows;
    }

    /*
        DELETE ITEM FROM INVENTORY
     */
    public int deleteItem(long itemId) {
        SQLiteDatabase db = this.getWritableDatabase();

        int rows = db.delete(TABLE_ITEMS, COL_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});

        db.close();
        return rows;
    }

    /*
        RETRIEVE ALL ITEMS FROM INVENTORY
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_NAME));
                String sku = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_SKU));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_CATEGORY));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_QUANTITY));

                items.add(new Item(id, name, sku, category, quantity));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return items;
    }
}
