package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditItemFragment.EditItemFragmentListener{
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etEditText;
    int editPosition;

    static private final int GET_EDITED_TEXT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        etEditText = (EditText) findViewById(R.id.etNewItem);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
//                i.putExtra("position", position);
//                i.putExtra("itemText", items.get(position));
//                startActivityForResult(i, GET_EDITED_TEXT_REQUEST_CODE);
                editPosition = position;
                showEditFragment(items.get(position));
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    public void populateItems() {
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

    }
    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View view) {
        itemsAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    private void showEditFragment(String textToEdit) {
//        EditItemFragment editItemFragment = EditItemFragment.newInstance("Edit Item", textToEdit);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, editItemFragment);
//        transaction.addToBackStack(null);
//
//        transaction.commit();
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemFragment = EditItemFragment.newInstance("Edit Item", textToEdit);
        editItemFragment.show(fm, "fragment_edit_item");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_EDITED_TEXT_REQUEST_CODE && resultCode == RESULT_OK) {
            String editedText = data.getStringExtra("editedText");
            items.set(data.getIntExtra("textPosition", 0), editedText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    @Override
    public void onFinishEditItem(String inputText) {
        items.set(editPosition, inputText);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}
