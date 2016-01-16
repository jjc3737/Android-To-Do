package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    int position;
    EditText editItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String textToEdit = getIntent().getStringExtra("itemText");
        position = getIntent().getIntExtra("position", 0);

        editItemText = (EditText) findViewById(R.id.etItemText);
        editItemText.setText(textToEdit);
        editItemText.setSelection(textToEdit.length());


    }

    public void onSaveItem(View view) {
        String editedText = editItemText.getText().toString();

        Intent backToMainActivity = new Intent();
        backToMainActivity.putExtra("editedText", editedText);
        backToMainActivity.putExtra("textPosition", position);

        setResult(RESULT_OK, backToMainActivity);
        finish();
    }
}
