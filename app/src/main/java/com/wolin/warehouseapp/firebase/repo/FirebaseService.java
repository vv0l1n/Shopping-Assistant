package com.wolin.warehouseapp.firebase.repo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseService {

    private static FirebaseService firebaseService;
    private static FirebaseFirestore firebaseFirestore;
    DocumentReference userRef;
    private MutableLiveData<UserDetails> mutableLiveDataUser;
    private MutableLiveData<Group> mutableLiveDataGroup;
    private Product product;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProductPhotos");
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    public static FirebaseService getInstance() {
        if(firebaseService == null) {
            firebaseService = new FirebaseService();
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseService;
    }

    private FirebaseService() {}

    //user

    public void getUser(String uid, MyCallback<MutableLiveData<UserDetails>> callback) {
                    System.out.println("6");
                    firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                System.out.println("7");
                                UserDetails userDetails = task.getResult().toObject(UserDetails.class);
                                mutableLiveDataUser.postValue(userDetails);
                                callback.onCallback(mutableLiveDataUser);
                            }
                        }
                    });
            System.out.println("8");
    }


    public void registerUser(UserDetails user) {
        firebaseFirestore.collection("Users").document(user.getUid()).set(user);
    }


    //product

    public void insertProduct(Product product, String groupId) {
        if (product.getUri() != null) {
            StorageReference imageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
            imageRef.putFile(product.getUri()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.isComplete()) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uid = currentUser.getUid();
                                    product.setUrl(uri.toString());
                                    firebaseFirestore.collection("Groups/" + groupId + "/products/").add(product);
                                }
                            });
                        }
                    }
                }
            });
        } else {
            product.setUrl("none");
            firebaseFirestore.collection("Groups/" + groupId + "/products/").add(product);
        }
    }



    //group

    public void addGroup(Group group) {
        firebaseFirestore.collection("Groups").document(group.getId()).set(group);
    }

    public void getGroup(String groupId, MyCallback<Group> callback) {
        firebaseFirestore.collection("Groups").document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Group group = task.getResult().toObject(Group.class);
                    callback.onCallback(group);
                }
            }
        });
    }

    public void getGroups(String uid, MyCallback<Group> callback) {
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    UserDetails user = task.getResult().toObject(UserDetails.class);
                    List<Group> groups = new ArrayList<>();
                    for(String groupId : user.getGroups()) {
                        firebaseFirestore.collection("Groups").document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    Group group = task.getResult().toObject(Group.class);
                                    groups.add(group);
                                    System.out.println("GRUPA SERWIS: " + group);
                                    callback.onCallback(group);
                                }
                            }
                        });
                    }

                }
            }
        });
    }


    public MutableLiveData<UserDetails> getMutableLiveDataUser() {
        return mutableLiveDataUser;
    }

    public void setMutableLiveDataUser(MutableLiveData<UserDetails> mutableLiveDataUser) {
        this.mutableLiveDataUser = mutableLiveDataUser;
    }

    public MutableLiveData<Group> getMutableLiveDataGroup() {
        return mutableLiveDataGroup;
    }

    public void setMutableLiveDataGroup(MutableLiveData<Group> mutableLiveDataGroup) {
        this.mutableLiveDataGroup = mutableLiveDataGroup;
    }
}
