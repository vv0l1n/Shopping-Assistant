package com.wolin.warehouseapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.utils.adapter.ItemSelectListener;
import com.wolin.warehouseapp.utils.adapter.MainActivityGroupAdapter;
import com.wolin.warehouseapp.utils.adapter.MainAdapter;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ItemSelectListener<Group> {

    private Dialog dialog;
    private CheckBox onlyActive;
    private ImageButton sortButton;
    private ImageButton filterButton;
    private ImageButton groupButton;
    private ImageButton hamburger;
    private RecyclerView productRecyclerView;
    private Button addButton;
    private RecyclerView groupRecyclerView;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Group currentGroup;
    LiveData<UserDetails> currentUser;
    List<Group> userGroups;

    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseProductViewModel firebaseProductViewModel;
    private FirebaseGroupViewModel firebaseGroupViewModel;

    private MutableLiveData<Map<String, String>> groupMap;

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

        userGroups = new ArrayList<>();

        firebaseProductViewModel = new ViewModelProvider(this).get(FirebaseProductViewModel.class);
        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        List<Product> testItems = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MainAdapter(getApplicationContext(), testItems));


        loadDialog(addButton.getRootView());
    }

    public void onMainActivityAddButtonClick(View view) {
        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(addIntent);
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

        firebaseGroupViewModel.getGroups(currentFirebaseUser.getUid()).observe(this, groups -> {
            System.out.println("OBSERVER: " + groups);
            adapter.updateData(groups);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(Group item) {
        System.out.println(item.getId() + "KIKNIETO");
        currentGroup = item;
        dialog.dismiss();
    }
}