package com.example.universalyogacoursework;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateYogaCourse extends AppCompatActivity {

    Button btnHome, btnCreateClass;
    Spinner spDayOfWeek, spType, spLevel;
    EditText timeCourse, capacityPeople, durationClass, price, description;

    FirebaseDatabase firebaseDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_yoga_class);
        btnHome = (Button) findViewById(R.id.btnCourses);
        btnCreateClass = (Button) findViewById(R.id.btnUpdateCourse);
        spDayOfWeek = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        spType = (Spinner) findViewById(R.id.spTypeOfClassUpdated);
        spLevel = (Spinner) findViewById(R.id.spLevelUpdated);
        timeCourse = (EditText) findViewById(R.id.etTimeUpdated);
        capacityPeople = (EditText) findViewById(R.id.etCapacityUpdated);
        durationClass = (EditText) findViewById(R.id.etDurationUpdated);
        price = (EditText) findViewById(R.id.etPriceUpdated);
        description = (EditText) findViewById(R.id.etDescriptionUpdated);


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });
    }



    private void getInputs() {

        String day = spDayOfWeek.getSelectedItem().toString(),
                timeOfCourse = timeCourse.getText().toString(),
                capacity = capacityPeople.getText().toString(),
                duration = durationClass.getText().toString(),
                priceCourse = price.getText().toString(),
                type = spType.getSelectedItem().toString(),
                descriptionClass = description.getText().toString(),
                levelClass = spLevel.getSelectedItem().toString();

        String[] timeOfCourseArray = timeOfCourse.split(":");

        if (timeOfCourse.isEmpty()) {
            Toast.makeText(CreateYogaCourse.this, "Please , fill the time field!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timeOfCourseArray.length != 2) {
            Toast.makeText(CreateYogaCourse.this, "Invalid time format. Use HH:mm", Toast.LENGTH_SHORT).show();
            return;
        }
        if (capacity.isEmpty()) {
            Toast.makeText(CreateYogaCourse.this, "Please , fill the capacity field!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (duration.isEmpty()) {
            Toast.makeText(CreateYogaCourse.this, "Please , fill the duration field!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (priceCourse.isEmpty()) {
            Toast.makeText(CreateYogaCourse.this, "Please , fill the price field!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int Hour = Integer.parseInt(timeOfCourseArray[0]);
            int Minute = Integer.parseInt(timeOfCourseArray[1]);
        } catch (NumberFormatException e) {
            Toast.makeText(CreateYogaCourse.this, "Invalid time format", Toast.LENGTH_SHORT).show();
            return;

        }

        displayConfirmAlert(day, timeOfCourse, capacity, duration, priceCourse, type, descriptionClass, levelClass);

    }


    private void displayConfirmAlert(String day, String time, String capacity, String duration, String price, String type, String description, String level) {

        new AlertDialog.Builder(this).setTitle("Details entered").setMessage(
                "Details entered:\n" + "Day of the week: " + day + "\n" + "Time of course: " + time + "\n" + "Capacity: " + capacity + " people" + "\n" + "Duration (minutes): "
                        + duration + " minutes" + "\n" + "Price(£): " + price + "£" + "\n" + "Type of class: " + type + "\n" + "Description: " + description + "\n" + "Level: " + level).setNeutralButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
                Toast.makeText(CreateYogaCourse.this, "Course created!", Toast.LENGTH_SHORT).show();
            }
        }).show();

    }


    private void saveData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Spinner dayText = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        EditText timeText = (EditText) findViewById(R.id.etTimeUpdated);
        EditText capacityText = (EditText) findViewById(R.id.etCapacityUpdated);
        EditText durationText = (EditText) findViewById(R.id.etDurationUpdated);
        EditText priceText = (EditText) findViewById(R.id.etPriceUpdated);
        Spinner typeText = (Spinner) findViewById(R.id.spTypeOfClassUpdated);
        EditText descriptionText = (EditText) findViewById(R.id.etDescriptionUpdated);
        Spinner levelText = (Spinner) findViewById(R.id.spLevelUpdated);


        String day = dayText.getSelectedItem().toString(),
                timeOfCourse = timeText.getText().toString(),
                capacity = capacityText.getText().toString(),
                duration = durationText.getText().toString(),
                priceCourse = priceText.getText().toString(),
                type = typeText.getSelectedItem().toString(),
                descriptionClass = descriptionText.getText().toString(),
                levelClass = levelText.getSelectedItem().toString();


            YogaCourse y = new YogaCourse(day, timeOfCourse, capacity, duration, priceCourse, type, descriptionClass, levelClass);
            long yogaClassId = dbHelper.insertCourseDetails(y);
            Intent intent = new Intent(this, YogaCourseDetails.class);
            startActivity(intent);
           // Toast.makeText(this , "Yoga class has been created with id: " + yogaClassId , Toast.LENGTH_SHORT).show();

    }
}
