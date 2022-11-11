package com.wolin.warehouseapp.firebase.repo;

import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wolin.warehouseapp.ui.AddActivity;
import com.wolin.warehouseapp.ui.MainActivity;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseService {

    private static FirebaseService firebaseService;
    private static FirebaseFirestore firebaseFirestore;
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

    //user

    public MutableLiveData<UserDetails> getUser(String uid) {
        DocumentReference docRef = firebaseFirestore.collection("Users").document(uid);
        Map<String, Object> data = docRef.get().getResult().getData();

        String email = (String) data.get("email");
        String name = (String) data.get("name");
        String lastName = (String) data.get("lastName");
        List<String> groups = (List<String>) data.get("groups");

        mutableLiveDataUser = new MutableLiveData<>();
        UserDetails userDetails = new UserDetails(uid, email, name, lastName, groups);
        mutableLiveDataUser.postValue(userDetails);

        return mutableLiveDataUser;
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

    public MutableLiveData<Group> getGroupMutableLiveData(String groupId) {
        DocumentReference docRef = firebaseFirestore.collection("Groups").document(groupId);
        Map<String, Object> data = docRef.get().getResult().getData();

        String name = (String) data.get("name");
        String owner = (String) data.get("owner");
        List<String> members = (List<String>) data.get("members");

        CollectionReference colRef = firebaseFirestore.collection("Groups").document(groupId).collection("products");
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Product> productList = new ArrayList<>();

                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        productList.add(doc.toObject(Product.class));
                    }
                }
                mutableLiveDataGroup = new MutableLiveData<>();
                Group group = new Group(name, owner);
                group.setMembers(members);
                group.setProducts(productList);
                mutableLiveDataGroup.postValue(group);
            }
        });
        return mutableLiveDataGroup;
    }
}
