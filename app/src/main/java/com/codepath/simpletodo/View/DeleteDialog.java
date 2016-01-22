package com.codepath.simpletodo.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.codepath.simpletodo.Activities.MainActivity;
import com.codepath.simpletodo.Activities.TodoItemDetailActivity;
import com.codepath.simpletodo.Model.TodoItem;
import com.codepath.simpletodo.Model.TodoItemDatabase;
import com.codepath.simpletodo.R;

/**
 * Created by JaneChung on 1/18/16.
 */
public class DeleteDialog {

    public static void showConfirmDeleteDialogForTodoItem(Activity sourceActivity, TodoItem todo, TodoItemDatabase db){
        AlertDialog confirmCancelDialog;
        confirmCancelDialog = new AlertDialog.Builder(sourceActivity)
                .setIcon(R.mipmap.warning_iocns)
                .setTitle("Are you sure to want to delete this to-do item?")
                .setPositiveButton("Yes",
                        new YesDeleteButtonListener(sourceActivity, todo, db))
                .setNegativeButton("No",
                        new CloseDialogButtonListener())
                .create();
        confirmCancelDialog.show();
    }



    // An inner class to handle event when user select Yes button on Confirm Cancel dialog
    private static class YesDeleteButtonListener implements OnClickListener {

        private Activity sourceActivity; // the activity that called the dialog
        private TodoItem todo;
        private TodoItemDatabase db;

        public YesDeleteButtonListener(Activity sourceActivity, TodoItem todo, TodoItemDatabase db){
            this.sourceActivity = sourceActivity;
            this.todo = todo;
            this.db = db;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            db.deleteTodoItem(todo);
            if (sourceActivity instanceof TodoItemDetailActivity) {
                Intent returnIntent = new Intent();
                sourceActivity.setResult(Activity.RESULT_OK, returnIntent);
                sourceActivity.finish();
            } else if (sourceActivity instanceof MainActivity) {
                ((MainActivity) sourceActivity).refreshData();
            }
        }

    }

    private static class CloseDialogButtonListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss(); // just close the dialog box
        }

    }
}
