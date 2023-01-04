package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.utils.model.GroupInvite;
import com.wolin.warehouseapp.utils.model.User;

import java.util.List;

public class FirebaseInviteViewModel extends ViewModel {
    private FirebaseService firebaseService;
    private MutableLiveData<List<GroupInvite>> inviteListLiveData;

    public FirebaseInviteViewModel() {
        firebaseService = FirebaseService.getInstance();
    }

    public void inviteUser(String target, String inviterUid, String groupId) {
        firebaseService.inviteUser(target, inviterUid, groupId);
    }

    public LiveData<List<GroupInvite>> getInvites(String uid) {
        if (inviteListLiveData == null) {
            inviteListLiveData = new MutableLiveData<List<GroupInvite>>();
            loadInvites(uid);
        }
        return inviteListLiveData;
    }

    private void loadInvites(String uid) {
        firebaseService.getInvites(uid, new MyCallback<List<GroupInvite>>() {
            @Override
            public void onCallback(List<GroupInvite> data) {
                inviteListLiveData.postValue(data);
            }
        });
    }

    public void declineInvite(GroupInvite invite) {
        firebaseService.declineInvite(invite);
    }

    public void acceptInvite(GroupInvite invite, User user) {
        firebaseService.acceptInvite(invite, user);
    }

}
