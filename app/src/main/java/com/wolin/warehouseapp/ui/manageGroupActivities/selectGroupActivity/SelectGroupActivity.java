package com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.manageGroupActivities.manageGroupActivity.ManageGroupActivity;
import com.wolin.warehouseapp.ui.manageGroupActivities.selectGroupActivity.adapter.GroupAdapter;
import com.wolin.warehouseapp.utils.listeners.ItemSelectListener;
import com.wolin.warehouseapp.utils.model.Group;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupActivity extends AppCompatActivity implements ItemSelectListener<Group> {

    private TextView selectGroupTextView;
    private RecyclerView groupsRecyclerView;
    private Button backButton;

    private FirebaseGroupViewModel firebaseGroupViewModel;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);

        selectGroupTextView = findViewById(R.id.selectGroupTextView);
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView);
        backButton = findViewById(R.id.selectGroupButton);

        firebaseGroupViewModel = new ViewModelProvider(this).get(FirebaseGroupViewModel.class);

        loadRecycler();
    }

    private void loadRecycler() {

        GroupAdapter adapter = new GroupAdapter(this, new ArrayList<>(), currentFirebaseUser.getUid(), this);
        groupsRecyclerView.setAdapter(adapter);
        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseGroupViewModel.getGroups(currentFirebaseUser.getUid()).observe(this, (List<Group> groups) -> {
            if (groups.size() > 0) {
                selectGroupTextView.setText(R.string.selectGroup);
                adapter.updateData(groups);
                adapter.notifyDataSetChanged();
            } else {
                selectGroupTextView.setText(R.string.youHaveNoGroups);
            }
        });
    }

    public void onSelectGroupButtonClick(View view) {
        Intent mainIntent = new Intent(SelectGroupActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public void onItemClick(Group item) {
        Intent manageIntent = new Intent(SelectGroupActivity.this, ManageGroupActivity.class);
        manageIntent.putExtra("group", item.getId());
        startActivity(manageIntent);
    }
}
