package com.wolin.warehouseapp.firebase.repo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wolin.warehouseapp.firebase.viewmodel.FirebaseUserViewModel;
import com.wolin.warehouseapp.room.viewmodel.PhotoRoomViewModel;
import com.wolin.warehouseapp.room.viewmodel.UserRoomViewModel;
import com.wolin.warehouseapp.utils.model.Photo;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;

public class FirebasePhotoRepo {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private OnDataUploaded onDataUploaded;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    public FirebasePhotoRepo(OnDataUploaded onDataUploaded){
        this.onDataUploaded = onDataUploaded;
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("Users").child("products");
    }

    public void uploadImage(Uri uri, PhotoRoomViewModel photoRoomViewModel, Long lastProductID, Product product){
        StorageReference imageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
        imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.isComplete()){
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Photo photo = new Photo();
                                photo.setImageURL(uri.toString());
                                String uid = currentFirebaseUser.getUid();
                                product.setUrl(uri.toString());
                                firebaseFirestore.collection("Users" + currentFirebaseUser.getUid() + "/products/" + lastProductID.toString())
                                        .add(product)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()){
                                                    photoRoomViewModel.insertPhoto(photo);
                                                    DocumentReference changeLastId = firebaseFirestore.collection("Users/" + uid).document("details");
                                                    changeLastId.update("lastProductID", (lastProductID+1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            System.out.println("dodano");
                                                        }
                                                    });
                                                }
                                                onDataUploaded.onDataUpload(task);
                                            }
                                        });
                            }
                        });
                    }
                }
            }
        });
    }

    public void getImages(PhotoRoomViewModel photoRoomViewModel){
        firebaseFirestore.collection("images").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        Photo photo = doc.getDocument().toObject(Photo.class);
                        photoRoomViewModel.insertPhoto(photo);
                    }
                }
            }
        });
    }
}