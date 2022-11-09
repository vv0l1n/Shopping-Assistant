package com.wolin.warehouseapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.utils.adapter.MainAdapter;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        List<Product> testItems = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MainAdapter(getApplicationContext(), testItems));

    }

    public void onMainActivityAddButtonClick(View view) {
        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(addIntent);
    }
}