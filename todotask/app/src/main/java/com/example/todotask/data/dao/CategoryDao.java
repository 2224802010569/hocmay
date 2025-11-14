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


    /*public List<Category> getAllCategories() {
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
    }*/
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY,
                new String[]{DatabaseHelper.COLUMN_CATEGORY_ID, DatabaseHelper.COLUMN_CATEGORY_NAME, DatabaseHelper.COLUMN_CATEGORY_COLOR},
                null, null, null, null, DatabaseHelper.COLUMN_CATEGORY_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new Category(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_COLOR))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }
    // trả về null nếu không tìm thấy - lay id cho task detail tách riêng add task
    public Category getCategoryById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Category cat = null;
        Cursor cursor = db.query(DatabaseHelper.TABLE_CATEGORY,
                new String[]{DatabaseHelper.COLUMN_CATEGORY_ID, DatabaseHelper.COLUMN_CATEGORY_NAME, DatabaseHelper.COLUMN_CATEGORY_COLOR},
                DatabaseHelper.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, "1");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cat = new Category(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_COLOR))
                );
            }
            cursor.close();
        }
        db.close();
        return cat;
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
