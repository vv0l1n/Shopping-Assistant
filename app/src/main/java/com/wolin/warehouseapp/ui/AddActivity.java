package com.wolin.warehouseapp.ui;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseViewModel;
import com.wolin.warehouseapp.room.viewmodel.PhotoRoomViewModel;
import com.wolin.warehouseapp.utils.adapter.AddActivityShopAdapter;
import com.wolin.warehouseapp.utils.adapter.ShopSelectListener;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.Shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.lifecycle.Observer;

public class AddActivity extends AppCompatActivity implements ShopSelectListener {

    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    private ImageView productImageAdd;
    private EditText productNameAdd;
    private EditText countAdd;
    private EditText maxPriceAdd;
    private EditText noteAdd;
    private ImageView shopLogoAdd;
    private Button addActivityAddButton;
    private Button addActivityCancelButton;
    private Dialog dialog;
    private RecyclerView addActivityShopRecyclerView;
    private List<Shop> shops;

    private Shop choosenShop;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private Uri mImageURI = null;
    private FirebaseViewModel firebaseViewModel;
    private PhotoRoomViewModel photoRoomViewModel;

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

        photoRoomViewModel = new ViewModelProvider(this).get(PhotoRoomViewModel.class);
        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);

        //permission for camera
        if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        //product image from camera
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                productImageAdd.setImageBitmap((Bitmap) result.getData().getExtras().get("data"));
                                System.out.println(result.getData() + "niger");
                            } else {
                                System.out.println("bład kamery");
                            }
                        }
                    }
                });

        //product image from gallery
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        productImageAdd.setImageURI(result.getData().getData());
                        productImageAdd.setMaxWidth(130);
                        productImageAdd.setMinimumWidth(130);
                        productImageAdd.setMaxHeight(130);
                        productImageAdd.setMinimumHeight(130);
                        mImageURI = result.getData().getData();
                        uploadFromGallery(noteAdd.getRootView());
                    } else {
                        System.out.println("błąd galerii");
                    }
                });
        //loading dialog for
        loadDialog(productNameAdd.getRootView());
    }

    //picking product image
    public void onProductImageClick(View view) {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] options = {"Zrób zdjęcie", "Wybierz z galerii", "Anuluj"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("Wybierz zdjęcie!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Zrób zdjęcie")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    cameraActivityResultLauncher.launch(intent);
                } else {
                    System.out.println("!!!!!!!!!!!!!!!!");
                }
            } else if (options[item].equals("Wybierz z galerii")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(intent);
            } else if (options[item].equals("Anuluj")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void uploadFromGallery(View view) {
        firebaseViewModel.uploadImagesToFirebase(mImageURI , photoRoomViewModel);
        firebaseViewModel.getTaskMutableLiveData().observe(AddActivity.this, documentReferenceTask -> {
            if (documentReferenceTask.isSuccessful()) {
                Toast.makeText(AddActivity.this, "Image Uploaded Successfully !!", Toast.LENGTH_SHORT).show();
                productImageAdd.setImageURI(mImageURI);
            } else {
                Toast.makeText(AddActivity.this, documentReferenceTask.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    //picking shop
    public void onShopLogoClick(View view) {
        dialog.show();
    }

    //shop dialog
    public void loadDialog(View v) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_activity_shop_dialog);

        //add shops
        addData();

        AddActivityShopAdapter adapter = new AddActivityShopAdapter(this ,shops, this);

        addActivityShopRecyclerView = dialog.findViewById(R.id.addActivityShopRecyclerView);
        addActivityShopRecyclerView.setAdapter(adapter);
        addActivityShopRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
    }

    public void addData() {
        shops = new ArrayList<>();
        Shop dowolny = new Shop("Dowolny", R.drawable.noneshop);
        choosenShop = dowolny;
        shops.add(dowolny);
        shops.add(new Shop("Auchan", R.drawable.auchan));
        shops.add(new Shop("Biedronka", R.drawable.biedronka));
        shops.add(new Shop("Carrefour", R.drawable.carrefour));
        shops.add(new Shop("Delikatesy-Centrum", R.drawable.delikatesy));
        shops.add(new Shop("Dino", R.drawable.dino));
        shops.add(new Shop("Kaufland", R.drawable.kaufland));
        shops.add(new Shop("Lewiatan", R.drawable.lewiatan));
        shops.add(new Shop("Lidl", R.drawable.lidl));
        shops.add(new Shop("Top Market", R.drawable.topmarket));
        shops.add(new Shop("Żabka", R.drawable.zabka));
    }

        //adding product
        public void onAddActivityAddButtonClick (View view){

            String productName = productNameAdd.getText().toString().trim();
            int count = Integer.parseInt(countAdd.getText().toString().trim());
            double maxPrice = Integer.parseInt(maxPriceAdd.getText().toString().trim());
            String note = noteAdd.getText().toString().trim();
            int imageOfProduct;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = simpleDateFormat.format(new Date());


            //Product product = new Product(productName, count, maxPrice, note, choosenShop, imageOfProduct, true, date, currentFirebaseUser.getUid());
        }



        //cancel button
        public void onAddActivityCancelButtonClick (View view){
            Intent addActivityCancelIntent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(addActivityCancelIntent);
        }

    @Override
    public void onShopClick(Shop shop) {
        choosenShop = shop;
        switch(shop.getName()) {
            case "Auchan": {
                shopLogoAdd.setImageResource(R.drawable.auchan);
                break;
            }
            case "Biedronka": {
                shopLogoAdd.setImageResource(R.drawable.biedronka);
                break;
            }
            case "Carrefour": {
                shopLogoAdd.setImageResource(R.drawable.carrefour);
                break;
            }
            case "Delikatesy-Centrum": {
                shopLogoAdd.setImageResource(R.drawable.delikatesy);
                break;
            }
            case "Dino": {
                shopLogoAdd.setImageResource(R.drawable.dino);
                break;
            }
            case "Kaufland": {
                shopLogoAdd.setImageResource(R.drawable.kaufland);
                break;
            }
            case "Lewiatan": {
                shopLogoAdd.setImageResource(R.drawable.lewiatan);
                break;
            }
            case "Lidl": {
                shopLogoAdd.setImageResource(R.drawable.lidl);
                break;
            }
            case "Top Market": {
                shopLogoAdd.setImageResource(R.drawable.topmarket);
                break;
            }
            case "Żabka": {
                shopLogoAdd.setImageResource(R.drawable.zabka);
                break;
            }
            default: {
                shopLogoAdd.setImageResource(R.drawable.noneshop);
                break;
            }
        }

        dialog.dismiss();
    }
}