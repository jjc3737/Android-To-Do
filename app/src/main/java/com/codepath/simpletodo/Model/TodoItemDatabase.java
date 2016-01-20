package com.codepath.simpletodo.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by JaneChung on 1/17/16.
 */
public class TodoItemDatabase extends SQLiteOpenHelper{

    private static final String TAG = "Exception_tag";
    private static final String DATABASE_NAME = "todoItemDatabase";
    private static final int DATABASE_VERSION = 3;

    //TodoItem Table
    public static final String TODO_ITEM_TABLE_NAME = "_todoItem";
    public static final String TODO_ITEM_TABLE_COLUMN_ID = "_id";
    public static final String TODO_ITEM_TABLE_COLUMN_TITLE = "_title";
    public static final String TODO_ITEM_TABLE_COLUMN_DUE_DATE ="_due_date";
    public static final String TODO_ITEM_TABLE_COLUMN_PRIORITY = "_priority";
    public static final String TODO_ITEM_TABLE_COLUMN_IS_COMPLETED = "_isCompleted";

    private static TodoItemDatabase sInstance;

    public static synchronized  TodoItemDatabase getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TODO_ITEM_TABLE_CREATE
                = "CREATE TABLE " + TODO_ITEM_TABLE_NAME
                + "("
                + TODO_ITEM_TABLE_COLUMN_ID + " TEXT PRIMARY KEY,"
                + TODO_ITEM_TABLE_COLUMN_TITLE + " TEXT NOT NULL,"
                + TODO_ITEM_TABLE_COLUMN_DUE_DATE + " INTEGER NOT NULL,"
                + TODO_ITEM_TABLE_COLUMN_PRIORITY + " TEXT NOT NULL,"
                + TODO_ITEM_TABLE_COLUMN_IS_COMPLETED + " INTEGER NOT NULL"
                + ")";
        db.execSQL(TODO_ITEM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TODO_ITEM_TABLE_NAME);
            onCreate(db);
        }
    }

    public void storeNewTodoItemToDB(TodoItem todo) {

        //Add a to do item to database
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(TODO_ITEM_TABLE_COLUMN_ID, todo.getId());
            values.put(TODO_ITEM_TABLE_COLUMN_TITLE, todo.getItemTitle());
            values.put(TODO_ITEM_TABLE_COLUMN_DUE_DATE, todo.getDueDate().getTimeInMillis());
            values.put(TODO_ITEM_TABLE_COLUMN_PRIORITY, todo.gePriority());
            values.put(TODO_ITEM_TABLE_COLUMN_IS_COMPLETED, todo.getIsCompleted());
            db.insert(TODO_ITEM_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add todo item to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<TodoItem>getAllTodoItems() {
        ArrayList<TodoItem> items = new ArrayList<>();

        //grab all to do items from database
        String TODO_SELECT_QUERY = "SELECT * FROM " + TODO_ITEM_TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(TODO_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    TodoItem item = new TodoItem();
                    item.setId(cursor.getString(cursor.getColumnIndex(TODO_ITEM_TABLE_COLUMN_ID)));
                    item.setItemTitle(cursor.getString(cursor.getColumnIndex(TODO_ITEM_TABLE_COLUMN_TITLE)));
                    Date date = new Date(cursor.getLong(cursor.getColumnIndex(TODO_ITEM_TABLE_COLUMN_DUE_DATE)));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    item.setDueDate(cal);
                    item.setPriority(cursor.getInt(cursor.getColumnIndex(TODO_ITEM_TABLE_COLUMN_PRIORITY)));
                    item.setIsCompleted(cursor.getInt(cursor.getColumnIndex(TODO_ITEM_TABLE_COLUMN_IS_COMPLETED)));
                    items.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while getting items from database");
        } finally {
            if (cursor != null & !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;

    }

    public void updateTodoItem(TodoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updateValues = new ContentValues();
        updateValues.put(TODO_ITEM_TABLE_COLUMN_TITLE, todo.getItemTitle());
        updateValues.put(TODO_ITEM_TABLE_COLUMN_DUE_DATE, todo.getDueDate().getTimeInMillis());
        updateValues.put(TODO_ITEM_TABLE_COLUMN_PRIORITY, todo.gePriority());
        updateValues.put(TODO_ITEM_TABLE_COLUMN_IS_COMPLETED, todo.getIsCompleted());
        db.update(TODO_ITEM_TABLE_NAME, updateValues, TODO_ITEM_TABLE_COLUMN_ID + " = '" + todo.getId() + "'", null);
    }

    public void deleteTodoItem(TodoItem todo){
        SQLiteDatabase db = this.getWritableDatabase();
        String id = todo.getId();
        db.delete(TODO_ITEM_TABLE_NAME, TODO_ITEM_TABLE_COLUMN_ID + " = '" + id + "'", null);
    }

    public Cursor getTodoItemById(String taskId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TODO_ITEM_TABLE_NAME,
                new String[] {TODO_ITEM_TABLE_COLUMN_ID, TODO_ITEM_TABLE_COLUMN_TITLE, TODO_ITEM_TABLE_COLUMN_DUE_DATE, TODO_ITEM_TABLE_COLUMN_PRIORITY, TODO_ITEM_TABLE_COLUMN_IS_COMPLETED},
                TODO_ITEM_TABLE_COLUMN_ID + " = '" + taskId + "'", null, null, null, null);
    }

    public String getNewTodoItemId(){

        //create a unique id for each item

        String uuid = null;
        Cursor cursor = null;

        do {
            uuid = UUID.randomUUID().toString();
            cursor = getTodoItemById(uuid);
        } while (cursor.getCount() > 0);

        return uuid;
    }

}
