package com.example.universalyogacoursework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditClass extends AppCompatActivity {
    public static final  String TEACHER = "teacher";
    public static final  String DATE = "date";
    public static final  String COMMENTS = "comments";

    public static final int CLASS_ID = 1;
    public static final int COURSE_ID = 2;
    private int classId;
    private int idCourse;

    private YogaCourse currentCourse;
    Button btnHome , btnUpdate;
    Spinner teacher;
    TextView date;
    EditText comments;
    DatabaseHelper db ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        btnHome = (Button) findViewById(R.id.btnHomeUpdate);
        btnUpdate = (Button) findViewById(R.id.btnUpdateClass);
        teacher = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        date = (TextView) findViewById(R.id.etTimeUpdated);
        comments = (EditText) findViewById(R.id.etCapacityUpdated);
        db = new DatabaseHelper(this);



        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String teacherUpdated = extras.getString(TEACHER);
            String dateUpdated = extras.getString(DATE);
            String commentsUpdated = extras.getString(COMMENTS);
            idCourse = extras.getInt(String.valueOf(COURSE_ID));
            classId = extras.getInt(String.valueOf(CLASS_ID));
            currentCourse = db.getCourseById(idCourse);
          //  Toast.makeText(this, "COURSE ID " + idCourse, Toast.LENGTH_SHORT).show();
          //  Toast.makeText(this, "CLASS ID " + classId, Toast.LENGTH_SHORT).show();

            if(teacherUpdated != null || dateUpdated != null  || commentsUpdated != null ){
                int positionTeacher = getSelectedTeacherPosition(teacherUpdated);
                teacher.setSelection(positionTeacher);
                date.setText(dateUpdated);
                comments.setText(commentsUpdated);

            }
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , YogaCourseDetails.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
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

            ((EditClass)getActivity()).updateSelectedDate(year, month, day);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new EditClass.DatePickerFragment();
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


    private int getSelectedTeacherPosition(String selectedTeacher) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) teacher.getAdapter();
        return adapter.getPosition(selectedTeacher);
    }


    private void updateData() {
        Spinner teacherText = (Spinner) findViewById(R.id.spDayOfWeekUpdated);
        TextView dateText = (TextView) findViewById(R.id.etTimeUpdated);
        EditText comments = (EditText) findViewById(R.id.etCapacityUpdated);

        String teacherSelected = teacherText.getSelectedItem().toString();
        String dateSelected = dateText.getText().toString();
        String commentsSelected = comments.getText().toString();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            // The date conversion to Day of the week is taken
            // from https://stackoverflow.com/questions/7651221/android-how-to-get-the-current-day-of-the-week-monday-etc-in-the-users-l
            Date newDate = dateFormat.parse(dateSelected);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            String selectedDay = dayFormat.format(newDate);

            if (!selectedDay.equals(currentCourse.getDayOfWeek())) {
                Toast.makeText(this, "Your date should match the day that you provided - " + currentCourse.getDayOfWeek(), Toast.LENGTH_SHORT).show();
            } else {
                YogaClass updatedClass = new YogaClass(teacherSelected, dateSelected, commentsSelected);

                db.updateClass(classId, updatedClass);
                Toast.makeText(EditClass.this, "Class Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditClass.this, YogaCourseDetails.class);
                startActivity(intent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}