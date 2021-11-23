package com.example.keeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddTextHighlightActivity extends AppCompatActivity {
    private String id;
    private TextInputEditText pageInputEditText, highlightInputEditText;
    private Button saveHighlightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_highlight);

        pageInputEditText = findViewById(R.id.pageInputEditText);
        highlightInputEditText = findViewById(R.id.highlightInputEditText);
        saveHighlightButton = findViewById(R.id.saveHighlight);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
             id = bundle.getString("id");
        }

        saveHighlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String highlight = highlightInputEditText.getText().toString();
                Integer pageNumber = Integer.parseInt(pageInputEditText.getText().toString());

                if (highlight != "") {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(AddTextHighlightActivity.this);
                    myDB.addHighlight(id, highlight, pageNumber);
                    myDB.close();
                    finish();
                    Intent intent = new Intent(AddTextHighlightActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}