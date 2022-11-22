package com.wolin.warehouseapp.ui.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.ui.addactivity.AddActivity;
import com.wolin.warehouseapp.ui.mainactivity.adapter.productadapter.ProductAdapter;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.mainactivity.adapter.groupadapter.MainActivityGroupAdapter;
import com.wolin.warehouseapp.utils.listeners.ItemBuyListener;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemSelectListener<Object> {

    private Dialog dialog;
    private CheckBox onlyActive;
    private ImageButton sortButton;
    private ImageButton filterButton;
    private ImageButton groupButton;
    private ImageButton hamburger;
    private RecyclerView productRecyclerView;
    private Button addButton;
    private RecyclerView groupRecyclerView;
    private TextView actualGroupTextView;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Group currentGroup;
    private List<Group> userGroups;

    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseProductViewModel firebaseProductViewModel;
    private FirebaseGroupViewModel firebaseGroupViewModel;

    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        onlyActive = findViewById(R.id.onlyActive);
        sortButton = findViewById(R.id.sortButton);
        filterButton = findViewById(R.id.filterButton);
        groupButton = findViewById(R.id.groupButton);
        hamburger = findViewById(R.id.hamburger);
        productRecyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        actualGroupTextView = findViewById(R.id.actualGroupTextView);

        currentGroup = new Group("brak", "brak");
        currentGroup.setProducts(new ArrayList<>());
        userGroups = new ArrayList<>();

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        productAdapter = new ProductAdapter(this, currentGroup.getProducts(), this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid());
        productRecyclerView.setAdapter(productAdapter);

        actualGroupTextView.setText("Aktualna grupa: brak");

        loadDialog(addButton.getRootView());
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
                currentGroup = groups.get(0);
                updateCurrentGroupName();
                adapter.updateData(groups);
                adapter.notifyDataSetChanged();
                //productAdapter.updateData(currentGroup.getProducts(), currentGroup.getId());
                productAdapter = new ProductAdapter(this, currentGroup.getProducts(), this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid());
                productRecyclerView.setAdapter(productAdapter);
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
            productAdapter = new ProductAdapter(this, currentGroup.getProducts(), this, firebaseProductViewModel, currentGroup.getId(), currentFirebaseUser.getUid());
            productRecyclerView.setAdapter(productAdapter);
            productRecyclerView.refreshDrawableState();
            productRecyclerView.getRecycledViewPool().clear();
            //productAdapter.updateData(currentGroup.getProducts(), currentGroup.getId());
            //productAdapter.notifyDataSetChanged();
            dialog.dismiss();
        } else {
            Product p = (Product) o;
            System.out.println("KLIKNIETO PRODUKT: " + p.getName());
        }
    }
}