package com.example.musicapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.musicapp.Adapter.CategoryByThemeAdapter;
import com.example.musicapp.Entity.Categories;
import com.example.musicapp.Entity.Theme;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryByThemeActivity extends AppCompatActivity implements FirebaseReference {
    private Theme theme;
    private ImageView btnBack;
    RecyclerView recyclerViewTheLoaiTheoChuDe;
    Toolbar toolbarTheLoaiTheoChuDe;
    private ArrayList<Categories> categories;
    CategoryByThemeAdapter categoryByThemeAdapter;
    String idTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_by_theme);
        GetIntent();
        init();
        GetData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void init(){
        recyclerViewTheLoaiTheoChuDe = findViewById(R.id.recyclerViewTheLoaiTheoChuDe);
        toolbarTheLoaiTheoChuDe = findViewById(R.id.toolbarTheLoaiTheoChuDe);
//        setSupportActionBar(toolbarTheLoaiTheoChuDe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(theme.getNameTheme());
        toolbarTheLoaiTheoChuDe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void GetData(){
//        Query connectedPlaylist = DATABASE_REFERENCE_CATEGORY.child("idTheme");
        DATABASE_REFERENCE_CATEGORY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categories = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Categories category = dataSnapshot.getValue(Categories.class);
                    category.setIdTheme(dataSnapshot.getKey());
                    categories.add(category);
                }

                categoryByThemeAdapter = new CategoryByThemeAdapter(CategoryByThemeActivity.this,categories);
                recyclerViewTheLoaiTheoChuDe.setLayoutManager(new GridLayoutManager(CategoryByThemeActivity.this, 2) );
                recyclerViewTheLoaiTheoChuDe.setAdapter(categoryByThemeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("chude")){
            theme = (Theme) intent.getSerializableExtra("chude");
        }
    }
}