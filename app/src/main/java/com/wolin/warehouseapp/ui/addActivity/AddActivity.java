package com.wolin.warehouseapp.ui.addActivity;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.addActivity.adapter.AddActivityShopAdapter;
import com.wolin.warehouseapp.utils.common.Category;
import com.wolin.warehouseapp.utils.common.ShopLoader;
import com.wolin.warehouseapp.utils.common.TimeFormatter;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.Shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements ItemSelectListener<Shop> {

    ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    private ImageView productImageAdd;
    private EditText productNameAdd;
    private EditText countAdd;
    private EditText maxPriceAdd;
    private EditText noteAdd;
    private ImageView shopLogoAdd;
    private Button dateToBuyAddButton;
    private RadioButton lowPriorityButton;
    private RadioButton mediumPriorityButton;
    private RadioButton highPriorityButton;
    private Button categoryButton;

    private Dialog dialog;
    private RecyclerView addActivityShopRecyclerView;
    private List<Shop> shops;
    private DatePickerDialog datePickerDialog;

    private String currentGroupId;

    private Shop chosenShop;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private Uri mImageURI;
    private FirebaseProductViewModel firebaseProductViewModel;

    private FirebaseUserViewModel firebaseUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productImageAdd = findViewById(R.id.productImageAdd);
        productNameAdd = findViewById(R.id.productNameAdd);
        countAdd = findViewById(R.id.countAdd);
        maxPriceAdd = findViewById(R.id.maxPriceAdd);
        noteAdd = findViewById(R.id.noteAdd);
        dateToBuyAddButton = findViewById(R.id.dateToBuyAddButton);
        lowPriorityButton = findViewById(R.id.lowPriorityButton);
        mediumPriorityButton = findViewById(R.id.mediumPriorityButton);
        highPriorityButton = findViewById(R.id.highPriorityButton);
        categoryButton = findViewById(R.id.AddCategoryButton);

        shopLogoAdd = findViewById(R.id.shopLogoAdd);

        productImageAdd.setImageResource(R.drawable.press_to_add_product_image);
        shopLogoAdd.setImageResource(R.drawable.noneshop);

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        currentGroupId = getIntent().getStringExtra("currentGroupId");


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
                    } else {
                        System.out.println("błąd galerii");
                    }
                });
        //loading dialog for
        loadDialog(productNameAdd.getRootView());
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
            dateToBuyAddButton.setText(date);
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
    public void onAddActivityAddButtonClick(View view){

        String productName;
        if (!productNameAdd.getText().toString().isEmpty()) {
            productName = productNameAdd.getText().toString().trim();
        } else {
            Toast.makeText(this, "Nazwa produktu musi zostać podana.", Toast.LENGTH_LONG).show();
            return;
        }

        int count;
        if (!countAdd.getText().toString().isEmpty()) {
            count = Integer.parseInt(countAdd.getText().toString().trim());
        } else {
            Toast.makeText(this, "Ilość produktu musi zostać podana.", Toast.LENGTH_LONG).show();
            return;
        }

        double maxPrice;
        if(!maxPriceAdd.getText().toString().isEmpty()) {
            maxPrice = Double.parseDouble(maxPriceAdd.getText().toString().trim());
        } else {
            maxPrice = 0;
        }

            String note = noteAdd.getText().toString().trim();
            if(note.isEmpty()) {
                note = "Brak uwag";
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = simpleDateFormat.format(new Date().getTime());
            String dateToBuy = dateToBuyAddButton.getText().toString().trim();
            if(dateToBuy.equals("Data wygaśnięcia")) {
                dateToBuy = "Brak daty wygaśnięcia.";
            }

            String priority;
            if(mediumPriorityButton.isChecked()) {
                priority = "Niski";
            } else if (lowPriorityButton.isChecked()) {
                priority = "Średni";
            } else {
                priority = "Wysoki";
            }

            Category category;
            String categoryStr = categoryButton.getText().toString();
            switch (categoryStr) {
                case "Odzież":
                    category = Category.CLOTHES;
                    break;
                case "Żywność":
                    category = Category.FOOD;
                    break;
                case "Użytek domowy":
                    category = Category.HOME;
                    break;
                case "Inne":
                default:
                    category = Category.OTHERS;
                    break;
            }

            Product product = new Product(productName, count, maxPrice, note, chosenShop, true, date, dateToBuy, priority, currentFirebaseUser.getUid(), category);

            firebaseProductViewModel.insertProduct(product, mImageURI, currentGroupId);
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            intent.putExtra("currentGroupId", currentGroupId);
            startActivity(intent);
        }

        //cancel button
        public void onAddActivityCancelButtonClick (View view){
            Intent addActivityCancelIntent = new Intent(AddActivity.this, MainActivity.class);
            addActivityCancelIntent.putExtra("currentGroupId", currentGroupId);
            startActivity(addActivityCancelIntent);
        }


    @Override
    public void onItemClick(Shop shop) {
        chosenShop = shop;
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

    public void onAddCategoryButtonClick(View view) {
        System.out.println("KATEGORIA");
        final CharSequence[] categories = {"Odzież", "Żywność", "Użytek domowy", "Inne"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setTitle("Wybierz kategorię");
        builder.setItems(categories, (dialog, item) -> {
            categoryButton.setText(categories[item]);
        });
        builder.show();
    }
}