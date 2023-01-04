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
        final CharSequence[] categories = {"Odzież", "Żywność", "Użytek domowy", "Inne", "Wszystkie"};
        AlertDialog.Builder builder = new AlertDialog.Builder(YourProductsActivity.this);
        builder.setTitle("Wybierz kategorię");
        builder.setItems(categories, (dialog, item) -> {
            switch (categories[item].toString()) {
                case "Odzież":
                    currentCategory = Category.CLOTHES;
                    break;
                case "Żywność":
                    currentCategory = Category.FOOD;
                    break;
                case "Użytek domowy":
                    currentCategory = Category.HOME;
                    break;
                case "Inne":
                    currentCategory = Category.OTHERS;
                    break;
                case "Wszystkie":
                    currentCategory = Category.NONE;
                    break;
            }
            System.out.println("IS SELECTED: " + selected);
            productAdapterYPA.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
        });
        builder.show();
    }

    public void onYourProductsSortButtonClick(View view) {
        final CharSequence[] categories = {"Najważniejsze", "Najmniej ważne", "Najbliższy termin zakupu",
                "Najpóźniejszy termin zakupu", "Domyślnie"};
        AlertDialog.Builder builder = new AlertDialog.Builder(YourProductsActivity.this);
        builder.setTitle("Sortuj produkty");
        builder.setItems(categories, (dialog, item) -> {
            switch (categories[item].toString()) {
                case "Najważniejsze":
                    sortState = SortState.DESCPRIORITY;
                    break;
                case "Najmniej ważne":
                    sortState = SortState.ASCPRIORITY;
                    break;
                case "Najbliższy termin zakupu":
                    sortState = SortState.DESCDATE;
                    break;
                case "Najpóźniejszy termin zakupu":
                    sortState = SortState.ASCDATE;
                    break;
                case "Domyślnie":
                    sortState = SortState.NONE;
                    break;
            }
            productAdapterYPA.updateData(currentGroup.getProducts(), selected, currentCategory, sortState);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
        });
        builder.show();
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
