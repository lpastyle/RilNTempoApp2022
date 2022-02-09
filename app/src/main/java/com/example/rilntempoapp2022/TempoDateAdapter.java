package com.example.rilntempoapp2022;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TempoDateAdapter extends RecyclerView.Adapter<TempoDateAdapter.ViewHolder> {

    List<TempoDate> tempoDates;

    public TempoDateAdapter(List<TempoDate> tempoDates) {
        this.tempoDates = tempoDates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tempo_date_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return tempoDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        FrameLayout colorFl;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           dateTv = itemView.findViewById(R.id.date_tv);
           colorFl = itemView.findViewById(R.id.color_fl);
       }
   }


}
