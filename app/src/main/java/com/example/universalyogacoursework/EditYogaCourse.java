package com.example.universalyogacoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditYogaCourse extends AppCompatActivity {
    public static final  String DAY = "day";
    public static final  String TIME = "time";
    public static final  String CAPACITY = "capacity";
    public static final  String DURATION = "duration";
    public static final  String PRICE = "price";
    public static final  String TYPE = "type";
    public static final  String DESCRIPTION = "description";
    public static final  String LEVEL = "level";
    public static final int COURSE_ID = 1;
    private int classId;

    Button btnClasses , btnUpdateClass;
    Spinner spDayOfWeek , spType , spLevel;
    EditText timeCourse , capacityPeople , durationClass , price , description;

    DatabaseHelper db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_yoga_class);
        btnClasses = (Button) findViewById(R.id.btnCourses);
        btnUpdateClass = (Button) findViewById(R.id.btnUpdateCourse);
        spDayOfWeek = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        spType = (Spinner)findViewById(R.id.spTypeOfClassUpdated);
        spLevel = (Spinner)findViewById(R.id.spLevelUpdated);
        timeCourse = (EditText) findViewById(R.id.etTimeUpdated);
        capacityPeople = (EditText) findViewById(R.id.etCapacityUpdated);
        durationClass = (EditText) findViewById(R.id.etDurationUpdated);
        price = (EditText) findViewById(R.id.etPriceUpdated);
        description = (EditText) findViewById(R.id.etDescriptionUpdated);
        db = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String day = extras.getString(DAY);
            String time = extras.getString(TIME);
            int priceValue = extras.getInt(PRICE);
            String capacity = extras.getString(CAPACITY);
            String duration = extras.getString(DURATION);
            String type = extras.getString(TYPE);
            String level = extras.getString(LEVEL);
            String descriptionValue = extras.getString(DESCRIPTION);
            classId = extras.getInt(String.valueOf(COURSE_ID), -1);

            if(day != null || time != null  || capacity != null || duration != null || type != null || level != null || descriptionValue != null){
                int positionDay = getSelectedDayPosition(day); // This method converts day string to position in spinner
                int positionType = getSelectedTypePosition(type); // This method converts type string to position in spinner
                int positionLevel = getSelectedLevelPosition(level); // This method converts level string to position in spinner
                spDayOfWeek.setSelection(positionDay);
                timeCourse.setText(time);
                capacityPeople.setText(capacity);
                durationClass.setText(duration);
                price.setText(String.valueOf(priceValue));
                spType.setSelection(positionType);
                spLevel.setSelection(positionLevel);
                description.setText(descriptionValue);
            }
        }

        btnUpdateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                Toast.makeText(EditYogaCourse.this, "Course Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        btnClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , YogaCourseDetails.class);
                startActivity(i);
            }
        });
    }

    private void updateData(){
        Spinner dayText = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        EditText timeText = (EditText) findViewById(R.id.etTimeUpdated);
        EditText capacityText = (EditText) findViewById(R.id.etCapacityUpdated);
        EditText durationText = (EditText) findViewById(R.id.etDurationUpdated);
        EditText priceText = (EditText) findViewById(R.id.etPriceUpdated);
        Spinner typeText = (Spinner) findViewById(R.id.spTypeOfClassUpdated);
        EditText descriptionText = (EditText) findViewById(R.id.etDescriptionUpdated);
        Spinner levelText = (Spinner) findViewById(R.id.spLevelUpdated);

        String day = dayText.getSelectedItem().toString();
        String time = timeText.getText().toString();
        String capacity = capacityText.getText().toString();
        String duration = durationText.getText().toString();
        String priceValue = priceText.getText().toString();
        String type = typeText.getSelectedItem().toString();
        String descriptionValue = descriptionText.getText().toString();
        String level = levelText.getSelectedItem().toString();


        YogaCourse updatedClass = new YogaCourse(day, time, capacity, duration, priceValue, type, descriptionValue, level);


        db.updateCourse(classId, updatedClass);

        Intent intent = new Intent(EditYogaCourse.this, YogaCourseDetails.class);

        startActivity(intent);
        finish();
    }


    private int getSelectedDayPosition(String selectedDay) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spDayOfWeek.getAdapter();
        return adapter.getPosition(selectedDay);
    }

    private int getSelectedTypePosition(String selectedType) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spType.getAdapter();
        return adapter.getPosition(selectedType);
    }

    private int getSelectedLevelPosition(String selectedLevel) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spLevel.getAdapter();
        return adapter.getPosition(selectedLevel);
    }
}