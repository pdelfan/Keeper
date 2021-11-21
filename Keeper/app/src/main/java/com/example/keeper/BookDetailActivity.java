package com.example.keeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView coverDetail;
    private TextView titleDetail, authorsDetail, dateDetail, descriptionDetail, genreDetail, languageDetail;
    private Button addToBooksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addToBooksButton = findViewById(R.id.addToBooksButton);

        ImageView coverDetail = findViewById(R.id.coverDetail);
        titleDetail = findViewById(R.id.titleDetail);
        authorsDetail = findViewById(R.id.authorDetail);
        descriptionDetail = findViewById(R.id.descriptionDetail);
        genreDetail = findViewById(R.id.genreDetail);
        dateDetail = findViewById(R.id.dateDetail);
        languageDetail = findViewById(R.id.languageDetail);

        Bundle bundle = getIntent().getExtras();

        String cover = bundle.getString("cover");
        String title = bundle.getString("title");
        String authors = bundle.getString("authors");
        String date = bundle.getString("date");
        String description = bundle.getString("description");
        String genre = bundle.getString("genre");
        String language = bundle.getString("language");

        titleDetail.setText(title);
        authorsDetail.setText(authors);
        descriptionDetail.setText(description);
        genreDetail.setText(genre);
        dateDetail.setText(date);
        languageDetail.setText(language);

        if (cover.isEmpty()) {
            Glide.with(this).load(getDrawable(R.drawable.ic_baseline_image_not_supported_120)).into(coverDetail);
        } else {
            Glide.with(this).load(cover).into(coverDetail);
        }

        addToBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(BookDetailActivity.this);
                myDB.addBook(title, authors, date, cover);
                myDB.close();
            }
        });

    }
}