package com.wolin.warehouseapp.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.wolin.warehouseapp.firebase.repo.FirebasePhotoRepo;
import com.wolin.warehouseapp.room.viewmodel.PhotoRoomViewModel;

public class FirebaseViewModel extends ViewModel implements FirebasePhotoRepo.OnDataUploaded {

    private FirebasePhotoRepo firebasePhotoRepo;
    private MutableLiveData<Task<DocumentReference>> taskMutableLiveData;

    public MutableLiveData<Task<DocumentReference>> getTaskMutableLiveData() {
        return taskMutableLiveData;
    }

    public FirebaseViewModel(){
        firebasePhotoRepo = new FirebasePhotoRepo(this);
        taskMutableLiveData = new MutableLiveData<>();
    }

    public void uploadImagesToFirebase(Uri uri , PhotoRoomViewModel photoRoomViewModel){
        firebasePhotoRepo.uploadImage(uri , photoRoomViewModel);
    }

    public void getImagesFromFirebase(PhotoRoomViewModel photoRoomViewModel){
        firebasePhotoRepo.getImages(photoRoomViewModel);
    }

    @Override
    public void onDataUpload(Task<DocumentReference> task) {
        taskMutableLiveData.setValue(task);
    }
}