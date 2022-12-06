package com.wolin.warehouseapp.ui.yourProductsActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.createGroupActivity.CreateGroupActivity;
import com.wolin.warehouseapp.ui.editProductActivity.EditProductActivity;
import com.wolin.warehouseapp.ui.loginActivity.LoginActivity;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.mainActivity.adapter.groupadapter.MainActivityGroupAdapter;
import com.wolin.warehouseapp.ui.yourProductsActivity.productadapter.ProductAdapterYPA;
import com.wolin.warehouseapp.utils.listeners.ItemEditListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YourProductsActivity extends AppCompatActivity implements ItemSelectListener<Object>, ItemEditListener<Product>, NavigationView.OnNavigationItemSelectedListener {

    private Dialog dialog;
    private CheckBox onlyActive;
    private ImageButton sortButton;
    private ImageButton filterButton;
    private ImageButton groupButton;
    private RecyclerView productRecyclerView;
    private RecyclerView groupRecyclerView;
    private TextView actualGroupTextView;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Group currentGroup;
    private List<Group> userGroups;
    private DrawerLayout drawerLayout;
    private NavigationView navViev;
    private Toolbar toolbar;

    private FirebaseAuth auth;

    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseProductViewModel firebaseProductViewModel;
    private FirebaseGroupViewModel firebaseGroupViewModel;

    private ProductAdapterYPA productAdapterYPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("YPA");
        setContentView(R.layout.your_products_activity);

        onlyActive = findViewById(R.id.onlyActiveYPA);
        sortButton = findViewById(R.id.sortButtonYPA);
        filterButton = findViewById(R.id.filterButtonYPA);
        groupButton = findViewById(R.id.groupButtonYPA);
        productRecyclerView = findViewById(R.id.recyclerViewYPA);
        actualGroupTextView = findViewById(R.id.actualGroupTextViewYPA);
        drawerLayout = findViewById(R.id.drawerLayoutYPA);
        navViev = findViewById(R.id.nav_viewYPA);
        toolbar = findViewById(R.id.toolbarYPA);

        auth = FirebaseAuth.getInstance();

        currentGroup = new Group("brak", "brak");
        currentGroup.setProducts(new ArrayList<>());
        userGroups = new ArrayList<>();

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        productAdapterYPA = new ProductAdapterYPA(this, currentGroup.getProducts(), this, this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid());
        productRecyclerView.setAdapter(productAdapterYPA);

        actualGroupTextView.setText("Aktualna grupa: brak");

        loadDialog(filterButton.getRootView());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navViev.setNavigationItemSelectedListener(this);
    }


    public void onGroupButtonCick(View view) {
        dialog.show();
    }

    private void loadDialog(View view) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.main_activity_group_dialog);

        MainActivityGroupAdapter adapter = new MainActivityGroupAdapter(this, userGroups, this);
        groupRecyclerView = dialog.findViewById(R.id.groupRecyclerView);
        groupRecyclerView.setAdapter(adapter);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        firebaseGroupViewModel.getGroups(currentFirebaseUser.getUid()).observe(this, (List<Group> groups) -> {
            if(groups != null && groups.size() > 0) {
                String lastGroupId = getIntent().getStringExtra("currentGroupId");
                if(lastGroupId != null) {
                    System.out.println("lastGroupId: " + lastGroupId);
                    for(Group group : groups) {
                        if(group.getId().equals(lastGroupId)) {
                            currentGroup = group;
                            System.out.println("NOWA AKTUALNA GRUPA");
                        }
                    }
                } else {
                    currentGroup = groups.get(0);
                }

                updateCurrentGroupName();
                adapter.updateData(groups);
                adapter.notifyDataSetChanged();
                productAdapterYPA.updateData(currentGroup.getProducts(), currentGroup.getId());
                productAdapterYPA.notifyDataSetChanged();
                //productAdapterYPA = new ProductAdapterYPA(this, currentGroup.getProducts(), this, this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid());
                productRecyclerView.setAdapter(productAdapterYPA);
                productRecyclerView.refreshDrawableState();
                productRecyclerView.getRecycledViewPool().clear();
            }
        });

    }

    private void updateCurrentGroupName() {
        actualGroupTextView.setText("Aktualna grupa: " + currentGroup.getName());
    }

    @Override
    public void onItemClick(Object o) {
        if(o instanceof Integer) {
            int position = (int) o;
            currentGroup = userGroups.get(position);
            updateCurrentGroupName();
            productAdapterYPA = new ProductAdapterYPA(this, currentGroup.getProducts(), this, this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid());
            productRecyclerView.setAdapter(productAdapterYPA);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
            dialog.dismiss();
        } else {
            Product p = (Product) o;
            System.out.println("KLIKNIETO PRODUKT: " + p.getName());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_add_group:
                Intent addGroup = new Intent(YourProductsActivity.this, CreateGroupActivity.class);
                startActivity(addGroup);
                break;
            case R.id.nav_your_products:
                break;
            case R.id.nav_list:
                Intent main = new Intent(YourProductsActivity.this, MainActivity.class);
                main.putExtra("currentGroupId", currentGroup.getId());
                finish();
                startActivity(main);
                break;
            case R.id.nav_logout:
                auth.signOut();
                Intent intentLogOut = new Intent(YourProductsActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
        }
        return true;
    }

    @Override
    public void onItemEdit(Product item) {
        Intent editIntent = new Intent(YourProductsActivity.this, EditProductActivity.class);
        editIntent.putExtra("currentGroupId", currentGroup.getId());
        editIntent.putExtra("currentProductId", item.getProductId());
        startActivity(editIntent);
    }
}
