package com.example.universalyogacoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class YogaClassDetails extends AppCompatActivity {

    RecyclerView.Adapter myPersonalAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView emptyClassesMessageText;
    Button createClassButton;

    DatabaseHelper db ;
    ArrayList<YogaClass> details;
    EditText etSearchTeacher;
    Spinner spTeacher;
    Button btnSearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_course_details);
        emptyClassesMessageText = (TextView) findViewById(R.id.tvEmptyClasses);
        createClassButton = (Button) findViewById(R.id.btnDetailsClass);
        createClassButton.setVisibility(View.INVISIBLE);
        emptyClassesMessageText.setVisibility(View.INVISIBLE);
        spTeacher = (Spinner) findViewById(R.id.spTeacherSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);


        db = new DatabaseHelper(this);
        details = db.getClasses();

        createClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateYogaCourse.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.rvYogaClassesDetails);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (details.isEmpty()) {
            emptyClassesMessageText.setText(R.string.emptyClass);
            createClassButton.setVisibility(View.VISIBLE);
            emptyClassesMessageText.setVisibility(View.VISIBLE);

        }

        myPersonalAdapter = new YogaClassAdapter(details, db);
        recyclerView.setAdapter(myPersonalAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherName = spTeacher.getSelectedItem().toString().trim();

                if (teacherName.equals("All")) {
                    ArrayList<YogaClass> allClasses = db.getClasses();
                    myPersonalAdapter = new YogaClassAdapter(allClasses, db);
                    recyclerView.setAdapter(myPersonalAdapter);

                    emptyClassesMessageText.setVisibility(View.GONE);
                } else if (!teacherName.isEmpty()) {
                    ArrayList<YogaClass> searchResults = db.searchClassesByTeacher(teacherName);
                    myPersonalAdapter = new YogaClassAdapter(searchResults, db);
                    recyclerView.setAdapter(myPersonalAdapter);

                    if (searchResults.isEmpty()) {
                        emptyClassesMessageText.setText("No classes found for " + teacherName);
                        emptyClassesMessageText.setVisibility(View.VISIBLE);
                    } else {
                        emptyClassesMessageText.setVisibility(View.GONE);
                    }
                } else {

                    Toast.makeText(YogaClassDetails.this, "Please select a teacher's name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}