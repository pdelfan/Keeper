package com.example.keeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class HighlightsActivity extends AppCompatActivity implements SensorEventListener {

    private FloatingActionButton addHighlightFAB, addImageFAB, addTextFAB;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;

    private boolean clicked;
    private String id;

    private SensorManager sensorManager;
    private Sensor sensor;
    private float lightValue;

    MyDatabaseHelper myDB;
    ArrayList<Highlight> highlightsList;
    HighlightsAdapter highlightsAdapter;
    RecyclerView recyclerView;

    private TextView noHighlightLabel;
    private static final int REQUEST_CAMERA_CODE = 100;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


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

        addHighlightFAB.setOnClickListener(view -> {
            setVisibility(clicked);
            setAnimation(clicked);
            clicked = !clicked;
        });

        addTextFAB.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddTextHighlightActivity.class);

            Bundle bundle1 = new Bundle();
            bundle1.putString("id", id);

            intent.putExtras(bundle1);
            startActivity(intent);
        });


        if (ContextCompat.checkSelfPermission(HighlightsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HighlightsActivity.this, new String[] {
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        addImageFAB.setOnClickListener(view -> {
            if (lightValue < 800.0) {
                openDialog();
            } else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE);
            }
        });

        noHighlightLabel = findViewById(R.id.noHighlights);

        recyclerView = findViewById(R.id.highlightsRecyclerView);
        myDB = new MyDatabaseHelper(HighlightsActivity.this);
        updateAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapter();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void updateAdapter() {
        highlightsList = myDB.getAllHighlights(id);
        highlightsAdapter = new HighlightsAdapter(HighlightsActivity.this, highlightsList);
        recyclerView.setAdapter(highlightsAdapter);
        updateHighlightLabel(highlightsList);
    }

    public void updateHighlightLabel(ArrayList<Highlight> highlightsList) {
        if (highlightsList.size() < 1) {
            noHighlightLabel.setVisibility(View.VISIBLE);
        } else {
            noHighlightLabel.setVisibility(View.INVISIBLE);
        }
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
//            Toast.makeText(this, "Lux "+lightValue, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void openDialog() {
        LowLightDialog lowLightDialog = new LowLightDialog();
        lowLightDialog.show(getSupportFragmentManager(), "lowLightDialog");
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (myDB.deleteHighlight(highlightsList.get(position).getId())) {
                highlightsList.remove(position);
                highlightsAdapter.notifyItemRemoved(position);
                updateHighlightLabel(highlightsList);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(HighlightsActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };



    }