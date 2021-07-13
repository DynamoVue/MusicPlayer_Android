package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.musicapp.Entity.Theme;
import com.example.musicapp.R;

public class CategoryByThemeActivity extends AppCompatActivity {
    private Theme theme;
    private ImageView btnBack;
    RecyclerView recyclerViewTheLoaiTheoChuDe;
    Toolbar toolbarTheLoaiTheoChuDe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_by_theme);
        btnBack = (ImageView) findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetIntent();
        init();
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

    private void GetIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("chude")){
            theme = (Theme) intent.getSerializableExtra("chude");
        }
    }
}