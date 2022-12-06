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
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.addActivity.adapter.AddActivityShopAdapter;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.Shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Button editButton;
    private Button cancelButton;
    private Button dateToBuyButton;
    private RadioGroup radioGroup;
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
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private Uri mImageURI;
    private FirebaseProductViewModel firebaseProductViewModel;

    private FirebaseUserViewModel firebaseUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_activity);
        productImage = findViewById(R.id.productImageAddE);
        productName = findViewById(R.id.productNameAddE);
        count = findViewById(R.id.countAddE);
        maxPrice = findViewById(R.id.maxPriceAddE);
        note = findViewById(R.id.noteAddE);
        dateToBuyButton = findViewById(R.id.dateToBuyAddButtonE);
        radioGroup = findViewById(R.id.radioGroupE);
        lowPriorityButton = findViewById(R.id.lowPriorityButtonE);
        mediumPriorityButton = findViewById(R.id.mediumPriorityButtonE);
        highPriorityButton = findViewById(R.id.highPriorityButtonE);

        shopLogo = findViewById(R.id.shopLogoAddE);
        editButton = findViewById(R.id.editButtonE);
        cancelButton = findViewById(R.id.cancelButtonE);

        productImage.setImageResource(R.drawable.press_to_add_product_image);
        shopLogo.setImageResource(R.drawable.noneshop);

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        currentGroupId = getIntent().getStringExtra("currentGroupId");
        currentProductId = getIntent().getStringExtra("currentProductId");
        System.out.println("ID GRUPY GETINTENT: " + getIntent().getStringExtra("currentGroupId"));

        loadProduct();

        //permission for camera
        if (ContextCompat.checkSelfPermission(EditProductActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProductActivity.this, new String[]{
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
                                productImage.setImageBitmap((Bitmap) result.getData().getExtras().get("data"));
                                System.out.println(result.getData());
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
        final CharSequence[] options = {"Zrób zdjęcie", "Wybierz z galerii", "Anuluj"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductActivity.this);
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

    //picking date to buy
    public void onAddDateToBuyButton(View view) {
        datePickerDialog.show();
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month++;
                String date = makeDateString(day, month, year);
                dateToBuyButton.setText(date);
            }
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

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }


    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
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
        chosenShop = dowolny;
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
    public void onEditButtonClick (View view){
        if(!productName.getText().equals(null)) {
            currentProduct.setName(productName.getText().toString().trim());
            currentProduct.setCount(Integer.parseInt(count.getText().toString().trim()));
            currentProduct.setMaxPrice(Double.parseDouble(maxPrice.getText().toString().trim()));
            currentProduct.setNote(note.getText().toString().trim());
            currentProduct.setDateToBuy(dateToBuyButton.getText().toString().trim());
            String priority;
            if(mediumPriorityButton.isChecked()) {
                priority = "medium";
            } else if (lowPriorityButton.isChecked()) {
                priority = "low";
            } else {
                priority = "high";
            }
            currentProduct.setPriority(priority);
            System.out.println("ID GRUPY ADDACTIVITY: " + currentGroupId);

            firebaseProductViewModel.update(currentProduct, mImageURI, currentGroupId);
            Intent intent = new Intent(EditProductActivity.this, YourProductsActivity.class);
            intent.putExtra("currentGroupId", currentGroupId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nazwa produktu musi zostać podana.", Toast.LENGTH_LONG);
        }
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
                shopLogo.setImageResource(product.getShop().getShopLogo());
            }
        });
    }
}