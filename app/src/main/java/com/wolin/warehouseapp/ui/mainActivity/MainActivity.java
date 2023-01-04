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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseInviteViewModel;
import com.wolin.warehouseapp.ui.addActivity.AddActivity;
import com.wolin.warehouseapp.ui.createGroupActivity.CreateGroupActivity;
import com.wolin.warehouseapp.ui.invitesActivity.InvitesActivity;
import com.wolin.warehouseapp.ui.loginActivity.LoginActivity;
import com.wolin.warehouseapp.ui.mainActivity.adapter.productadapter.ProductAdapter;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.ui.mainActivity.adapter.groupadapter.MainActivityGroupAdapter;
import com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.SelectGroupActivity;
import com.wolin.warehouseapp.ui.productDetails.ProductDetailsActivity;
import com.wolin.warehouseapp.ui.profileActivity.ProfileActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.common.Category;
import com.wolin.warehouseapp.utils.common.SortState;
import com.wolin.warehouseapp.utils.listeners.ItemBuyListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemSelectListener<Object>, NavigationView.OnNavigationItemSelectedListener, ItemBuyListener {

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

    private Dialog sortDialog;
    private Dialog filterDialog;

    private boolean selected = true;
    private SortState sortState = SortState.NONE;

    private FirebaseInviteViewModel firebaseInviteViewModel;
    private FirebaseProductViewModel firebaseProductViewModel;
    private FirebaseGroupViewModel firebaseGroupViewModel;

    private ProductAdapter productAdapter;

    private Category currentCategory = Category.NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        onlyActive = findViewById(R.id.onlyActive);
        onlyActive.setSelected(true);
        onlyActiveListener();
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

        firebaseInviteViewModel = new ViewModelProvider(this).get(FirebaseInviteViewModel.class);
        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);

        productAdapter = new ProductAdapter(currentGroup.getProducts(), this, this, getResources());
        productRecyclerView.setAdapter(productAdapter);

        actualGroupTextView.setText("Aktualna grupa: brak");

        loadDialog();
        loadFilterDialog();
        loadSortDialog();
        loadInvites();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navViev.setNavigationItemSelectedListener(this);
    }

    private void onlyActiveListener() {
        onlyActive.setOnCheckedChangeListener((compoundButton, b) -> {
            selected = !selected;
            productAdapter.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
        });
    }


    public void onMainFilterButtonClick(View view) {
        filterDialog.show();
    }

    public void onMainSortButtonClick(View view) {
        sortDialog.show();
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
            for(Group group : groups) {
                System.out.println(group);
            }
            if(groups != null && groups.size() > 0) {
                String lastGroupId = getIntent().getStringExtra("currentGroupId");
                if(lastGroupId != null) {
                    for(Group group : groups) {
                        if(group.getId().equals(lastGroupId)) {
                            currentGroup = group;
                        }
                    }
                } else {
                    currentGroup = groups.get(0);
                }
                updateCurrentGroupName();
                adapter.updateData(groups);
                adapter.notifyDataSetChanged();
                productAdapter.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
                productRecyclerView.refreshDrawableState();
                productRecyclerView.getRecycledViewPool().clear();
            }
        });

    }

    private void loadFilterDialog() {
        filterDialog = new Dialog(this);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(R.layout.filter_dialog);

        TextView clothes = filterDialog.findViewById(R.id.filterDialogClothes);
        TextView food = filterDialog.findViewById(R.id.filterDialogFood);
        TextView home = filterDialog.findViewById(R.id.filterDialogHome);
        TextView others = filterDialog.findViewById(R.id.filterDialogOthers);
        TextView none = filterDialog.findViewById(R.id.filterDialogNone);

        clothes.setOnClickListener(v -> {
            currentCategory = Category.CLOTHES;
            afterFilterClick();
            setDialogTextViewColor(clothes, food, home, others, none);
            clothes.setBackgroundColor(Color.LTGRAY);
        });

        food.setOnClickListener(v -> {
            currentCategory = Category.FOOD;
            afterFilterClick();
            setDialogTextViewColor(clothes, food, home, others, none);
            food.setBackgroundColor(Color.LTGRAY);
        });

        home.setOnClickListener(v -> {
            currentCategory = Category.HOME;
            afterFilterClick();
            setDialogTextViewColor(clothes, food, home, others, none);
            home.setBackgroundColor(Color.LTGRAY);
        });

        others.setOnClickListener(v -> {
            currentCategory = Category.OTHERS;
            afterFilterClick();
            setDialogTextViewColor(clothes, food, home, others, none);
            others.setBackgroundColor(Color.LTGRAY);
        });

        none.setBackgroundColor(Color.LTGRAY);
        none.setOnClickListener(v -> {
            currentCategory = Category.NONE;
            afterFilterClick();
            setDialogTextViewColor(clothes, food, home, others, none);
            none.setBackgroundColor(Color.LTGRAY);
        });
    }

    private void afterFilterClick() {
        filterDialog.dismiss();
        productAdapter.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
        productRecyclerView.refreshDrawableState();
        productRecyclerView.getRecycledViewPool().clear();
    }

    private void loadSortDialog() {
        sortDialog = new Dialog(this);
        sortDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sortDialog.setCancelable(true);
        sortDialog.setContentView(R.layout.sort_dialog);

        TextView highestPriority = sortDialog.findViewById(R.id.sortDialogHighPriority);
        TextView lowestPriority = sortDialog.findViewById(R.id.sortDialogLowPriority);
        TextView closestDate = sortDialog.findViewById(R.id.sortDialogClosestDate);
        TextView furthestDate = sortDialog.findViewById(R.id.sortDialogFurthestDate);
        TextView defaultSort = sortDialog.findViewById(R.id.sortDialogDeafault);

        highestPriority.setOnClickListener(v -> {
            sortState = SortState.DESCPRIORITY;
            afterSortClick();
            setDialogTextViewColor(highestPriority, lowestPriority, closestDate, furthestDate, defaultSort);
            highestPriority.setBackgroundColor(Color.LTGRAY);
        });

        lowestPriority.setOnClickListener(v -> {
            sortState = SortState.ASCPRIORITY;
            afterSortClick();
            setDialogTextViewColor(highestPriority, lowestPriority, closestDate, furthestDate, defaultSort);
            lowestPriority.setBackgroundColor(Color.LTGRAY);
        });

        closestDate.setOnClickListener(v -> {
            sortState = SortState.DESCDATE;
            setDialogTextViewColor(highestPriority, lowestPriority, closestDate, furthestDate, defaultSort);
            afterSortClick();
            closestDate.setBackgroundColor(Color.LTGRAY);
        });

        furthestDate.setOnClickListener(v -> {
            sortState = SortState.ASCDATE;
            setDialogTextViewColor(highestPriority, lowestPriority, closestDate, furthestDate, defaultSort);
            afterSortClick();
            furthestDate.setBackgroundColor(Color.LTGRAY);
        });

        defaultSort.setBackgroundColor(Color.LTGRAY);
        defaultSort.setOnClickListener(v -> {
            sortState = SortState.NONE;
            setDialogTextViewColor(highestPriority, lowestPriority, closestDate, furthestDate, defaultSort);
            afterSortClick();
            defaultSort.setBackgroundColor(Color.LTGRAY);
        });
    }

    private void setDialogTextViewColor(TextView t1, TextView t2, TextView t3, TextView t4, TextView t5) {
        t1.setBackgroundColor(Color.WHITE);
        t2.setBackgroundColor(Color.WHITE);
        t3.setBackgroundColor(Color.WHITE);
        t4.setBackgroundColor(Color.WHITE);
        t5.setBackgroundColor(Color.WHITE);
    }

    private void afterSortClick() {
        sortDialog.dismiss();
        productAdapter.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
        productRecyclerView.refreshDrawableState();
        productRecyclerView.getRecycledViewPool().clear();
    }

    private void updateCurrentGroupName() {
        actualGroupTextView.setText("Aktualna grupa: " + currentGroup.getName());
    }

    private void loadInvites() {
        firebaseInviteViewModel.getInvites(currentFirebaseUser.getUid()).observe(this, groupInvites -> {
            if(groupInvites.size() > 0) {
                SpannableString s = new SpannableString("Zaproszenia do grup: " + groupInvites.size());
                s.setSpan(new ForegroundColorSpan(Color.CYAN), 0, s.length(), 0);
                navViev.getMenu().getItem(1).getSubMenu().getItem(2).setTitle(s);
            } else {
                SpannableString s = new SpannableString("Zaproszenia do grup");
                s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
                navViev.getMenu().getItem(1).getSubMenu().getItem(2).setTitle(s);
            }
        });
    }

    @Override
    public void onItemClick(Object o) {
        if(o instanceof Integer) {
            int position = (int) o;
            currentGroup = userGroups.get(position);
            updateCurrentGroupName();
            productAdapter = new ProductAdapter(currentGroup.getProducts(), this, this, getResources());
            productRecyclerView.setAdapter(productAdapter);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
            dialog.dismiss();
        } else {
            Product p = (Product) o;
            Intent productDetails = new Intent(MainActivity.this, ProductDetailsActivity.class);
            productDetails.putExtra("activity", "main");
            productDetails.putExtra("product", p);
            productDetails.putExtra("currentGroupId", currentGroup.getId());
            startActivity(productDetails);
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

    @Override
    public void buy(Product product) {
        getIntent().putExtra("currentGroupId", currentGroup.getId());
        firebaseProductViewModel.setBought(product.getProductId(), currentFirebaseUser.getUid(), currentGroup.getId());
    }
}