package com.example.keeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HighlightsActivity extends AppCompatActivity implements SensorEventListener {

    private FloatingActionButton addHighlightFAB, addImageFAB, addTextFAB;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;

    private boolean clicked;
    private String id;

    private SensorManager sensorManager;
    private Sensor sensor;
    private float lightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

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

        if (bundle != null) {
            id = bundle.getString("id");
        }

        addHighlightFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(clicked);
                setAnimation(clicked);
                clicked = !clicked;
            }
        });

        addTextFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTextHighlightActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("id", id);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        addImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sensor
                if (lightValue < 800.0) {
                    openDialog();
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValue = sensorEvent.values[0];
            Toast.makeText(this, "Lux "+lightValue, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void openDialog() {
        LowLightDialog lowLightDialog = new LowLightDialog();
        lowLightDialog.show(getSupportFragmentManager(), "lowLightDialog");
    }

}