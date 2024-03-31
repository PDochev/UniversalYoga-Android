package com.example.universalyogacoursework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class YogaClassAdapter extends RecyclerView.Adapter<YogaClassAdapter.YogaCourseViewHolder> {

    private ArrayList<YogaClass> yogaClasses;
    private DatabaseHelper databaseHelper;

    public YogaClassAdapter(ArrayList<YogaClass> yogaClasses , DatabaseHelper databaseHelper){
        this.yogaClasses = yogaClasses;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public YogaCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yoga_course,parent,false);
        return new YogaCourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YogaCourseViewHolder holder , int position){
        holder.tvTeacher.setText(yogaClasses.get(position).getTeacher());
        holder.tvDate.setText(yogaClasses.get(position).getDate());
        holder.tvComments.setText(yogaClasses.get(position).getComments());


        int courseId = yogaClasses.get(position).getId();

        holder.btnDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteClass(courseId);
                Toast.makeText(v.getContext(), "Class Deleted!", Toast.LENGTH_SHORT).show();
                yogaClasses.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount(){
        return  yogaClasses.size();
    }


    public static class YogaCourseViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTeacher , tvDate , tvComments ;
        public Button btnDeleteClass;


        public YogaCourseViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setClickable(true);
            tvTeacher = itemView.findViewById(R.id.tvTeacherNameDetails);
            tvDate = itemView.findViewById(R.id.tvDateDetails);
            tvComments = itemView.findViewById(R.id.tvCommentsDetails);
            btnDeleteClass = itemView.findViewById(R.id.btnDeleteClass);

        }
    }
}

