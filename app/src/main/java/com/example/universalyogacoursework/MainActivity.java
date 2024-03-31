package com.example.universalyogacoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button createCourse , viewCourse , btnPush;
    FirebaseDatabase firebaseDatabase = null;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createCourse = (Button) findViewById(R.id.btnCreateYogaCourse);
        viewCourse = (Button) findViewById(R.id.btnViewCourses);
        firebaseDatabase = FirebaseDatabase.getInstance("https://universalyoga-e002e-default-rtdb.europe-west1.firebasedatabase.app/");
        dbHelper = new DatabaseHelper(this);
        btnPush = (Button) findViewById(R.id.btnPushData);


        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , CreateYogaCourse.class);
                startActivity(intent);
            }
        });


        viewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , YogaCourseDetails.class);
                startActivity(intent);
            }
        });

        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushData();
            }
        });

    }

    // The showNetworkDialog code is taken from
    // this post: https://stackoverflow.com/questions/25685755/ask-user-to-connect-to-internet-or-quit-app-android
    private void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to Wi-Fi or mobile data")
                .setCancelable(false)
                .setNeutralButton("Connect to Mobile Data", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                })
                .setPositiveButton("Connect to Wi-Fi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private boolean checkNetworkStatus() {
        boolean isConnected = false;
        boolean wifiConnected;
        boolean mobileDataConnected;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileDataConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            isConnected = true;
//            if(wifiConnected){
//                Toast.makeText(CreateYogaCourse.this, "Wifi is connected", Toast.LENGTH_SHORT).show();
//            } else if (mobileDataConnected) {
//                Toast.makeText(CreateYogaCourse.this, "Mobile data is connected", Toast.LENGTH_SHORT).show();
//            }

        }
        return isConnected;
    }


    // I had a help from Syed Adil Ahmed for the pushData implementation.
    // He is a student from the COMP1661 - Mobile applications module.
    public void pushData(){
        try{
            ArrayList<YogaCourse> yogaCourseArrayList = dbHelper.getCourses();
            ArrayList<YogaClass> yogaClassArrayList = dbHelper.getClasses();
            deleteDataBeforeUpload();

            if(checkNetworkStatus()){
                for(int i = 0; i < yogaCourseArrayList.size(); i++){
                    addCourse(yogaCourseArrayList.get(i));
                }
                for(int i = 0; i < yogaClassArrayList.size(); i++){
                    addClass(yogaClassArrayList.get(i));
                }
                Toast.makeText(getApplicationContext(),"Pushed successfully" , Toast.LENGTH_SHORT).show();
            }else{
                showNetworkDialog();
                Toast.makeText(MainActivity.this , "No network connections available. Failed to upload data" , Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Failed to push data" , Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDataBeforeUpload(){
        try{
            DatabaseReference coursesRef = firebaseDatabase.getReference("courses");
            DatabaseReference classesRef = firebaseDatabase.getReference("classes");


            coursesRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this , "Deleted data successfully (Courses)" , Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this , "Failed to delete the Courses data" , Toast.LENGTH_SHORT).show();
                }
            });
            classesRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this , "Deleted data successfully (Classes)" , Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this , "Failed to delete the Classes data" , Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(MainActivity.this , "Failed to delete data" , Toast.LENGTH_SHORT).show();

        }
    }


    private boolean addCourse(YogaCourse yogaCourse){
        try{
            DatabaseReference myRef = firebaseDatabase.getReference();
            myRef.child("courses").push().setValue(yogaCourse).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this , "Upload data successfully " , Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this , "Failed to upload data " , Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean addClass(YogaClass yogaClass){
        try{
            DatabaseReference myRef = firebaseDatabase.getReference();
            myRef.child("classes").push().setValue(yogaClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this , "Upload data successfully " , Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this , "Failed to upload data " , Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }catch (Exception e){
            return false;
        }
    }


}