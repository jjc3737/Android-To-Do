package com.codepath.simpletodo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.simpletodo.Model.TodoItem;
import com.codepath.simpletodo.Model.TodoItemDatabase;
import com.codepath.simpletodo.R;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    private TodoItem todo = null;
    private int userAction;
    private final int USER_ACTION_ADD = 1;
    private final int USER_ACTION_EDIT = 2;
    private TodoItemDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        db = TodoItemDatabase.getsInstance(this);

        //get extra from when it's editing
        Bundle editBundle = this.getIntent().getExtras();
        try {
            this.todo = (TodoItem) editBundle.getSerializable(todo.TODO_EXTRA_KEY);
        } catch (Exception ex){
            ex.printStackTrace();
        }

        if (todo == null) {
            this.userAction = this.USER_ACTION_ADD;
            this.todo = new TodoItem();
            setTitle("Add");
        } else {
            this.userAction = this.USER_ACTION_EDIT;
            loadTodoItemToFields();
            setTitle("Edit");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_todo_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent resultIntent = new Intent();
        Bundle resultBundle;

        //Create or Update to do item and save to database
        if (item.getItemId() == R.id.actionbar_save) {
            if (this.userAction == USER_ACTION_ADD) {
                addTodoItemToDatabase();
                setResult(RESULT_OK, resultIntent);
            } else {
                editExistingTask();
                resultBundle = new Bundle();
                resultBundle.putSerializable(todo.TODO_EXTRA_KEY, this.todo);
                resultIntent.putExtras(resultBundle);
                setResult(RESULT_OK, resultIntent);
            }

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNewTodoItemFromInput() {
        String itemTitle = ((EditText)findViewById(R.id.et_todo_title)).getText().toString();
        this.todo.setItemTitle(itemTitle);

        DatePicker dueDatePicker = (DatePicker) findViewById(R.id.date_picker_due_date);
        this.todo.getDueDate().set(Calendar.DATE, dueDatePicker.getDayOfMonth());
        this.todo.getDueDate().set(Calendar.MONTH, dueDatePicker.getMonth());
        this.todo.getDueDate().set(Calendar.YEAR, dueDatePicker.getYear());

        int priorityLevel = ((Spinner)findViewById(R.id.spinner_priority)).getSelectedItemPosition();
        this.todo.setPriority(priorityLevel);

        int status = ((Spinner)findViewById(R.id.spinner_status)).getSelectedItemPosition();
        this.todo.setIsCompleted(status);

        this.todo.setId(db.getNewTodoItemId());
    }

    private void addTodoItemToDatabase() {
        createNewTodoItemFromInput();
        this.db.storeNewTodoItemToDB(this.todo);
    }

    private void editExistingTask() {
        updateTodoItemFromFields();
        db.updateTodoItem(this.todo);
    }

    private void updateTodoItemFromFields() {
        String taskTitle = ((EditText)findViewById(R.id.et_todo_title)).getText().toString();
        this.todo.setItemTitle(taskTitle);

        DatePicker taskDueDatePicker = (DatePicker) findViewById(R.id.date_picker_due_date);
        this.todo.getDueDate().set(Calendar.DATE, taskDueDatePicker.getDayOfMonth());
        this.todo.getDueDate().set(Calendar.MONTH, taskDueDatePicker.getMonth());
        this.todo.getDueDate().set(Calendar.YEAR, taskDueDatePicker.getYear());

        int priority = ((Spinner)findViewById(R.id.spinner_priority)).getSelectedItemPosition();
        this.todo.setPriority(priority);

        int isCompleted = ((Spinner)findViewById(R.id.spinner_status)).getSelectedItemPosition();
        this.todo.setIsCompleted(isCompleted);
    }

    private void loadTodoItemToFields() {
        if (this.userAction == this.USER_ACTION_EDIT) {

            EditText etTitle = (EditText) findViewById(R.id.et_todo_title);
            etTitle.setText(this.todo.getItemTitle());

            DatePicker taskDueDatePicker = (DatePicker) findViewById(R.id.date_picker_due_date);
            taskDueDatePicker.updateDate(this.todo.getDueDate().get(Calendar.YEAR),
                    this.todo.getDueDate().get(Calendar.MONTH),
                    this.todo.getDueDate().get(Calendar.DATE));

            Spinner taskPriorityLevelSpinner = (Spinner) findViewById(R.id.spinner_priority);
            taskPriorityLevelSpinner.setSelection(this.todo.gePriority());

            Spinner completionStatusSpinner = (Spinner) findViewById(R.id.spinner_status);
            completionStatusSpinner.setSelection(this.todo.getIsCompleted());
        }
    }
}
