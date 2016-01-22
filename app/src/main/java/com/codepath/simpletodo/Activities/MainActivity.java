package com.codepath.simpletodo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.simpletodo.Model.TodoItem;
import com.codepath.simpletodo.Model.TodoItemDatabase;
import com.codepath.simpletodo.R;
import com.codepath.simpletodo.View.DeleteDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity  {

    private ListView lvItems;
    private ArrayList<TodoItem> items;
    private TodoItemDatabase db;
    private TodoItemAdapter adapter;

    static private final int ADD_TODO_ITEM_REQUEST_CODE = 1;
    static private final int DETAIL_TODO_ITEM_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("To Do");
        lvItems = (ListView) findViewById(R.id.ListView_todo_items);

        //Shows detailed view of to do item
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this, TodoItemDetailActivity.class);
                Bundle detailBundle = new Bundle();
                TodoItem todo = items.get(position);
                detailBundle.putSerializable(todo.TODO_EXTRA_KEY, todo);
                detailIntent.putExtras(detailBundle);
                startActivityForResult(detailIntent, DETAIL_TODO_ITEM_REQUEST_CODE);
            }
        });


        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem todo = items.get(position);
                DeleteDialog.showConfirmDeleteDialogForTodoItem(MainActivity.this, todo, db);
                return true;
            }
        });
        //instantiate database and get all to do items from SQLite
        db = TodoItemDatabase.getsInstance(this);
        populateItems();
    }

    public void populateItems() {

        if (db != null) {
            items = db.getAllTodoItems();
            sortItems();
            this.adapter = new TodoItemAdapter(this, 0, items);
            lvItems.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == ADD_TODO_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            refreshData();
        } else if (requestCode == DETAIL_TODO_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            //Probably a better way to do this instead of clearing and refreshing
            refreshData();
        }*/
        //refresh the to do list if result is ok from either intents
        if (resultCode == RESULT_OK) {
            refreshData();
        }
    }

    public void refreshData() {
        this.items = this.db.getAllTodoItems();
        sortItems();
        this.adapter.refreshTodoItems(this.items);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionbar_add) {
            //Intent to Add Item Activity
            showAddTodoItemActivity();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
        return true;
    }

    public void showAddTodoItemActivity() {
        Intent addNewItemIntent = new Intent(MainActivity.this, EditItemActivity.class);
        startActivityForResult(addNewItemIntent, ADD_TODO_ITEM_REQUEST_CODE);
    }

    public void sortItems() {
        //High priority shows up first
        Collections.sort(items, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem lhs, TodoItem rhs) {
                Integer completedLeft = lhs.getIsCompleted();
                Integer completedRight = rhs.getIsCompleted();
                int doneComparison = completedLeft.compareTo(completedRight);
                Integer left = lhs.gePriority();
                Integer right = rhs.gePriority();
                return doneComparison == 0 ? right.compareTo(left) : doneComparison;
            }
        });
    }
}

