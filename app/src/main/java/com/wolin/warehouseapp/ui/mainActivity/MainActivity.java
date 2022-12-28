package com.wolin.warehouseapp.ui.mainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.ui.addActivity.AddActivity;
import com.wolin.warehouseapp.ui.createGroupActivity.CreateGroupActivity;
import com.wolin.warehouseapp.ui.invitesActivity.InvitesActivity;
import com.wolin.warehouseapp.ui.loginActivity.LoginActivity;
import com.wolin.warehouseapp.ui.mainActivity.adapter.productadapter.ProductAdapter;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.mainActivity.adapter.groupadapter.MainActivityGroupAdapter;
import com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.SelectGroupActivity;
import com.wolin.warehouseapp.ui.profileActivity.ProfileActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemSelectListener<Object>, NavigationView.OnNavigationItemSelectedListener {

    private Dialog dialog;
    private CheckBox onlyActive;
    private ImageButton sortButton;
    private ImageButton filterButton;
    private ImageButton groupButton;
    private RecyclerView productRecyclerView;
    private Button addButton;
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

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MainActivity");

        auth = FirebaseAuth.getInstance();

        onlyActive = findViewById(R.id.onlyActive);
        sortButton = findViewById(R.id.sortButton);
        filterButton = findViewById(R.id.filterButton);
        groupButton = findViewById(R.id.groupButton);
        productRecyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        actualGroupTextView = findViewById(R.id.actualGroupTextView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navViev = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        currentGroup = new Group("brak", "brak");
        currentGroup.setProducts(new ArrayList<>());
        userGroups = new ArrayList<>();

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        productAdapter = new ProductAdapter(currentGroup.getProducts(), this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid(), getResources());
        productRecyclerView.setAdapter(productAdapter);

        actualGroupTextView.setText("Aktualna grupa: brak");

        loadDialog();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navViev.setNavigationItemSelectedListener(this);
    }

    public void onMainActivityAddButtonClick(View view) {
        if(!currentGroup.getName().equals("brak")) {
            Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
            addIntent.putExtra("currentGroupId", currentGroup.getId());
            startActivity(addIntent);
        } else {
            Toast.makeText(this, "Nie możesz dodać produktu, jeżeli nie jesteś w żadnej grupie.", Toast.LENGTH_LONG);
        }
    }

    public void onGroupButtonCick(View view) {
        dialog.show();
    }

    private void loadDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.main_group_dialog);

        MainActivityGroupAdapter adapter = new MainActivityGroupAdapter(userGroups, this);
        groupRecyclerView = dialog.findViewById(R.id.groupRecyclerView);
        groupRecyclerView.setAdapter(adapter);
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        firebaseGroupViewModel.getGroups(currentFirebaseUser.getUid()).observe(this, (List<Group> groups) -> {
            System.out.println("change");
            for(Group group : groups) {
                System.out.println(group);
            }
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
                productAdapter.updateData(currentGroup.getProducts(), currentGroup.getId());
                System.out.println("PRODUKTY: ");
                for(Product product : currentGroup.getProducts()) {
                    System.out.println(product);
                }
                productAdapter.notifyDataSetChanged();
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
            productAdapter = new ProductAdapter(currentGroup.getProducts(), this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid(), getResources());
            productRecyclerView.setAdapter(productAdapter);
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
            case R.id.nav_profile:
                Intent intentProfile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.nav_add_group:
                Intent intentAdd = new Intent(MainActivity.this, CreateGroupActivity.class);
                startActivity(intentAdd);
                break;
            case R.id.nav_your_products:
                Intent intentYP = new Intent(MainActivity.this, YourProductsActivity.class);
                intentYP.putExtra("currentGroupId", currentGroup.getId());
                startActivity(intentYP);
                break;
            case R.id.nav_list:
                break;
            case R.id.nav_manage_group:
                Intent intentManageGroups = new Intent(MainActivity.this, SelectGroupActivity.class);
                startActivity(intentManageGroups);
                break;
            case R.id.nav_invites:
                Intent intentInvites = new Intent(MainActivity.this, InvitesActivity.class);
                intentInvites.putExtra("lastIntent", "main");
                startActivity(intentInvites);
                break;
            case R.id.nav_logout:
                auth.signOut();
                Intent intentLogOut = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentLogOut);
        }
        return true;
    }
}