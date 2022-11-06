package com.wolin.warehouseapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wolin.warehouseapp.BuildConfig;
import com.wolin.warehouseapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class AddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_ID = 234;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;

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

        productImageAdd.setImageResource(R.drawable.press_to_add_product_image);
        shopLogoAdd.setImageResource(R.drawable.noneshop);

        if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {
                    Manifest.permission.CAMERA
            }, 100);
        }

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                if(result.getData() != null) {
                                    productImageAdd.setImageBitmap((Bitmap) result.getData().getExtras().get("data"));
                                    System.out.println(result.getData() + "niger");
                                }else {
                                    System.out.println("bład kamery");
                                }
                            }
                    }
                });


        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        productImageAdd.setImageURI(result.getData().getData());
                        productImageAdd.setMaxWidth(130);
                        productImageAdd.setMinimumWidth(130);
                        productImageAdd.setMaxHeight(130);
                        productImageAdd.setMinimumHeight(130);
                    } else {
                        System.out.println("błąd galerii");
                    }
                });
    }

    public void onProductImageClick(View view) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo"))
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null) {
                    cameraActivityResultLauncher.launch(intent);
                } else {
                    System.out.println("!!!!!!!!!!!!!!!!");
                }
            }
            else if (options[item].equals("Choose from Gallery"))
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(intent);
            }
            else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void onShopLogoClick(View view) {

    }

    public void onAddActivityAddButtonClick(View view) {

    }

    public void onAddActivityCancelButtonClick(View view) {
        Intent addActivityCancelIntent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(addActivityCancelIntent);
    }

}