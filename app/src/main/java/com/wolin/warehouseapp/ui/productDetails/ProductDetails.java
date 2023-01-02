package com.wolin.warehouseapp.ui.productDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.User;

public class ProductDetails extends AppCompatActivity {

    private TextView nametv;
    private ImageView productImage;
    private TextView desctv;
    private TextView counttv;
    private TextView maxPricetv;
    private TextView addDatetv;
    private TextView dateToBuytv;
    private TextView prioritytv;
    private TextView activetv;
    private TextView buyertv;
    private TextView ownertv;
    private ImageView shopImage;

    private FirebaseUserViewModel firebaseUserViewModel;

    private Product product;
    private String lastActivity;
    private String lastGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        nametv = findViewById(R.id.productDetailsName);
        productImage = findViewById(R.id.productDetailsImage);
        desctv = findViewById(R.id.productDetailsDescription);
        counttv = findViewById(R.id.productDetailsCount);
        maxPricetv = findViewById(R.id.productDetailsMaxPrice);
        addDatetv = findViewById(R.id.productDetailsAddDate);
        dateToBuytv = findViewById(R.id.productDetailsDateToBuy);
        prioritytv = findViewById(R.id.productDetailsPriority);
        activetv = findViewById(R.id.productDetailsActive);
        buyertv = findViewById(R.id.productDetailsBuyer);
        ownertv = findViewById(R.id.productDetailsOwner);
        shopImage = findViewById(R.id.productDetailsShop);

        product = (Product) getIntent().getSerializableExtra("product");
        lastActivity = getIntent().getExtras().getString("activity");
        lastGroupId = getIntent().getExtras().getString("currentGroupId");

        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        loadProduct();
    }

    private void loadProduct() {
        nametv.setText(product.getName());

        if(!(product.getPhoto() == null)) {
            Glide.with(getBaseContext()).load(product.getPhoto())
                    .into(productImage);
        }

        desctv.setText(product.getNote());
        counttv.setText("Ilość: " + product.getCount());
        maxPricetv.setText("Produkt nie powinien być droższy niż: " + product.getMaxPrice());
        addDatetv.setText("Produkt dodano: " + product.getDate());
        dateToBuytv.setText("Produkt należy kupić do: " + product.getDateToBuy());
        prioritytv.setText("Priorytet: " + product.getPriority());

        if(product.isActive()) {
            activetv.setText("Produkt jest aktywny");
        } else {
            activetv.setText("Produkt nie jest aktywny");
        }

        if(product.getBuyer() != null) {
            firebaseUserViewModel.getUser(product.getBuyer()).observe(this, user -> {
                buyertv.setText("Produkt został kupiony przez: " + user.getName() + " " + user.getLastName());
            });
        } else {
            buyertv.setText("Produkt nie został przez nikogo kupiony");
        }

        firebaseUserViewModel.getUser(product.getOwner()).observe(this, user -> {
            ownertv.setText("Właścicielem produktu jest: " + user.getName() + " " + user.getLastName());
        });

        shopImage.setImageResource(getResources().getIdentifier(product.getShop().getShopLogo(),
                "drawable", "com.wolin.warehouseapp"));

    }


    public void onProductDetailsBackButtonClick(View view) {
        Intent i;
        if(lastActivity.equals("main")) {
            i = new Intent(ProductDetails.this, MainActivity.class);
        } else {
            i = new Intent(ProductDetails.this, YourProductsActivity.class);
        }
        i.putExtra("currentGroupId", lastGroupId);
        startActivity(i);
    }
}
