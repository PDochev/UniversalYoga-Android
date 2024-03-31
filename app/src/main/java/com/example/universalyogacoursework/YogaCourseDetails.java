package com.example.universalyogacoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class YogaCourseDetails extends AppCompatActivity {

    RecyclerView.Adapter myPersonalAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView emptyCourseMessage;
    Button createClass ,   btnCreateCourse;
    ImageButton btnHome;
    DatabaseHelper db ;
    ArrayList<YogaCourse> details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga_class_details);
        emptyCourseMessage = (TextView) findViewById(R.id.tvEmptyCourses);
        createClass = (Button) findViewById(R.id.btnDetailsCreate);
        createClass.setVisibility(View.INVISIBLE);
        emptyCourseMessage.setVisibility(View.INVISIBLE);
        btnHome = (ImageButton) findViewById(R.id.btnGoHome);
        btnCreateCourse = (Button) findViewById(R.id.btnCreateACourse);

        db = new DatabaseHelper(this);
        details = db.getCourses();


        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , CreateYogaCourse.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateYogaCourse.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.rvYogaClasses);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(details.isEmpty()){
            emptyCourseMessage.setText(R.string.emptyCourse);
            createClass.setVisibility(View.VISIBLE);
        }

        myPersonalAdapter = new YogaCourseAdapter(details ,db);
        recyclerView.setAdapter(myPersonalAdapter);

    }

}