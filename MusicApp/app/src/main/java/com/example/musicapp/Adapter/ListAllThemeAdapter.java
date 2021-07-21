package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.CategoryByThemeActivity;
import com.example.musicapp.Activity.ThemeScreenActivity;
import com.example.musicapp.Entity.Theme;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAllThemeAdapter extends RecyclerView.Adapter<ListAllThemeAdapter.ViewHolder> {

    Context context;
    ArrayList<Theme> mangchude;

    public ListAllThemeAdapter(Context context, ArrayList<Theme> mangchude) {
        this.context = context;
        this.mangchude = mangchude;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_theme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ListAllThemeAdapter.ViewHolder holder, int position) {
        Theme theme = mangchude.get(position);
        Picasso.get().load(theme.getImageTheme()).into(holder.imgchude);
    }

    @Override
    public int getItemCount() {
        return mangchude.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgchude;
        public ViewHolder(View itemView) {
            super(itemView);
            imgchude = itemView.findViewById(R.id.imageViewDongCacChuDe);
            imgchude.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CategoryByThemeActivity.class);
                    intent.putExtra("chude",mangchude.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
