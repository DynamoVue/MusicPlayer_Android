package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.PlaylistActivity;
import com.example.musicapp.Entity.Categories;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryByThemeAdapter extends RecyclerView.Adapter<CategoryByThemeAdapter.ViewHolder> {

    Context context;
    ArrayList<Categories> categoriesArrayList;

    public CategoryByThemeAdapter(Context context, ArrayList<Categories> categoriesArrayList) {
        this.context = context;
        this.categoriesArrayList = categoriesArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_category_by_theme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryByThemeAdapter.ViewHolder holder, int position) {
        Categories category = categoriesArrayList.get(position);
        Picasso.get().load(category.getImageCategory()).into(holder.imageScreen);
        holder.txtTheme.setText(category.getNameCategory());
    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageScreen;
        TextView txtTheme;
        public ViewHolder(View itemView) {
            super(itemView);
            imageScreen = itemView.findViewById(R.id.imageViewCategoryByTheme);
            txtTheme = itemView.findViewById(R.id.textViewCategoryByTheme);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlaylistActivity.class);
                    intent.putExtra("idTheLoai", categoriesArrayList.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
