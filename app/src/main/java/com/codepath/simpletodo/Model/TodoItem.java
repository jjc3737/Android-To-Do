package com.codepath.simpletodo.Model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by JaneChung on 1/17/16.
 */

public class TodoItem implements Serializable {

    public static final int LOW_PRIORITY = 0;
    public static final int MEDIUM_PRIORITY = 1;
    public static final int HIGH_PRIORITY = 2;

    public static final int IS_NOT_COMPLETED = 0;
    public static final int IS_COMPLETED = 1;

    public static final String TODO_EXTRA_KEY = "todo_extra_key";

    public String id;
    public String itemTitle;
    public Calendar dueDate;
    public int priority;
    public int isCompleted;

    public TodoItem() {
        this.id = "";
        this.itemTitle = "";
        this.dueDate = Calendar.getInstance();
        this.priority = LOW_PRIORITY;
        this.isCompleted = IS_NOT_COMPLETED;
    }

    public TodoItem(String id, String itemTitle, Calendar dueDate, int priority, int isCompleted) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }

    public int gePriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIsCompleted() {
        return  isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }



}
