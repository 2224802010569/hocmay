package com.example.todotask.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.todotask.data.database.DatabaseHelper;
import com.example.todotask.data.model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private DatabaseHelper dbHelper;

    public CategoryDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("color", category.getColor());
        long id = db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
        db.close();
        return id;
    }

    public void deleteCategory(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CATEGORY, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY,
                new String[]{"id", "name", "color"},
                null, null, null, null, "id DESC");
        if (cursor.moveToFirst()) {
            do {
                list.add(new Category(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("color", category.getColor());
        db.update(DatabaseHelper.TABLE_CATEGORY, values, "id = ?", new String[]{String.valueOf(category.getId())});
        db.close();
    }

}
