package com.wolin.warehouseapp.ui.productDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.common.Category;
import com.wolin.warehouseapp.utils.model.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView nameTv;
    private ImageView productImage;
    private TextView descTv;
    private TextView countTv;
    private TextView maxPriceTv;
    private TextView addDateTv;
    private TextView dateToBuyTv;
    private TextView priorityTv;
    private TextView activeTv;
    private TextView buyerTv;
    private TextView ownerTv;
    private ImageView shopImage;
    private TextView categoryTv;

    private FirebaseUserViewModel firebaseUserViewModel;

    private Product product;
    private String lastActivity;
    private String lastGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        nameTv = findViewById(R.id.productDetailsName);
        productImage = findViewById(R.id.productDetailsImage);
        descTv = findViewById(R.id.productDetailsDescription);
        countTv = findViewById(R.id.productDetailsCount);
        maxPriceTv = findViewById(R.id.productDetailsMaxPrice);
        addDateTv = findViewById(R.id.productDetailsAddDate);
        dateToBuyTv = findViewById(R.id.productDetailsDateToBuy);
        priorityTv = findViewById(R.id.productDetailsPriority);
        activeTv = findViewById(R.id.productDetailsActive);
        buyerTv = findViewById(R.id.productDetailsBuyer);
        ownerTv = findViewById(R.id.productDetailsOwner);
        shopImage = findViewById(R.id.productDetailsShop);
        categoryTv = findViewById(R.id.productDetailsCategory);

        product = (Product) getIntent().getSerializableExtra("product");
        lastActivity = getIntent().getExtras().getString("activity");
        lastGroupId = getIntent().getExtras().getString("currentGroupId");

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        loadProduct();
    }

    private void loadProduct() {
        nameTv.setText(product.getName());

        if(!(product.getPhoto() == null)) {
            Glide.with(getBaseContext()).load(product.getPhoto())
                    .into(productImage);
        }

        descTv.setText(product.getNote());
        countTv.setText("Ilość: " + product.getCount());
        maxPriceTv.setText("Produkt nie powinien być droższy niż: " + product.getMaxPrice());
        addDateTv.setText("Produkt dodano: " + product.getDate());
        dateToBuyTv.setText("Produkt należy kupić do: " + product.getDateToBuy());
        priorityTv.setText("Priorytet: " + product.getPriority());

        if(product.isActive()) {
            activeTv.setText("Produkt jest aktywny");
        } else {
            activeTv.setText("Produkt nie jest aktywny");
        }

        if(product.getBuyer() != null) {
            firebaseUserViewModel.getUser(product.getBuyer()).observe(this, user -> {
                buyerTv.setText("Produkt został kupiony przez: " + user.getName() + " " + user.getLastName());
            });
        } else {
            buyerTv.setText("Produkt nie został przez nikogo kupiony");
        }

        firebaseUserViewModel.getUser(product.getOwner()).observe(this, user -> {
            ownerTv.setText("Właścicielem produktu jest: " + user.getName() + " " + user.getLastName());
        });

        shopImage.setImageResource(getResources().getIdentifier(product.getShop().getShopLogo(),
                "drawable", "com.wolin.warehouseapp"));

        switch (product.getCategory()) {
            case CLOTHES:
                categoryTv.setText("Odzież");
                break;
            case FOOD:
                categoryTv.setText("Żywność");
                break;
            case HOME:
                categoryTv.setText("Użytek domowy");
                break;
            case OTHERS:
                categoryTv.setText("Inne");
                break;
        }

    }


    public void onProductDetailsBackButtonClick(View view) {
        Intent i;
        if(lastActivity.equals("main")) {
            i = new Intent(ProductDetailsActivity.this, MainActivity.class);
        } else {
            i = new Intent(ProductDetailsActivity.this, YourProductsActivity.class);
        }
        i.putExtra("currentGroupId", lastGroupId);
        startActivity(i);
    }
}
