package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.wolin.warehouseapp.firebase.repo.FirebaseUserRepo;
import com.wolin.warehouseapp.firebase.repo.OnDataUploaded;
import com.wolin.warehouseapp.room.viewmodel.UserRoomViewModel;
import com.wolin.warehouseapp.utils.model.UserDetails;

public class FirebaseUserViewModel extends ViewModel implements OnDataUploaded {
    private FirebaseUserRepo firebaseUserRepo;
    private MutableLiveData<Task<DocumentReference>> taskMutableLiveData;

    public MutableLiveData<Task<DocumentReference>> getTaskMutableLiveData() {
        return taskMutableLiveData;
    }

    public FirebaseUserViewModel(){
        firebaseUserRepo = new FirebaseUserRepo(this);
        taskMutableLiveData = new MutableLiveData<>();
    }

    public void registerUserToFirebase(UserDetails user, UserRoomViewModel userRoomViewModel){
        firebaseUserRepo.registerUser(user, userRoomViewModel);
    }

    public void getUser(String uid, UserRoomViewModel userRoomViewModel){
        firebaseUserRepo.getUser(uid, userRoomViewModel);
    }

    @Override
    public void onDataUpload(Task<DocumentReference> task) {
        taskMutableLiveData.setValue(task);
    }
}
