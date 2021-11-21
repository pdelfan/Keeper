package com.example.keeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HighlightsActivity extends AppCompatActivity {

    private FloatingActionButton addHighlightFAB, addImageFAB, addTextFAB;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;

    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addHighlightFAB = findViewById(R.id.fab_add_highlight);
        addHighlightFAB.setColorFilter(Color.WHITE);
        clicked = false;

        addImageFAB = findViewById(R.id.fab_highlight_camera);
        addTextFAB = findViewById(R.id.fab_highlight_text);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        addHighlightFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(clicked);
                setAnimation(clicked);
                clicked = !clicked;
            }
        });
    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            addImageFAB.setVisibility(View.VISIBLE);
            addTextFAB.setVisibility(View.VISIBLE);
        } else {
            addImageFAB.setVisibility(View.GONE);
            addTextFAB.setVisibility(View.GONE);
        }
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
            addImageFAB.startAnimation(fromBottom);
            addTextFAB.startAnimation(fromBottom);
            addHighlightFAB.startAnimation(rotateOpen);
        } else {
            addImageFAB.startAnimation(toBottom);
            addTextFAB.startAnimation(toBottom);
            addHighlightFAB.startAnimation(rotateClose);
        }
    }


}