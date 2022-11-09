package com.wolin.warehouseapp.firebase.repo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wolin.warehouseapp.room.viewmodel.UserRoomViewModel;
import com.wolin.warehouseapp.utils.model.UserDetails;

public class FirebaseUserRepo {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private OnDataUploaded onDataUploaded;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    public FirebaseUserRepo(OnDataUploaded onDataUploaded){
        this.onDataUploaded = onDataUploaded;
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Users");
    }

    public void registerUser(UserDetails user, UserRoomViewModel userRoomViewModel){
        firebaseFirestore.collection("Users").document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isComplete()) {
                    if(task.isSuccessful()) {
                        userRoomViewModel.insertUser(user);
                    }
                    onDataUploaded.onDataUpload(task);
                }
            }
        });
    }

    public void getUser(String uid, UserRoomViewModel userRoomViewModel) {
        DocumentReference docRef = firebaseFirestore.collection("Users/" + uid).document("details");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserDetails userDetails = document.toObject(UserDetails.class);
                        if(userDetails != null) {
                            userRoomViewModel.insertUser(userDetails);
                        }
                    } else {
                        System.out.println("Brak dokumentu");
                    }
                } else {
                    System.out.println(task.getException());
                }
            }
        });
    }
}