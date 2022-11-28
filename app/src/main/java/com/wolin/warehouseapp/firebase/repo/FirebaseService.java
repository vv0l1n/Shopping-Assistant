package com.wolin.warehouseapp.firebase.repo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {

    private static FirebaseService firebaseService;
    private static FirebaseFirestore firebaseFirestore;
    DocumentReference userRef;
    private MutableLiveData<UserDetails> mutableLiveDataUser = new MutableLiveData<>();
    private MutableLiveData<Group> mutableLiveDataGroup = new MutableLiveData<>();
    private MutableLiveData<List<Group>> groupsMutable = new MutableLiveData<>();
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

    public void getUser(String uid, MyCallback<UserDetails> callback) {
                    System.out.println("6 " + uid);
                    firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                System.out.println(task.getResult().get("uid"));
                                UserDetails userDetails = task.getResult().toObject(UserDetails.class);
                                callback.onCallback(userDetails);
                            }
                        }
                    });
            System.out.println("8");
    }


    public void registerUser(UserDetails user) {
        firebaseFirestore.collection("Users").document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    List<String> emptyList = new ArrayList<>();
                    addGroup(new Group("Prywatna lista", user.getUid()), emptyList);
                }
            }
        });
    }


    //product

    public void insertProduct(Product product, Uri uri, String groupId) {
        System.out.println("ID GRUPY: " + groupId);
        if (uri != null) {
            StorageReference imageRef = storageReference.child(String.valueOf(System.currentTimeMillis()));
            imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.isComplete()) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    product.setPhoto(uri.toString());
                                    firebaseFirestore.collection("Groups").document(groupId).collection("products").document(product.getProductId()).set(product);
                                }
                            });
                        }
                    }
                }
            });
        } else {
            firebaseFirestore.collection("Groups").document(groupId).collection("products").add(product);
        }
    }


    public void getProducts(String groupId, MyCallback<List<Product>> callback) {
        System.out.println("POBIERAM PRODUKTY GRUPY: " + groupId);
        CollectionReference productsColRef = firebaseFirestore.collection("Groups").document(groupId).collection("products");
        productsColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Product> products = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    Product product = documentSnapshot.toObject(Product.class);
                    products.add(product);
                }
                System.out.println("PRODUCT CALLBACK: " + products);
                callback.onCallback(products);
            }
        });
    }

    public void setBought(String productId, String uid, String groupId) {
        System.out.println("Zmieniam pole active: " + productId + " " + groupId);
        firebaseFirestore.collection("Groups").document(groupId).collection("products").document(productId).update("active", false, "buyer", uid);
    }


    //group

    public void addGroup(Group group, List<String> groups) {
        groups.add(group.getId());
        firebaseFirestore.collection("Groups").document(group.getId()).set(group);
        firebaseFirestore.collection("Users").document(group.getOwner()).update("groups", groups);
    }

    public void getGroup(String groupId, MyCallback<Group> callback) {
        System.out.println("POBIERAM GRUPE: " + groupId);
        DocumentReference groupDocRef = firebaseFirestore.collection("Groups").document(groupId);
        groupDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Group group = value.toObject(Group.class);
                group.setId(group.getOwner() + "-" + group.getName());
                getProducts(groupId, new MyCallback<List<Product>>() {
                    @Override
                    public void onCallback(List<Product> data) {
                        group.setProducts(data);
                        System.out.println("ZWRACAM GRUPE: " + group.getId() + ":::" + group.getName() + ":::" + group.getMembers() + ":::" + group.getOwner() + ":::" + group.getProducts());
                        callback.onCallback(group);
                    }
                });
            }
        });
    }

    public LiveData<List<Group>> getGroups(String uid) {
        System.out.println("ROZPOCZYNAM POBIERANIE UZYTKOWNIKA");
        DocumentReference userDocRef = firebaseFirestore.collection("Users").document(uid);
        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                System.out.println("POBRANO UZYTKOWNIKA");
                if(value.exists()) {
                    UserDetails user = value.toObject(UserDetails.class);
                    List<Group> groupsList = new ArrayList<>();
                    System.out.println("GRUPY UZYTKOWNIKA: " + user.getGroups());
                    groupsMutable.postValue(groupsList);
                    for(String groupId : user.getGroups()) {
                        getGroup(groupId, new MyCallback<Group>() {
                            @Override
                            public void onCallback(Group data) {
                                System.out.println("POBRANO GRUPE CALLBACK " + data.getName() + " " + data.getProducts());
                                List<Group> tempList = groupsMutable.getValue();
                                boolean found = false;
                                for(int i = 0; i < tempList.size(); i++) {
                                    if(tempList.get(i).getId().equals(data.getId())) {
                                        tempList.set(i, data);
                                        found = true;
                                    }
                                }
                                if(!found) {
                                    tempList.add(data);
                                }
                                groupsMutable.postValue(tempList);
                                System.out.println("GRUPY: " + groupsMutable.getValue());
                            }
                        });
                    }
                }
            }
        });
        System.out.println("ZWRACAM LIVEDATA");
        return groupsMutable;
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
