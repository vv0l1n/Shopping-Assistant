package com.wolin.warehouseapp.ui.yourProductsActivity;

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
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseInviteViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.createGroupActivity.CreateGroupActivity;
import com.wolin.warehouseapp.ui.editProductActivity.EditProductActivity;
import com.wolin.warehouseapp.ui.invitesActivity.InvitesActivity;
import com.wolin.warehouseapp.ui.loginActivity.LoginActivity;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.mainActivity.adapter.groupadapter.MainActivityGroupAdapter;
import com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.SelectGroupActivity;
import com.wolin.warehouseapp.ui.productDetails.ProductDetailsActivity;
import com.wolin.warehouseapp.ui.profileActivity.ProfileActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.productadapter.ProductAdapterYPA;
import com.wolin.warehouseapp.utils.common.Category;
import com.wolin.warehouseapp.utils.common.SortState;
import com.wolin.warehouseapp.utils.listeners.ItemDeleteListener;
import com.wolin.warehouseapp.utils.listeners.ItemEditListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
import java.util.List;

public class YourProductsActivity extends AppCompatActivity implements ItemSelectListener<Object>, ItemEditListener<Product>, ItemDeleteListener, NavigationView.OnNavigationItemSelectedListener {

    private Dialog dialog;
    private Dialog sortDialog;
    private Dialog filterDialog;
    private CheckBox onlyActive;
    private ImageButton sortButton;
    private ImageButton filterButton;
    private ImageButton groupButton;
    private RecyclerView productRecyclerView;
    private RecyclerView groupRecyclerView;
    private TextView actualGroupTextView;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Group currentGroup;
    private Category currentCategory = Category.NONE;
    private boolean selected = true;
    private SortState sortState = SortState.NONE;
    private List<Group> userGroups;
    private DrawerLayout drawerLayout;
    private NavigationView navViev;
    private Toolbar toolbar;

    private FirebaseAuth auth;

    private FirebaseInviteViewModel firebaseInviteViewModel;
    private FirebaseProductViewModel firebaseProductViewModel;
    private FirebaseGroupViewModel firebaseGroupViewModel;

    private ProductAdapterYPA productAdapterYPA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_products);

        onlyActive = findViewById(R.id.onlyActiveYPA);
        onlyActive.setSelected(true);
        onlyActiveListener();
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
        firebaseInviteViewModel = new ViewModelProvider(this).get(FirebaseInviteViewModel.class);

        productAdapterYPA = new ProductAdapterYPA( currentGroup.getProducts(), this, this, this, currentFirebaseUser.getUid(), getResources());
        productRecyclerView.setAdapter(productAdapterYPA);

        actualGroupTextView.setText("Aktualna grupa: brak");

        loadDialog();
        loadSortDialog();
        loadFilterDialog();
        loadInvites();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navViev.setNavigationItemSelectedListener(this);
    }

    private void onlyActiveListener() {
        onlyActive.setOnCheckedChangeListener((compoundButton, b) -> {
            selected = !selected;
            productAdapterYPA.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
        });
    }

    public void onYourProductsFilterButtonClick(View view) {
        filterDialog.show();
    }

    public void onYourProductsSortButtonClick(View view) {
        sortDialog.show();
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
        productAdapterYPA.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
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
        productAdapterYPA.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
        productRecyclerView.refreshDrawableState();
        productRecyclerView.getRecycledViewPool().clear();
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
                productAdapterYPA.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
                productRecyclerView.refreshDrawableState();
                productRecyclerView.getRecycledViewPool().clear();
            }
        });

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
            productAdapterYPA = new ProductAdapterYPA(currentGroup.getProducts(), this, this, this, currentFirebaseUser.getUid(), getResources());
            productRecyclerView.setAdapter(productAdapterYPA);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
            dialog.dismiss();
        } else {
            Product p = (Product) o;
            Intent productDetails = new Intent(YourProductsActivity.this, ProductDetailsActivity.class);
            productDetails.putExtra("activity", "yourProducts");
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
                Intent intentProfile = new Intent(YourProductsActivity.this, ProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.nav_add_group:
                Intent addGroup = new Intent(YourProductsActivity.this, CreateGroupActivity.class);
                startActivity(addGroup);
                break;
            case R.id.nav_your_products:
                break;
            case R.id.nav_list:
                Intent main = new Intent(YourProductsActivity.this, MainActivity.class);
                main.putExtra("currentGroupId", currentGroup.getId());
                startActivity(main);
                break;
            case R.id.nav_manage_group:
                Intent intentManageGroups = new Intent(YourProductsActivity.this, SelectGroupActivity.class);
                startActivity(intentManageGroups);
                break;
            case R.id.nav_invites:
                Intent intentInvites = new Intent(YourProductsActivity.this, InvitesActivity.class);
                intentInvites.putExtra("lastIntent", "yp");
                startActivity(intentInvites);
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

    @Override
    public void delete(Product product) {
        firebaseProductViewModel.deleteProduct(product.getProductId(), product.getOwner(), currentGroup.getId());
    }
}
