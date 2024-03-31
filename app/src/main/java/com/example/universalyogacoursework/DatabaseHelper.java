package com.example.universalyogacoursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String COURSE_TABLE = "yoga_course";
    public static final String ID_COURSE = "course_id";
    public static final String DAY = "day";
    public static final String TIME = "time";
    public static final String CAPACITY = "capacity";
    public static final String DURATION = "duration";
    public static final String PRICE = "price";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final String LEVEL = "level";

    private static final String CLASS_TABLE = "yoga_class";
    public static final String ID_CLASS = "class_id";
    public static final String ID_COURSE_KEY = "course_id_Key";
    public static final String TEACHER = "teacher";
    public static final String DATE = "date";
    public static final String COMMENTS = "comments";




    private SQLiteDatabase database;

    private static final String COURSE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            COURSE_TABLE ,ID_COURSE,DAY,TIME,CAPACITY,DURATION,PRICE,TYPE,DESCRIPTION,LEVEL
    );


    private static final String CLASS_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "   %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   %s INTEGER, " +
                    "   %s TEXT, " +
                    "   %s TEXT, " +
                    "   %s TEXT)",
            CLASS_TABLE, ID_CLASS, ID_COURSE_KEY, TEACHER,DATE, COMMENTS
    );



    public DatabaseHelper(Context context){
        super(context,COURSE_TABLE  , null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COURSE_CREATE);
        db.execSQL(CLASS_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE );
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE);

        Log.w(this.getClass().getName() , COURSE_TABLE  + " database upgrade to version " +
                newVersion + " - old data lost");
    }

    public long insertCourseDetails(YogaCourse y){
        ContentValues rowValues = new ContentValues();
        rowValues.put(DAY, y.getDayOfWeek());
        rowValues.put(TIME , y.getTimeOfCourse());
        rowValues.put(CAPACITY , y.getCapacity());
        rowValues.put(DURATION , y.getDuration());
        rowValues.put(PRICE , y.getPrice());
        rowValues.put(TYPE , y.getType());
        rowValues.put(DESCRIPTION , y.getDescription());
        rowValues.put(LEVEL, y.getLevel());

        long insertedId = database.insertOrThrow(COURSE_TABLE , null, rowValues);
        Log.d("InsertDetails", "Inserted row ID: " + insertedId);
        return insertedId;
    }

    public long insertClassDetails(YogaClass y , int courseID){
        ContentValues rowValues = new ContentValues();
        rowValues.put(ID_COURSE_KEY, courseID);
        rowValues.put(TEACHER, y.getTeacher());
        rowValues.put(DATE , y.getDate());
        rowValues.put(COMMENTS, y.getComments());

        return database.insertOrThrow(CLASS_TABLE , null , rowValues);
    }


    public void deleteCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COURSE_TABLE , ID_COURSE + " = ?", new String[]{String.valueOf(courseId)});
        db.delete(CLASS_TABLE, ID_COURSE_KEY+ " = ?", new String[]{String.valueOf(courseId)});
    }

    public  void deleteClass(int classId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CLASS_TABLE, ID_CLASS + " = ?", new String[]{String.valueOf(classId)});
    }


    public void updateCourse(int courseId , YogaCourse updatedClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DAY, updatedClass.getDayOfWeek());
        values.put(TIME, updatedClass.getTimeOfCourse());
        values.put(CAPACITY, updatedClass.getCapacity());
        values.put(DURATION, updatedClass.getDuration());
        values.put(PRICE, updatedClass.getPrice());
        values.put(TYPE, updatedClass.getType());
        values.put(DESCRIPTION, updatedClass.getDescription());
        values.put(LEVEL, updatedClass.getLevel());

        db.update(COURSE_TABLE , values, ID_COURSE + " = ?",
                new String[]{String.valueOf(courseId)});
    }

    public void updateClass(int classId , YogaClass updatedClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEACHER, updatedClass.getTeacher());
        values.put(DATE, updatedClass.getDate());
        values.put(COMMENTS, updatedClass.getComments());


        db.update( CLASS_TABLE , values, ID_CLASS + " = ?",
                new String[]{String.valueOf(classId)});
    }



    public ArrayList<YogaCourse> getCourses(){
        Cursor results = database.query("yoga_course" , new String[]{"course_id" , "day", "time", "capacity", "duration" , "price" , "type" , "description" , "level"},
                null,null,null,null,"day");

        ArrayList<YogaCourse> listClasses = new ArrayList<>();

        results.moveToFirst();
        while (!results.isAfterLast()){
            int id = results.getInt(0);
            String day = results.getString(1);
            String time = results.getString(2);
            String capacity = results.getString(3);
            String duration = results.getString(4);
            String price = results.getString(5);
            String type = results.getString(6);
            String description = results.getString(7);
            String level = results.getString(8);


            listClasses.add(new YogaCourse(id,day,time,capacity,duration,price,type,description,level));
            results.moveToNext();
        }
        return listClasses;
    }



    // I used ChatGPT for this query since I was a bit confused how to query this from the database.
    // I had to join two tables abd query the ID_COURSE
    // from the COURSE_TABLE and ID_COURSE_KEY from the Class table
    public ArrayList<YogaClass> getClassesByCourseId(int courseId) {
        String query = "SELECT " + CLASS_TABLE + ".*, " + COURSE_TABLE + ".*" +
                " FROM " + CLASS_TABLE +
                " INNER JOIN " + COURSE_TABLE +
                " ON " + COURSE_TABLE + "." + ID_COURSE + " = " + CLASS_TABLE + "." + ID_COURSE_KEY +
                " WHERE " + CLASS_TABLE + "." + ID_COURSE_KEY + " = ?";
        Cursor results = database.rawQuery(query, new String[]{String.valueOf(courseId)});

        ArrayList<YogaClass> courses = new ArrayList<>();

        if (results.moveToFirst()) {
            do {

                int id = results.getInt(0);
                String teacher = results.getString(2);
                String date = results.getString(3);
                String comments = results.getString(4);

                YogaClass yogaClass = new YogaClass(id, teacher, date, comments);
                courses.add(yogaClass);
            } while (results.moveToNext());
        }

        results.close();
        return courses;
    }



    public ArrayList<YogaClass> searchClassesByTeacherAndCourseSelected(String teacherName, int courseId) {
        ArrayList<YogaClass> classes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID_COURSE_KEY, TEACHER, DATE, COMMENTS};

        // LIKE is a logical operator in SQL that allows you to match on similar values rather than exact ones
        String selection = TEACHER + " LIKE ? AND " + ID_COURSE_KEY + " = ?";

        // Using % to perform a partial match
        // Partial matches are valuable if you don't know the exact form of the string for which you're searching.
        // You can also use partial matches to retrieve multiple rows that contain similar strings in one of the table's columns.
        // https://www.dummies.com/article/technology/programming-web-design/sql/how-to-use-like-and-not-like-predicates-in-sql-statements-160787/
        String[] selectionArgs = {"%" + teacherName + "%", String.valueOf(courseId)};

        Cursor cursor = db.query(CLASS_TABLE, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String teacher = cursor.getString(1);
                String date = cursor.getString(2);
                String comments = cursor.getString(3);

                YogaClass course = new YogaClass(id, teacher, date, comments);
                classes.add(course);
            } while (cursor.moveToNext());

        }
        return classes;
    }


    public ArrayList<YogaClass> searchClassesByTeacher(String teacherName) {
        ArrayList<YogaClass> classes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID_CLASS, TEACHER, DATE, COMMENTS};

        String selection = TEACHER + " LIKE ?";

        String[] selectionArgs = {"%" + teacherName + "%"};

        Cursor cursor = db.query(CLASS_TABLE, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String teacher = cursor.getString(1);
                String date = cursor.getString(2);
                String comments = cursor.getString(3);

                YogaClass course = new YogaClass(id, teacher, date, comments);
                classes.add(course);
            } while (cursor.moveToNext());
        }

        return classes;
    }


    public YogaCourse getCourseById(int idCourse) {
        Cursor results = database.query("yoga_course",
                new String[]{"course_id", "day", "time", "capacity", "duration", "price", "type", "description", "level"},
                "course_id = ?",
                new String[]{String.valueOf(idCourse)},
                null, null, null);

        YogaCourse yogaCourse = null;
        if (results.moveToFirst()) {
            int id = results.getInt(0);
            String day = results.getString(1);
            String time = results.getString(2);
            String capacity = results.getString(3);
            String duration = results.getString(4);
            String price = results.getString(5);
            String type = results.getString(6);
            String description = results.getString(7);
            String level = results.getString(8);

            yogaCourse = new YogaCourse(idCourse, day, time, capacity, duration, price, type, description, level);
            System.out.println(yogaCourse);
        }else {
            Log.d("Debug", "No entry found for idCourse: " + idCourse);

        }

        results.close();
        return yogaCourse;
    }



    public ArrayList<YogaClass> getClasses(){
        Cursor results = database.query("yoga_class" , new String[]{"class_id" , "course_id_Key", "teacher", "date", "comments"},
                null,null,null,null,"teacher");

        ArrayList<YogaClass> listClasses = new ArrayList<>();

        results.moveToFirst();
        while (!results.isAfterLast()){
            int idIndex = results.getColumnIndex(ID_CLASS);
            int id = results.getInt(idIndex);

            int idCourse_Index = results.getColumnIndex(ID_COURSE_KEY);
            int idCourse = results.getInt(idCourse_Index);

            int teacherIndex = results.getColumnIndex(TEACHER);
            String teacher = results.getString(teacherIndex);

            int dateIndex = results.getColumnIndex(DATE);
            String date = results.getString(dateIndex);

            int commentsIndex = results.getColumnIndex(COMMENTS);
            String comments = results.getString(commentsIndex);

            listClasses.add(new YogaClass(id,idCourse, teacher,date,comments));
            results.moveToNext();
        }
        return listClasses;
    }

}
