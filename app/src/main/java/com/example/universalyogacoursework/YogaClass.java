package com.example.universalyogacoursework;

public class YogaClass {
    private String date;
    private  String comments;
    private  String teacher;
    private int id;
    private int id_course;

    public YogaClass(String teacher , String date , String comments ){
        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
    }
    public YogaClass(int id, String teacher , String date , String comments){
        this.id = id;

        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
    }
    public YogaClass(int id, int id_course, String teacher , String date , String comments){
        this.id = id;
        this.id_course = id_course;
        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_course() { return id_course;}
    public void setId_course(int id_course){ this.id_course = id_course;}
}
