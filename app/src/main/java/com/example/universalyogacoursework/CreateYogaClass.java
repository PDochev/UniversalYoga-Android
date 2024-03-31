package com.example.universalyogacoursework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateYogaClass extends AppCompatActivity {

    Spinner spTeacher;
    TextView selectedDate;
    EditText comments;
    Button btnCreateCourse , btnHome;

    String defaultText;
    private YogaCourse currentCourse;
    DatabaseHelper db ;
    FirebaseDatabase firebaseDatabase = null;
    public static final int CLASS_ID = 1;
    private int idCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);
        spTeacher = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        selectedDate = (TextView) findViewById(R.id.etTimeUpdated);
        comments = (EditText) findViewById(R.id.etCapacityUpdated);
        btnCreateCourse = (Button) findViewById(R.id.btnUpdateClass);
        btnHome = (Button) findViewById(R.id.btnHomeUpdate);
        defaultText = selectedDate.getText().toString();

        db = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null && extras.containsKey("idCourseAdd")){
            idCourse = extras.getInt("idCourseAdd");
            currentCourse = db.getCourseById(idCourse);
            System.out.println(idCourse);
          //  Toast.makeText(this, "ID COURSE EXTRA: " + idCourse, Toast.LENGTH_SHORT).show();
        }



        btnCreateCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , YogaCourseDetails.class);
                startActivity(intent);
            }
        });
    }




    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

            ((CreateYogaClass)getActivity()).updateSelectedDate(year, month, day);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void updateSelectedDate(int year, int month, int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        TextView selectDate = (TextView)findViewById(R.id.etTimeUpdated);
        selectDate.setText(date);
    }


    private void getInputs(){

        String teacher = spTeacher.getSelectedItem().toString(),
                dateSelected = selectedDate.getText().toString(),
                addComments = comments.getText().toString();

        if(dateSelected.equals(defaultText)){
            Toast.makeText(CreateYogaClass.this, "Please , select a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        displayConfirmAlert(dateSelected,teacher,addComments);

    }


    private void displayConfirmAlert(String date, String teacher, String comments) {


        new AlertDialog.Builder(this).setTitle("Details entered").setMessage(
                "Details entered:\n" + "Date: " + date + "\n" + "Teacher: " + teacher + "\n" + "Comments: " + comments  ).setNeutralButton("Back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
                Toast.makeText(CreateYogaClass.this, "Class created", Toast.LENGTH_SHORT).show();
            }
        }).show();

    }



    private void saveData() {
        try {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        String teacher = spTeacher.getSelectedItem().toString(),
                date = selectedDate.getText().toString(),
                commentsSelected = comments.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


            // The date conversion to Day of the week is taken
            // from https://stackoverflow.com/questions/7651221/android-how-to-get-the-current-day-of-the-week-monday-etc-in-the-users-l
            Date newDate = dateFormat.parse(date);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            String selectedDay = dayFormat.format(newDate);


            if (!selectedDay.equals(currentCourse.getDayOfWeek()))
            {
                Toast.makeText(this, "Your date should match the day that you provided - " + currentCourse.getDayOfWeek(), Toast.LENGTH_SHORT).show();
            }
            else {
                    YogaClass y = new YogaClass(teacher, date, commentsSelected);
                    long yogaClassId = dbHelper.insertClassDetails(y , idCourse);
                    Intent intent = new Intent(this, YogaCourseDetails.class);
                    startActivity(intent);
                  //  Toast.makeText(this, "Yoga class has been created with id: " + yogaClassId, Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }


}