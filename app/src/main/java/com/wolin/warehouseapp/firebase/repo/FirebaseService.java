package com.wolin.warehouseapp.firebase.repo;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
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
    private MutableLiveData<UserDetails> mutableLiveDataUser;
    private MutableLiveData<Group> mutableLiveDataGroup;
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
                                    firebaseFirestore.collection("Groups").document(groupId).collection("products").add(product);
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
        firebaseFirestore.collection("Groups").document(groupId).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Product> products = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot : task.getResult()) {
                    Product product = documentSnapshot.toObject(Product.class);
                    products.add(product);
                }
                System.out.println("PRODUCT CALLBACK");
                callback.onCallback(products);
            }
        });
    }


    //group

    public void addGroup(Group group) {
        firebaseFirestore.collection("Groups").document(group.getId()).set(group);
    }

    public void getGroup(String groupId, MyCallback<Group> callback) {
        System.out.println("POBIERAM GRUPE: " + groupId);
        firebaseFirestore.collection("Groups").document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println();
                    Group group = task.getResult().toObject(Group.class);
                    group.setId(group.getOwner() + "-" + group.getName());
                    getProducts(groupId, new MyCallback<List<Product>>() {
                        @Override
                        public void onCallback(List<Product> data) {
                            group.setProducts(data);
                            System.out.println("ZWRACAM GRUPE: " + group.getId()+ ":::" + group.getName()+ ":::" + group.getMembers()+ ":::" + group.getOwner() + ":::" + group.getProducts());
                            callback.onCallback(group);
                        }
                    });
                }
            }
        });
    }

    public LiveData<List<Group>> getGroups(String uid) {
        System.out.println("ROZPOCZYNAM POBIERANIE UZYTKOWNIKA");
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                System.out.println("POBRANO UZYTKOWNIKA");
                if(task.isSuccessful()) {
                    UserDetails user = task.getResult().toObject(UserDetails.class);
                    List<Group> groupsList = new ArrayList<>();
                    System.out.println("GRUPY UZYTKOWNIKA: " + user.getGroups());
                    groupsMutable.postValue(groupsList);
                    for(String groupId : user.getGroups()) {
                        getGroup(groupId, new MyCallback<Group>() {
                            @Override
                            public void onCallback(Group data) {
                                System.out.println("POBRANO GRUPE CALLBACK " + data.getName());
                                List<Group> tempList = groupsMutable.getValue();
                                tempList.add(data);
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
