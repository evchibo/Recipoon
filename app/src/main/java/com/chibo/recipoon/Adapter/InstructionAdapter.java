package com.chibo.recipoon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chibo.recipoon.R;
import com.chibo.recipoon.Model.Step;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {
    private List<Step> steps;
    private int rowLayout;
    private Context context;


    public InstructionAdapter(List<Step> steps, int rowLayout, Context context) {
        this.steps = steps;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.stepView.setText(steps.get(position).getStep());


    }

    @Override
    public int getItemCount() {
        if (steps!= null){
            return steps.size();
        }

        return 0;
    }

    public class InstructionViewHolder extends RecyclerView.ViewHolder {
        CardView instructionsLayout;
        TextView stepView;



        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            instructionsLayout = itemView.findViewById(R.id.instructionCard);
            stepView =  itemView.findViewById(R.id.step);
        }
    }
}
