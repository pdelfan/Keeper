package com.example.keeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    SharedPreferences sharedPreferences;
    private Switch reminderSwitch;
    public static final String reminder = "remind";
    public static final String showReminderLabel = "reminderLabel";
    private LinearLayout reminderLayout;
    private TextView reminderLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reminderLayout = findViewById(R.id.reminderSettingsLayout);
        reminderLabel = findViewById(R.id.reminderLabel);

        // sharedPreferences
        reminderSwitch = findViewById(R.id.reminderSwitch);
        sharedPreferences = getSharedPreferences("User Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // switch
        if (sharedPreferences.contains(reminder)) {
            if (sharedPreferences.getBoolean(reminder, false) == true) {
                reminderLabel.setText(sharedPreferences.getString(showReminderLabel, ""));
                reminderLayout.setVisibility(View.VISIBLE);
                reminderSwitch.setChecked(true);
            }
        } else {
            reminderLayout.setVisibility(View.GONE);
        }

        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    editor.putBoolean(reminder, true);
                    reminderLayout.setVisibility(View.VISIBLE);
                } else {
                    editor.putBoolean(reminder, false);
                    editor.putString(showReminderLabel, "Time not set");
                    Intent intent = new Intent(SettingsActivity.this, AlarmReceiver.class);
                    intent.putExtra("notificationId", notificationId);
                    intent.putExtra("message", "Body");
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(alarmIntent);

                    reminderLabel.setText("Time not set");
                    reminderLayout.setVisibility(View.GONE);
                }
                editor.commit();
            }
        });


        // interval
        reminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });




    }

    private int notificationId = 1;

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (reminderSwitch.isChecked()) {
            reminderLabel.setText("Every day at: " + i + ":" + i1);

            Intent intent = new Intent(SettingsActivity.this, AlarmReceiver.class);
            intent.putExtra("notificationId", notificationId);
            intent.putExtra("message", "Body");

            PendingIntent alarmIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.cancel(alarmIntent);

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, i);
            startTime.set(Calendar.MINUTE, i1);
            startTime.set(Calendar.SECOND, 0);
            long alarmStartTime = startTime.getTimeInMillis();

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(showReminderLabel, "Every day at: " + i + ":" + i1);
            editor.commit();


        }
    }
}