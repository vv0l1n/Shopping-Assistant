package com.wolin.warehouseapp.ui.editProductActivity;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.ui.addActivity.adapter.AddActivityShopAdapter;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.common.ShopLoader;
import com.wolin.warehouseapp.utils.common.TimeFormatter;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.Shop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditProductActivity extends AppCompatActivity implements ItemSelectListener<Shop> {

    ActivityResultLauncher<Intent> cameraActivityResultLauncher;
    ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    private ImageView productImage;
    private EditText productName;
    private EditText count;
    private EditText maxPrice;
    private EditText note;
    private ImageView shopLogo;
    private Button dateToBuyButton;
    private RadioButton lowPriorityButton;
    private RadioButton mediumPriorityButton;
    private RadioButton highPriorityButton;

    private Dialog dialog;
    private RecyclerView addActivityShopRecyclerView;
    private List<Shop> shops;
    private DatePickerDialog datePickerDialog;

    private String currentGroupId;
    private String currentProductId;
    private Product currentProduct;

    private Shop chosenShop;

    private Uri mImageURI;
    private FirebaseProductViewModel firebaseProductViewModel;

    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        productImage = findViewById(R.id.productImageAddE);
        productName = findViewById(R.id.productNameAddE);
        count = findViewById(R.id.countAddE);
        maxPrice = findViewById(R.id.maxPriceAddE);
        note = findViewById(R.id.noteAddE);
        dateToBuyButton = findViewById(R.id.dateToBuyAddButtonE);
        lowPriorityButton = findViewById(R.id.lowPriorityButtonE);
        mediumPriorityButton = findViewById(R.id.mediumPriorityButtonE);
        highPriorityButton = findViewById(R.id.highPriorityButtonE);

        shopLogo = findViewById(R.id.shopLogoAddE);

        productImage.setImageResource(R.drawable.press_to_add_product_image);
        shopLogo.setImageResource(R.drawable.noneshop);

        resources = getResources();

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);

        currentGroupId = getIntent().getStringExtra("currentGroupId");
        currentProductId = getIntent().getStringExtra("currentProductId");

        loadProduct();


        //product image from gallery
        galleryActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        productImage.setImageURI(result.getData().getData());
                        productImage.setMaxWidth(130);
                        productImage.setMinimumWidth(130);
                        productImage.setMaxHeight(130);
                        productImage.setMinimumHeight(130);
                        mImageURI = result.getData().getData();
                    } else {
                        System.out.println("błąd galerii");
                    }
                });
        //loading dialog for
        loadDialog(productName.getRootView());
        //loading datepicker
        initDatePicker();
    }
    //picking product image
    public void onProductImageClick(View view) {
        selectImage();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryActivityResultLauncher.launch(intent);
    }

    //picking date to buy
    public void onAddDateToBuyButton(View view) {
        datePickerDialog.show();
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month++;
            String date = TimeFormatter.makeDateString(day, month, year);
            dateToBuyButton.setText(date);
        };

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+31556952000l);

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
        dialog.setContentView(R.layout.shop_dialog);

        //add shops
        addData();

        AddActivityShopAdapter adapter = new AddActivityShopAdapter(this ,shops, this, getResources());

        addActivityShopRecyclerView = dialog.findViewById(R.id.addActivityShopRecyclerView);
        addActivityShopRecyclerView.setAdapter(adapter);
        addActivityShopRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));
    }


    public void addData() {
        Resources resources = getResources();
        shops = ShopLoader.loadShop(resources);
        chosenShop = shops.get(0);
    }
    //adding product
    public void onEditButtonClick (View view){
        if (!productName.getText().toString().isEmpty()) {
            currentProduct.setName(productName.getText().toString().trim());
        } else {
            Toast.makeText(this, "Nazwa produktu musi zostać podana.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!count.getText().toString().isEmpty()) {
            currentProduct.setCount(Integer.parseInt(count.getText().toString().trim()));
        } else {
            Toast.makeText(this, "Ilość produktu musi zostać podana.", Toast.LENGTH_LONG).show();
            return;
        }

        if(!maxPrice.getText().toString().isEmpty()) {
            currentProduct.setMaxPrice(Double.parseDouble(maxPrice.getText().toString().trim()));
        } else {
            currentProduct.setMaxPrice(0);
        }

        String noteStr = note.getText().toString().trim();
        if(!noteStr.isEmpty()) {
            currentProduct.setNote(noteStr);
        } else {
            currentProduct.setNote("Brak uwag");
        }

        String dateToBuyStr = dateToBuyButton.getText().toString().trim();
        if(!dateToBuyStr.equals("Brak daty wygaśnięcia.")) {
            currentProduct.setDateToBuy(dateToBuyStr);
        }

        String priorityStr;
        if(mediumPriorityButton.isChecked()) {
            priorityStr = "Niski";
        } else if (lowPriorityButton.isChecked()) {
            priorityStr = "Średni";
        } else {
            priorityStr = "Wysoki";
        }
        currentProduct.setPriority(priorityStr);


        firebaseProductViewModel.update(currentProduct, mImageURI, currentGroupId);
            Intent intent = new Intent(EditProductActivity.this, YourProductsActivity.class);
            intent.putExtra("currentGroupId", currentGroupId);
            startActivity(intent);
        }


    //cancel button
    public void onCancelButtonClick (View view){
        Intent addActivityCancelIntent = new Intent(EditProductActivity.this, YourProductsActivity.class);
        addActivityCancelIntent.putExtra("currentGroupId", currentGroupId);
        startActivity(addActivityCancelIntent);
    }


    @Override
    public void onItemClick(Shop shop) {
        chosenShop = shop;
        switch(shop.getName()) {
            case "Auchan": {
                shopLogo.setImageResource(R.drawable.auchan);
                break;
            }
            case "Biedronka": {
                shopLogo.setImageResource(R.drawable.biedronka);
                break;
            }
            case "Carrefour": {
                shopLogo.setImageResource(R.drawable.carrefour);
                break;
            }
            case "Delikatesy-Centrum": {
                shopLogo.setImageResource(R.drawable.delikatesy);
                break;
            }
            case "Dino": {
                shopLogo.setImageResource(R.drawable.dino);
                break;
            }
            case "Kaufland": {
                shopLogo.setImageResource(R.drawable.kaufland);
                break;
            }
            case "Lewiatan": {
                shopLogo.setImageResource(R.drawable.lewiatan);
                break;
            }
            case "Lidl": {
                shopLogo.setImageResource(R.drawable.lidl);
                break;
            }
            case "Top Market": {
                shopLogo.setImageResource(R.drawable.topmarket);
                break;
            }
            case "Żabka": {
                shopLogo.setImageResource(R.drawable.zabka);
                break;
            }
            default: {
                shopLogo.setImageResource(R.drawable.noneshop);
                break;
            }
        }

        dialog.dismiss();
    }

    public void loadProduct() {
        firebaseProductViewModel.getProduct(currentProductId, currentGroupId).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                currentProduct = product;
                if(product.getPhoto() != null) {
                    Glide.with(getApplicationContext()).load(product.getPhoto()).into(productImage);
                }
                productName.setText(product.getName());
                count.setText(Integer.toString(product.getCount()));
                maxPrice.setText(Double.toString(product.getMaxPrice()));
                note.setText(product.getNote());
                dateToBuyButton.setText(product.getDateToBuy());
                switch(product.getPriority()) {
                    case "low":
                        lowPriorityButton.setActivated(true);
                        break;
                    case "medium":
                        mediumPriorityButton.setActivated(true);
                        break;
                    case "high":
                        highPriorityButton.setActivated(true);
                        break;
                }

                shopLogo.setImageResource(resources.getIdentifier(product.getShop().getShopLogo(), "drawable", "com.wolin.warehouseapp"));
                for(Shop shop : shops) {
                    if(product.getShop().getName().equals(shop.getName())) {
                        chosenShop = shop;
                        break;
                    }
                }
            }
        });
    }
}