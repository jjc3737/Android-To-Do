package com.codepath.simpletodo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codepath.simpletodo.Model.TodoItem;
import com.codepath.simpletodo.Model.TodoItemDatabase;
import com.codepath.simpletodo.R;
import com.codepath.simpletodo.View.DeleteDialog;

import java.util.Calendar;
import java.util.Locale;

public class TodoItemDetailActivity extends AppCompatActivity {

    private TodoItem todo;
    public static final int EDIT_TODO_REQUEST_CODE = 1;
    private TodoItemDatabase db;
    public Boolean isEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item_detail);

        db = TodoItemDatabase.getsInstance(this);

        setTitle("Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get extra from Intent
        Bundle detailBundle = this.getIntent().getExtras();
        try {
            this.todo = (TodoItem) detailBundle.getSerializable(TodoItem.TODO_EXTRA_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //show the to do item in the text views
        this.showDetailedData();

    }

    private void showDetailedData(){

        if (this.todo == null) {
            this.finish();
        } else {

            TextView taskTitleTextView = (TextView) findViewById(R.id.todo_content);
            taskTitleTextView.setText(this.todo.getItemTitle());

            TextView taskDueDateTextView = (TextView) findViewById(R.id.due_date_content);
            Calendar dueDate = this.todo.getDueDate();
            String dueDateString = dueDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " "
                    + dueDate.get(Calendar.DATE) + " "
                    + dueDate.get(Calendar.YEAR);
            taskDueDateTextView.setText(dueDateString);

            TextView priorityTextView = (TextView) findViewById(R.id.priority_content);
            String priorityString;

            if (this.todo.gePriority() == 0) {
                priorityString = this.getString(R.string.priority_low);
            } else if (this.todo.gePriority() == 1) {
                priorityString = this.getString(R.string.priority_medium);
            } else {
                priorityString = this.getString(R.string.priority_high);
            }

            priorityTextView.setText(priorityString);

            TextView completionTextView = (TextView) findViewById(R.id.is_completed_content);
            String completionString;
            if (this.todo.getIsCompleted() == todo.IS_COMPLETED) {
                completionString = getString(R.string.status_is_completed);
            } else {
                completionString = getString(R.string.status_is_not_completed);
            }
            completionTextView.setText(completionString);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_or_delete_todo_item, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //If user finished editing, refresh text views to reflect data
        if (requestCode == EDIT_TODO_REQUEST_CODE && resultCode == RESULT_OK) {
            this.isEdited = true;
            this.todo = (TodoItem) data.getExtras().getSerializable(todo.TODO_EXTRA_KEY);
            this.showDetailedData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){ // hit back button to return to list of tasks
            case android.R.id.home:
                if (isEdited == true) {
                    Intent returnIntent = new Intent();
                    this.db.updateTodoItem(this.todo);
                    setResult(RESULT_OK, returnIntent);
                }
                this.finish();
                return true;
            case R.id.actionbar_edit:
                goToEditTodoItem();
                return true;
            case R.id.actionbar_delete:
                DeleteDialog.showConfirmDeleteDialogForTodoItem(this, this.todo, this.db);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToEditTodoItem() {
        //Intent to Edit item Activity
        Intent editIntent = new Intent(TodoItemDetailActivity.this, EditItemActivity.class);
        Bundle detailBundle = new Bundle();
        detailBundle.putSerializable(todo.TODO_EXTRA_KEY, this.todo);
        editIntent.putExtras(detailBundle);
        startActivityForResult(editIntent, EDIT_TODO_REQUEST_CODE);
    }

}
