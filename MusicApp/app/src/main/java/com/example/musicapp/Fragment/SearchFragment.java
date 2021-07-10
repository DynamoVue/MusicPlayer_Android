package com.example.musicapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;

public class SearchFragment extends Fragment {
    View view;
    Toolbar toolbarSearchSong;
    RecyclerView recyclerViewSearchSong;
    TextView txtNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbarSearchSong = view.findViewById(R.id.toolBarSearchSong);
        recyclerViewSearchSong = view.findViewById(R.id.recyclerVSearchSong);
        txtNoData = view.findViewById(R.id.tvNoDataSearchSong);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarSearchSong);
        toolbarSearchSong.setTitle("");

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch); //click vao chau nao
        SearchView searchView = (SearchView) menuItem.getActionView();
        //Handle event
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
