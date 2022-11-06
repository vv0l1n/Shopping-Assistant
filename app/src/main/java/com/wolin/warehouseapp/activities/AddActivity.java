package com.wolin.warehouseapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wolin.warehouseapp.R;

public class AddActivity extends AppCompatActivity {

    ImageView productImageAdd;
    EditText productNameAdd;
    EditText countAdd;
    EditText maxPriceAdd;
    EditText noteAdd;
    ImageView shopLogoAdd;
    Button addActivityAddButton;
    Button addActivityCancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_activity);
        productImageAdd = findViewById(R.id.productImageAdd);
        productNameAdd = findViewById(R.id.productNameAdd);
        countAdd = findViewById(R.id.countAdd);
        maxPriceAdd = findViewById(R.id.maxPriceAdd);
        noteAdd = findViewById(R.id.noteAdd);
        shopLogoAdd = findViewById(R.id.shopLogoAdd);
        addActivityAddButton = findViewById(R.id.addActivityAddButton);
        addActivityCancelButton = findViewById(R.id.addActivityCancelButton);
    }

    private void onProductImageClick() {

    }

    private void onShopLogoClick() {

    }

    private void onAddActivityAddButtonClick() {

    }

    private void onAddActivityCancelButtonClick() {
        Intent addActivityCancelIntent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(addActivityCancelIntent);
    }

}