package com.example.universalyogacoursework;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class YogaCourseAdapter extends RecyclerView.Adapter<YogaCourseAdapter.YogaClassViewHolder> {

    private ArrayList<YogaCourse> yogaCourses;
    private DatabaseHelper databaseHelper;
    FirebaseDatabase firebaseDatabase = null;


    public YogaCourseAdapter(ArrayList<YogaCourse> yogaCourses, DatabaseHelper databaseHelper){
        this.yogaCourses = yogaCourses;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        firebaseDatabase = FirebaseDatabase.getInstance("https://universalyoga-e002e-default-rtdb.europe-west1.firebasedatabase.app/");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yoga_class,parent,false);
        return new YogaClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaClassViewHolder holder , int position){
        holder.tvDay.setText(yogaCourses.get(position).getDayOfWeek());
        holder.tvTime.setText(yogaCourses.get(position).getTimeOfCourse());
        holder.tvCapacity.setText(yogaCourses.get(position).getCapacity());
        holder.tvDuration.setText(yogaCourses.get(position).getDuration());
        holder.tvPrice.setText(yogaCourses.get(position).getPrice());
        holder.tvType.setText(yogaCourses.get(position).getType());
        holder.tvDescription.setText(yogaCourses.get(position).getDescription());
        holder.tvLevel.setText(yogaCourses.get(position).getLevel());

        int courseId = yogaCourses.get(position).getId();

        holder.btnViewClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), ViewClassInstances.class);
                intent.putExtra("idCourseInstance" , courseId );
                v.getContext().startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteCourse(courseId );
                Toast.makeText(v.getContext(), "Course Deleted!", Toast.LENGTH_SHORT).show();
                yogaCourses.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateYogaClass.class);
                intent.putExtra("idCourseAdd" , courseId );
                v.getContext().startActivity(intent);
               // Toast.makeText(v.getContext(), "COURSE ID " + courseId , Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int courseId = yogaCourses.get(holder.getAdapterPosition()).getId();
                String day = holder.tvDay.getText().toString();
                String timeDay = holder.tvTime.getText().toString();
                String capacity = holder.tvCapacity.getText().toString();
                String duration = holder.tvDuration.getText().toString();
                String price = holder.tvPrice.getText().toString();
                String type = holder.tvType.getText().toString();
                String description = holder.tvDescription.getText().toString();
                String level = holder.tvLevel.getText().toString();

                int priceInt = Integer.parseInt(price);
                Intent intent = new Intent(v.getContext(), EditYogaCourse.class);
                intent.putExtra(EditYogaCourse.DAY, day);
                intent.putExtra(EditYogaCourse.TIME, timeDay);
                intent.putExtra(EditYogaCourse.PRICE, priceInt);
                intent.putExtra(EditYogaCourse.CAPACITY, capacity);
                intent.putExtra(EditYogaCourse.DURATION, duration);
                intent.putExtra(EditYogaCourse.TYPE, type);
                intent.putExtra(EditYogaCourse.LEVEL, level);
                intent.putExtra(EditYogaCourse.DESCRIPTION, description);
                intent.putExtra(String.valueOf(EditYogaCourse.COURSE_ID), courseId);

                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount(){
        return  yogaCourses.size();
    }


    public static class YogaClassViewHolder extends RecyclerView.ViewHolder{

        public TextView tvDay , tvTime , tvCapacity,tvDuration,tvPrice , tvType,tvDescription,tvLevel;
            public Button btnDelete , btnEdit , btnViewClasses , btnAddClass;

        public YogaClassViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setClickable(true);
            tvDay = itemView.findViewById(R.id.spDayOfWeekUpdated);
            tvTime = itemView.findViewById(R.id.etTimeUpdated);
            tvCapacity = itemView.findViewById(R.id.etDurationUpdated);
            tvDuration = itemView.findViewById(R.id.etPriceUpdated);
            tvPrice = itemView.findViewById(R.id.etCapacityUpdated);
            tvType = itemView.findViewById(R.id.spTypeOfClassUpdated);
            tvDescription = itemView.findViewById(R.id.spLevelUpdated);
            tvLevel = itemView.findViewById(R.id.etDescriptionUpdated);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnViewClasses = itemView.findViewById(R.id.viewClassesFromACourse);
            btnAddClass = itemView.findViewById(R.id.btnAddClass);
        }
    }
}
