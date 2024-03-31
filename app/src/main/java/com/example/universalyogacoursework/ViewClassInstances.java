package com.example.universalyogacoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewClassInstances extends AppCompatActivity {
    RecyclerView.Adapter myPersonalAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView emptyClassesMessageText;


    DatabaseHelper db ;
    ArrayList<YogaClass> details;

    Spinner spTeacher;
    Button btnSearch , btnBack , btnGoCourses;
    private YogaCourse currentCourse;
    private int idCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_instances);
        emptyClassesMessageText = (TextView) findViewById(R.id.tvEmptyClassesInstance);
        btnBack= (Button) findViewById(R.id.btnBack);
        btnBack.setVisibility(View.INVISIBLE);
        emptyClassesMessageText.setVisibility(View.INVISIBLE);
        spTeacher = (Spinner) findViewById(R.id.spTeacherSearchInstance);
        btnSearch = (Button) findViewById(R.id.btnSearchINST);
        btnGoCourses = (Button) findViewById(R.id.btnGoCourses);

        db = new DatabaseHelper(this);

       btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YogaCourseDetails.class);
                startActivity(intent);
            }
        });

       btnGoCourses.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext() , YogaCourseDetails.class);
               startActivity(intent);
           }
       });

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("idCourseInstance")){
            idCourse = extras.getInt("idCourseInstance");
         //   currentCourse = db.getCourseById(idCourse);
         //   Toast.makeText(this, "ID COURSE: " + idCourse, Toast.LENGTH_SHORT).show();
        }


        details = db.getClassesByCourseId(idCourse);

        recyclerView = findViewById(R.id.rvYogaClassesDetailsINST);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (details.isEmpty()) {
            emptyClassesMessageText.setText(R.string.emptyClass);
            btnBack.setVisibility(View.VISIBLE);
            emptyClassesMessageText.setVisibility(View.VISIBLE);

        }

        myPersonalAdapter = new ViewClassInstancesAdapter(details, db , idCourse );
        recyclerView.setAdapter(myPersonalAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherName = spTeacher.getSelectedItem().toString().trim();

                if (teacherName.equals("All")) {

                    ArrayList<YogaClass> allClasses = db.getClassesByCourseId(idCourse);
                    myPersonalAdapter = new ViewClassInstancesAdapter(allClasses, db, idCourse);
                    recyclerView.setAdapter(myPersonalAdapter);
                    emptyClassesMessageText.setVisibility(View.GONE);
                } else if (!teacherName.isEmpty()) {

                    ArrayList<YogaClass> searchResults = db.searchClassesByTeacherAndCourseSelected(teacherName, idCourse);;
                    myPersonalAdapter = new ViewClassInstancesAdapter(searchResults, db , idCourse);
                    recyclerView.setAdapter(myPersonalAdapter);

                    if (searchResults.isEmpty()) {
                        emptyClassesMessageText.setText("No classes found for " + teacherName);
                        emptyClassesMessageText.setVisibility(View.VISIBLE);
                    } else {
                        emptyClassesMessageText.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(ViewClassInstances.this, "Please select a teacher's name", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    }
