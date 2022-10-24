package com.wolin.warehouseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        List<MainMenuItem> testItems = new ArrayList<MainMenuItem>();
        testItems.add(new MainMenuItem("Kasza gryczana", 10,"Zbożowe", "A", "12", "3", R.drawable.a));
        testItems.add(new MainMenuItem("Czipsy", 10,"Słone przekąski", "B", "4", "7", R.drawable.b));
        testItems.add(new MainMenuItem("Paluszki", 312,"Słone przekąski", "B", "12", "10", R.drawable.c));
        testItems.add(new MainMenuItem("Gofry", 1000,"Wypieki", "C", "5", "3", R.drawable.d));
        testItems.add(new MainMenuItem("Gofry", 500,"Wypieki", "C", "5", "4", R.drawable.d));
        testItems.add(new MainMenuItem("Cukierki", 78,"Słodkie przekąski", "D", "7", "8", R.drawable.e));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MainAdapter(getApplicationContext(), testItems));

    }
}