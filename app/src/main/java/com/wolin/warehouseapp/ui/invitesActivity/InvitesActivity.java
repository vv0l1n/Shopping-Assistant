package com.wolin.warehouseapp.ui.invitesActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wolin.warehouseapp.R;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseGroupViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseInviteViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseProductViewModel;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.ui.invitesActivity.adapter.InvitesAdapter;
import com.wolin.warehouseapp.ui.mainActivity.MainActivity;
import com.wolin.warehouseapp.ui.yourProductsActivity.YourProductsActivity;
import com.wolin.warehouseapp.utils.listeners.ButtonClickedListener;
import com.wolin.warehouseapp.utils.model.GroupInvite;
import com.wolin.warehouseapp.utils.model.User;

import java.util.ArrayList;
import java.util.List;

public class InvitesActivity extends AppCompatActivity implements ButtonClickedListener {

    private TextView noInvitesTextView;
    private RecyclerView recycler;
    private Button backButton;

    private FirebaseInviteViewModel firebaseInviteViewModel;

    private FirebaseUserViewModel firebaseUserViewModel;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private InvitesAdapter adapter;
    private ArrayList<GroupInvite> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        noInvitesTextView = findViewById(R.id.invitesNoInvitesTextView);
        recycler = findViewById(R.id.invitesRecycler);
        backButton = findViewById(R.id.invitesBackButton);

        firebaseInviteViewModel = new ViewModelProvider(this).get(FirebaseInviteViewModel.class);
        firebaseUserViewModel = new ViewModelProvider(this).get(FirebaseUserViewModel.class);

        adapter = new InvitesAdapter(list, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadRecycler();

    }

    private void loadRecycler() {
        firebaseInviteViewModel.getInvites(currentFirebaseUser.getUid()).observe(this, new Observer<List<GroupInvite>>() {
            @Override
            public void onChanged(List<GroupInvite> groupInvites) {
                if(groupInvites.size() > 0) {
                    noInvitesTextView.setVisibility((View.GONE));
                } else {
                    noInvitesTextView.setVisibility(View.VISIBLE);
                }
                groupInvites.forEach(i -> System.out.println(i));
                adapter.update(groupInvites);
            }
        });
    }

    public void onInviteBackButtonClick(View view) {
        String lastIntent = getIntent().getExtras().getString("lastIntent");
        Intent back;
        if(lastIntent.equals("main")) {
            back = new Intent(InvitesActivity.this, MainActivity.class);
        } else {
            back = new Intent(InvitesActivity.this, YourProductsActivity.class);
        }
        startActivity(back);
    }

    @Override
    public void clicked(GroupInvite invite, String buttonName) {
        if(buttonName.equals("accept")) {
            System.out.println("akceptuje");
            firebaseUserViewModel.getUser(currentFirebaseUser.getUid()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(user != null) {
                        firebaseInviteViewModel.acceptInvite(invite, user);
                    }
                }
            });
        } else {
            System.out.println("usuwam");
            firebaseUserViewModel.getUser(currentFirebaseUser.getUid()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(user != null) {
                        firebaseInviteViewModel.declineInvite(invite, user.getEmail());
                    }
                }
            });
        }
    }
}
