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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wolin.warehouseapp.utils.model.Group;
import com.wolin.warehouseapp.utils.model.GroupInvite;
import com.wolin.warehouseapp.utils.model.Product;
import com.wolin.warehouseapp.utils.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseService {

    private static FirebaseService firebaseService;
    private static FirebaseFirestore firebaseFirestore;
    private final MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
    private final MutableLiveData<Group> mutableLiveDataGroup = new MutableLiveData<>();
    private final MutableLiveData<List<Group>> groupsMutable = new MutableLiveData<>();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProductPhotos");


    public static FirebaseService getInstance() {
        if(firebaseService == null) {
            firebaseService = new FirebaseService();
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseService;
    }

    private FirebaseService() {}

    //user

    public void getUser(String uid, MyCallback<User> callback) {
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    callback.onCallback(user);
                }
            }
        });
    }



    public void registerUser(User user) {
        firebaseFirestore.collection("Users").document(user.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    //add his email to EmailToID collection
                    HashMap<String, String> uid = new HashMap<>();
                    uid.put("uid", user.getUid());
                    firebaseFirestore.collection("EmailToID").document(user.getEmail()).set(uid);

                    //create his private list
                    //empty list is because its not main screen so we dont have to pass current groups
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
            firebaseFirestore.collection("Groups").document(groupId).collection("products").document(product.getProductId()).set(product);
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

    public void deleteProduct(String productId, String uid, String groupId) {
        firebaseFirestore.collection("Groups").document(groupId).collection("products").document(productId).delete();
    }

    public void getProduct(String productId, String groupId, MyCallback<Product> callback) {
        firebaseFirestore.collection("Groups").document(groupId).collection("products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println(task.getResult().get("uid"));
                    Product product = task.getResult().toObject(Product.class);
                    callback.onCallback(product);
                }
            }
        });
    }

    public void updateProduct(Product product, Uri uri, String groupId) {
        System.out.println("ID GRUPY: " + groupId);
        System.out.println("ZDJECIE: " + uri);
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
                                    HashMap shop = new HashMap<String, String>();
                                    shop.put("name", product.getShop().getName());
                                    shop.put("shopLogo", product.getShop().getShopLogo());
                                    firebaseFirestore.collection("Groups").document(groupId).collection("products").document(product.getProductId()).set(product);
                                    System.out.println("photo: " + product.getPhoto());
                                    System.out.println("ZAKTUALIZOWANO PRODUKT: " + product);
                                }
                            });
                        }
                    }
                }
            });
        } else {
            firebaseFirestore.collection("Groups").document(groupId).collection("products").document(product.getProductId()).set(product);
        }
    }


    //group

    public void addGroup(Group group, List<String> groups) {
        groups.add(group.getId());
        firebaseFirestore.collection("Groups").document(group.getId()).set(group);
        firebaseFirestore.collection("Users").document(group.getOwner()).update("groups", groups);
    }

    public void getGroup(String groupId, MyCallback<Group> callback) {
        System.out.println("POBIERAM GRUPE: " + groupId);
        firebaseFirestore.collection("Groups").document(groupId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Group group = value.toObject(Group.class);
                //group.setId(group.getOwner() + "-" + group.getName());
                if(group != null) {
                    getProducts(groupId, new MyCallback<List<Product>>() {
                        @Override
                        public void onCallback(List<Product> data) {
                            group.setProducts(data);
                            System.out.println("ZWRACAM GRUPE: " + group.getId() + ":::" + group.getName() + ":::" + group.getMembers() + ":::" + group.getOwner() + ":::" + group.getProducts());
                            callback.onCallback(group);
                        }
                    });
                }
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
                    User user = value.toObject(User.class);
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

    public void deleteFromGroup(String uid, String groupId) {
        //deleting from User collection
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    ArrayList<String> newUserGroups = new ArrayList<>();
                    User user = task.getResult().toObject(User.class);
                    for(String groupIdLoop : user.getGroups()) {
                        if(!groupId.equals(groupIdLoop)) {
                            newUserGroups.add(groupIdLoop);
                        }
                    }
                    firebaseFirestore.collection("Users").document(uid).update("groups", newUserGroups);
                }
            }
        });
        //deleting from Groups collection
        firebaseFirestore.collection("Groups").document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    ArrayList<String> newMembers = new ArrayList<>();
                    Group group = task.getResult().toObject(Group.class);
                    for(String memberId : group.getMembers()) {
                        if(!memberId.equals(uid)) {
                            newMembers.add(memberId);
                        }
                    }
                    firebaseFirestore.collection("Groups").document(groupId).update("members", newMembers);
                }
            }
        });
    }

    public void deleteUserProductsInGroup(String uid, String groupId) {
        firebaseFirestore.collection("Groups").document(groupId).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()) {
                    if(task.isSuccessful()) {
                        List<Product> products = task.getResult().toObjects(Product.class);
                        for(Product product : products) {
                            if(product.getOwner().equals(uid)) {
                                firebaseFirestore.collection("Groups").document(groupId).collection("products").document(product.getProductId()).delete();
                            }
                        }
                    }
                }
            }
        });
    }


    public void getSimpleGroup(String groupId, MyCallback<Group> callback) {
        firebaseFirestore.collection("Groups").document(groupId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null) {
                    callback.onCallback(value.toObject(Group.class));
                }
            }
        });
    }

    //invites

    public void inviteUser(String target, String inviterUid, String groupId) {
        firebaseFirestore.collection("EmailToID").document(target).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()) { //check if user with that email exists
                    String targetUid = (String) task.getResult().get("uid");
                    firebaseFirestore.collection("EmailToID").document(target).collection("Invites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                List<DocumentSnapshot> results = task.getResult().getDocuments();
                                for(DocumentSnapshot doc : results) {
                                    GroupInvite inv = doc.toObject(GroupInvite.class);
                                    if (inv.getGroupId().equals(groupId)) { //check if the user dont already have invite to that group
                                        return;
                                    }
                                }
                                firebaseFirestore.collection("Users").document(targetUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            User userTarget = task.getResult().toObject(User.class);
                                            if(userTarget.getGroups().contains(groupId)) { //check if user is not already in that group
                                                return;
                                            } else {
                                                firebaseFirestore.collection("Users").document(inviterUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()) {
                                                            User userInviter = task.getResult().toObject(User.class);
                                                            String groupName = groupId.substring(groupId.indexOf('-') + 1);
                                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                                            String formattedDate = formatter.format(LocalDateTime.now());
                                                            GroupInvite invite = new GroupInvite(groupId, groupName, userInviter.getEmail(), target,formattedDate);
                                                            firebaseFirestore.collection("EmailToID").document(target).collection("Invites").add(invite);

                                                            firebaseFirestore.collection("Groups").document(groupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    Group group = documentSnapshot.toObject(Group.class);
                                                                    List<GroupInvite> invites = group.getInvites();
                                                                    if(invites == null || invites.size() > 0) {
                                                                        invites = new ArrayList<>();
                                                                    }
                                                                    invites.add(invite);
                                                                    firebaseFirestore.collection("Groups").document(groupId).update("invites", invites);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    public void getInvites(String uid, MyCallback<List<GroupInvite>> callback) {
        firebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    firebaseFirestore.collection("EmailToID").document(user.getEmail()).collection("Invites").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error == null) {
                                ArrayList<GroupInvite> invites = new ArrayList<>();
                                for(DocumentSnapshot doc : value) {
                                    invites.add(doc.toObject(GroupInvite.class));
                                }
                                callback.onCallback(invites);
                            }
                        }
                    });
                }
            }
        });
    }



    public void declineInvite(GroupInvite invite) {
        firebaseFirestore.collection("EmailToID").document(invite.getTargetEmail()).collection("Invites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    String inviteID;
                    for(DocumentSnapshot doc : docs) {
                        if(doc.toObject(GroupInvite.class).getGroupId().equals(invite.getGroupId())) {
                            inviteID = doc.getId();
                            firebaseFirestore.collection("EmailToID").document(invite.getTargetEmail()).collection("Invites").document(inviteID).delete();
                        }
                    }
                }
            }
        });
    }

    public void acceptInvite(GroupInvite invite, User user) {
        firebaseFirestore.collection("EmailToID").document(user.getEmail()).collection("Invites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    String inviteID;
                    for(DocumentSnapshot doc : docs) {
                        if(doc.toObject(GroupInvite.class).getGroupId().equals(invite.getGroupId())) {
                            inviteID = doc.getId();
                            firebaseFirestore.collection("EmailToID").document(user.getEmail()).collection("Invites").document(inviteID).delete();
                        }
                    }
                    firebaseFirestore.collection("Groups").document(invite.getGroupId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                List<String> members = (List<String>) task.getResult().get("members");
                                members.add(user.getUid());
                                firebaseFirestore.collection("Groups").document(invite.getGroupId()).update("members", members);
                            }
                        }
                    });
                    firebaseFirestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                List<String> groups = (List<String>) task.getResult().get("groups");
                                groups.add(invite.getGroupId());
                                firebaseFirestore.collection("Users").document(user.getUid()).update("groups", groups);
                            }
                        }
                    });
                }
            }
        });
    }

    public void deleteGroup(String groupId) {
        firebaseFirestore.collection("Groups").document(groupId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Group group = task.getResult().toObject(Group.class);

                    //delete members
                    for(String uid : group.getMembers()) {
                        deleteFromGroup(uid, groupId);
                    }

                    if(group.getInvites() != null && group.getInvites().size() > 0) {
                        for(GroupInvite invite : group.getInvites()) {
                            //delete invites
                            declineInvite(invite);
                        }
                    }

                    //delete products
                    firebaseFirestore.collection("Groups").document(groupId).collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc : task.getResult()) {
                                firebaseFirestore.collection("Groups").document(groupId).collection("products")
                                        .document(doc.getId()).delete();
                            }
                        }
                    });
                    firebaseFirestore.collection("Groups").document(groupId).delete();
                }
            }
        });
    }
}
