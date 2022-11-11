package com.android.become_a_farmer;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.become_a_farmer.service.SearchService;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter rAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        recyclerView = (RecyclerView) findViewById(R.id.rv_lst);
        rAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(rAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String text = getIntent().getStringExtra("text");

        SearchService searchService = new SearchService(text, rAdapter);
        searchService.search();

    }
}
