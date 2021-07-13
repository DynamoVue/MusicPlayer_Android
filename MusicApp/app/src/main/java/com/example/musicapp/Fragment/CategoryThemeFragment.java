package com.example.musicapp.Fragment;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.Activity.ThemeScreenActivity;
import com.example.musicapp.Entity.Categories;
import com.example.musicapp.Entity.Theme;
import com.example.musicapp.R;
import com.example.musicapp.Service.FirebaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryThemeFragment extends Fragment implements FirebaseReference {

    View view;
    HorizontalScrollView horizontalScrollView;
    TextView txtXemThemThemeAndCategory;
    ArrayList<Categories> categories;
    ArrayList<Theme> themes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_theme, container, false);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        txtXemThemThemeAndCategory = view.findViewById(R.id.textViewXemThem);
        txtXemThemThemeAndCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemeScreenActivity.class);
                startActivity(intent);
            }
        });
        GetData();
//        GetData1();
        return view;
    }
//    private void GetData1(){
//        DATABASE_REFERENCE_CATEGORY.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                categories = new ArrayList<>();
//                for(DataSnapshot item : dataSnapshot.getChildren()){
//                    Categories category = item.getValue(Categories.class);
//                    category.setIdCategory(dataSnapshot.getKey());
//                    categories.add(category);
//                }
//
//                LinearLayout linearLayout = new LinearLayout(getActivity());
//                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(580, 250);
//                layoutParams.setMargins(10,20,10,30);
//
//
//                horizontalScrollView.addView(linearLayout);
//            }
//
//            @Override
//            public void onCancelled( DatabaseError error) {
//
//            }
//        });
//    }
    private void GetData(){
        DATABASE_REFERENCE_THEME.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                themes = new ArrayList<>();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Theme theme = item.getValue(Theme.class);
                    theme.setIdTheme(dataSnapshot.getKey());
                    themes.add(theme);
                }

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(580, 250);
                layoutParams.setMargins(10,20,10,30);

                for (int i = 0; i < themes.size(); i++) {
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if(themes.get(i).getImageTheme() != null){
                        Picasso.get().load(themes.get(i).getImageTheme()).into(imageView);
                    }
                    cardView.setLayoutParams(layoutParams);
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);
                }

//                for (int j = 0; j < themes.size(); j++) {
//                    CardView cardView = new CardView(getActivity());
//                    cardView.setRadius(10);
//                    ImageView imageView = new ImageView(getActivity());
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    if(categories.get(j).getImageCategory() != null){
//                        Picasso.get().load(categories.get(j).getImageCategory()).into(imageView);
//                    }
//                    cardView.setLayoutParams(layoutParams);
//                    cardView.addView(imageView);
//                    linearLayout.addView(cardView);
//                }
            horizontalScrollView.addView(linearLayout);


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

}