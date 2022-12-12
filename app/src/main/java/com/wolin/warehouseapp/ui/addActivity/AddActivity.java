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
    private Button dateToBuyAddButton;
    private RadioGroup radioGroup;
    private RadioButton lowPriorityButton;
    private RadioButton mediumPriorityButton;
    private RadioButton highPriorityButton;

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
        radioGroup = findViewById(R.id.radioGroup);
        lowPriorityButton = findViewById(R.id.lowPriorityButton);
        mediumPriorityButton = findViewById(R.id.mediumPriorityButton);
        highPriorityButton = findViewById(R.id.highPriorityButton);

        shopLogoAdd = findViewById(R.id.shopLogoAdd);
        addActivityAddButton = findViewById(R.id.addActivityAddButton);
        addActivityCancelButton = findViewById(R.id.addActivityCancelButton);

        productImageAdd.setImageResource(R.drawable.press_to_add_product_image);
        shopLogoAdd.setImageResource(R.drawable.noneshop);

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        currentGroupId = getIntent().getStringExtra("currentGroupId");
        System.out.println("ID GRUPY GETINTENT: " + getIntent().getStringExtra("currentGroupId"));

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
                String date = TimeFormatter.makeDateString(day, month, year);
                dateToBuyAddButton.setText(date);
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
        shops = new ArrayList<>();
        Shop defaultShop = new Shop("Dowolny", resources.getResourceEntryName(R.drawable.noneshop));
        chosenShop = defaultShop;
        shops.add(defaultShop);
        shops.add(new Shop("Auchan", resources.getResourceEntryName(R.drawable.auchan)));
        shops.add(new Shop("Biedronka", resources.getResourceEntryName(R.drawable.biedronka)));
        shops.add(new Shop("Carrefour", resources.getResourceEntryName(R.drawable.carrefour)));
        shops.add(new Shop("Delikatesy-Centrum", resources.getResourceEntryName(R.drawable.delikatesy)));
        shops.add(new Shop("Dino", resources.getResourceEntryName(R.drawable.dino)));
        shops.add(new Shop("Kaufland", resources.getResourceEntryName(R.drawable.kaufland)));
        shops.add(new Shop("Lewiatan", resources.getResourceEntryName(R.drawable.lewiatan)));
        shops.add(new Shop("Lidl", resources.getResourceEntryName(R.drawable.lidl)));
        shops.add(new Shop("Top Market", resources.getResourceEntryName(R.drawable.topmarket)));
        shops.add(new Shop("Żabka", resources.getResourceEntryName(R.drawable.zabka)));
    }

        //adding product
    public void onAddActivityAddButtonClick (View view){
        if(!productNameAdd.getText().equals(null)) {
            String productName = productNameAdd.getText().toString().trim();
            int count = Integer.parseInt(countAdd.getText().toString().trim());
            double maxPrice = Double.parseDouble(maxPriceAdd.getText().toString().trim());
            String note = noteAdd.getText().toString().trim();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = simpleDateFormat.format(new Date().getTime());
            String dateToBuy = dateToBuyAddButton.getText().toString().trim();
            String priority;
            if(mediumPriorityButton.isChecked()) {
                priority = "medium";
            } else if (lowPriorityButton.isChecked()) {
                priority = "low";
            } else {
                priority = "high";
            }

            Product product = new Product(productName, count, maxPrice, note, chosenShop, true, date, dateToBuy, priority, currentFirebaseUser.getUid());

            firebaseProductViewModel.insertProduct(product, mImageURI, currentGroupId);
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            intent.putExtra("currentGroupId", currentGroupId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nazwa produktu musi zostać podana.", Toast.LENGTH_LONG);
        }
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
}