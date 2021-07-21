package com.example.musicapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.musicapp.Adapter.ListAllThemeAdapter;
import com.example.musicapp.Entity.Theme;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThemeScreenActivity extends AppCompatActivity implements FirebaseReference {
    private RecyclerView recyclerViewAllTheme;
    private Toolbar toolbarViewAllTheme;
    private ImageView btnBack;
    private ListAllThemeAdapter listAllThemeAdapter;
    private ArrayList<Theme> themes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_screen_activity);
        init();
        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init(){
        recyclerViewAllTheme = findViewById(R.id.recyclerViewAllTheme);
        toolbarViewAllTheme = findViewById(R.id.toolbarAllTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất cả chủ đề");
        toolbarViewAllTheme.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        DATABASE_REFERENCE_THEME.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                themes = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Theme theme = dataSnapshot.getValue(Theme.class);
                    theme.setIdTheme(dataSnapshot.getKey());
                    themes.add(theme);
                }
                listAllThemeAdapter = new ListAllThemeAdapter(ThemeScreenActivity.this, themes);
                recyclerViewAllTheme.setLayoutManager(new GridLayoutManager(ThemeScreenActivity.this, 1));
                recyclerViewAllTheme.setAdapter(listAllThemeAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

}