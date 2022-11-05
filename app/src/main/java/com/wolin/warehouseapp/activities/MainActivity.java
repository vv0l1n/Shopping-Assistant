package com.wolin.warehouseapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.other.MainAdapter;
import com.wolin.warehouseapp.other.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        List<Product> testItems = new ArrayList<Product>();
        //testItems.add(new Product("Kasza gryczana", 10,true, new SimpleDateFormat("19-12-2001"), "12"));
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MainAdapter(getApplicationContext(), testItems));

    }
}