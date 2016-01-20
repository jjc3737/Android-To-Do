package com.codepath.simpletodo.Activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.Model.TodoItem;
import com.codepath.simpletodo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by JaneChung on 1/17/16.
 */

public class TodoItemAdapter extends ArrayAdapter {
    public TodoItemAdapter(Context context, ArrayList<TodoItem> items) {
        super(context, 0, items);
    }

    public ArrayList<TodoItem> todoItems;

    public TodoItemAdapter(Context context, int resource, ArrayList<TodoItem> items) {
        super(context, resource, items);
        todoItems = items;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem item = this.todoItems.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        View vDone = (View) convertView.findViewById(R.id.vDone);

        //Custom font
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/GothamBook.ttf");

        tvTitle.setTypeface(custom_font);

        tvTitle.setText(item.getItemTitle());
        String textToDisplay;

        //Different colors for different priority
        int color;
        if (item.gePriority() == 0) {
            textToDisplay = "LOW";
            color = Color.parseColor("#ffd700");
        } else if (item.gePriority() == 1) {
            textToDisplay = "MEDIUM";
            color = Color.parseColor("#75C1C2");
        } else {
            textToDisplay = "HIGH";
            color = Color.parseColor("#ff5050");

        }


        tvPriority.setText(textToDisplay);

        //Show Due date
        Calendar dueDate = item.getDueDate();

        String date = "Due on: " + dueDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + " "
                + dueDate.get(Calendar.DATE) + " "
                + dueDate.get(Calendar.YEAR);
        tvDueDate.setText(date);

        int greyedOut = Color.parseColor("#b2b1b1");
        int black = Color.parseColor("#000000");

        //Show line & grey out if done
        if (item.isCompleted == 1) {
            vDone.setVisibility(View.VISIBLE);
            tvTitle.setTextColor(greyedOut);
            tvPriority.setTextColor(greyedOut);
            tvDueDate.setTextColor(greyedOut);
        } else {
            vDone.setVisibility(View.INVISIBLE);
            tvTitle.setTextColor(black);
            tvPriority.setTextColor(color);
            tvDueDate.setTextColor(black);
        }

        // Return the completed view to render on screen
        return convertView;
    }

    public void refreshTodoItems(ArrayList<TodoItem> newItems) {
        //This clears and reloads the array list
        todoItems.clear();
        todoItems.addAll(newItems);
        this.notifyDataSetChanged();

    }

}

