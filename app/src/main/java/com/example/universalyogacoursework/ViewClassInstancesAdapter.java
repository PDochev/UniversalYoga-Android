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

import java.util.ArrayList;

public class ViewClassInstancesAdapter extends RecyclerView.Adapter<ViewClassInstancesAdapter.YogaClassViewHolder>  {

    private ArrayList<YogaClass> yogaClasses;
    private DatabaseHelper databaseHelper;
    private int id_Course;

    public ViewClassInstancesAdapter (ArrayList<YogaClass> yogaClasses , DatabaseHelper databaseHelper , int id_Course){
        this.yogaClasses = yogaClasses;
        this.databaseHelper = databaseHelper;
        this.id_Course = id_Course;
    }

    @NonNull
    @Override
    public ViewClassInstancesAdapter .YogaClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yoga_class_instance,parent,false);
        return new ViewClassInstancesAdapter .YogaClassViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewClassInstancesAdapter.YogaClassViewHolder holder , int position){
        holder.tvTeacher.setText(yogaClasses.get(position).getTeacher());
        holder.tvDate.setText(yogaClasses.get(position).getDate());
        holder.tvComments.setText(yogaClasses.get(position).getComments());


        int classId = yogaClasses.get(position).getId();


        holder.btnDeleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteClass(classId);
                Toast.makeText(v.getContext(), "Class Deleted!", Toast.LENGTH_SHORT).show();
                yogaClasses.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        holder.btnEditClassInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int class_ID = yogaClasses.get(holder.getAdapterPosition()).getId();

                String teacher = holder.tvTeacher.getText().toString();
                String date = holder.tvDate.getText().toString();
                String comments = holder.tvComments.getText().toString();

                Intent intent = new Intent(v.getContext(), EditClass.class);
                intent.putExtra(EditClass.TEACHER, teacher);
                intent.putExtra(EditClass.DATE, date);
                intent.putExtra(EditClass.COMMENTS, comments);
                intent.putExtra(String.valueOf(EditClass.CLASS_ID), class_ID);
                intent.putExtra(String.valueOf(EditClass.COURSE_ID), id_Course);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount(){
        return  yogaClasses.size();
    }


    public static class YogaClassViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTeacher , tvDate , tvComments ;
        public Button btnDeleteClass , btnEditClassInstance ;


        public YogaClassViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setClickable(true);
            tvTeacher = itemView.findViewById(R.id.spDayOfWeekUpdated);
            tvDate = itemView.findViewById(R.id.etTimeUpdated);
            tvComments = itemView.findViewById(R.id.etCapacityUpdated);
            btnDeleteClass = itemView.findViewById(R.id.btnUpdateClass);
            btnEditClassInstance = itemView.findViewById(R.id.btnHomeUpdate);
        }
    }
}
