package com.chibo.recipoon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chibo.recipoon.R;
import com.chibo.recipoon.Model.Recipe;
import com.chibo.recipoon.RecipeDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecipeViewHolder>{

    public MyAdapter(List<Recipe> recipes, int rowLayout, Context context) {
        this.recipes = recipes;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    private List<Recipe> recipes;
    private int rowLayout;
    private Context context;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        holder.recipeTitle.setText(recipes.get(position).getTitle());
        holder.recipeReadyInMinutes.setText(recipes.get(position).getReadyInMinutes().toString());
        holder.recipeServings.setText(recipes.get(position).getServings().toString());
        Picasso.get().load(recipes.get(position).getImage()).resize(300,300).into(holder.imageView);
        holder.recipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("carview","onClick: clicked on "+ recipes.get(position).getTitle());

                Intent intent = new Intent(context, RecipeDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("title",recipes.get(position).getTitle());
                intent.putExtra("image",recipes.get(position).getImage());
                intent.putExtra("servings",recipes.get(position).getServings().toString());
                intent.putExtra("id",recipes.get(position).getId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipes != null){
            return recipes.size();
        }
        return 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        CardView recipeLayout;
        TextView recipeTitle;
        TextView recipeServings;
        TextView recipeReadyInMinutes;

        ImageView imageView;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeLayout = itemView.findViewById(R.id.card_layout);
            recipeTitle =  itemView.findViewById(R.id.tv_title);
            recipeServings = itemView.findViewById(R.id.tv_servings);
            recipeReadyInMinutes = itemView.findViewById(R.id.tv_readyInMinutes);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }
}
