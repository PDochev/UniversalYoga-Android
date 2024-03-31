package com.example.universalyogacoursework;

public class YogaCourse {
    private String dayOfWeek;
    private String timeOfCourse;
    private String capacity;
    private String duration;
    private String price;
    private String type;
    private String description;
    private String level;
    private int id;

    public YogaCourse(String dayOfWeek, String timeOfCourse, String capacity, String duration, String price, String type, String description , String level){
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.description = description;
        this.level = level;
    }

    public YogaCourse(int id , String dayOfWeek, String timeOfCourse, String capacity, String duration, String price, String type, String description , String level){
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.description = description;
        this.level = level;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getId(){return  id;}
    public void setId(int id){this.id = id;}
}
