package com.wolin.warehouseapp.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.OnDataUploaded;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class FirebaseProductViewModel extends ViewModel {

    private FirebaseService firebaseService;
    private MutableLiveData<List<Product>> productListMutableLiveData;

    public MutableLiveData<List<Product>> getProductListMutableLiveData() {
        return productListMutableLiveData;
    }

    public FirebaseProductViewModel(){
        firebaseService = FirebaseService.getInstance();
        productListMutableLiveData = new MutableLiveData<>();
    }

    public void insertProduct(Product product, Uri uri, String groupId){
        System.out.println("GROUP ID VIEWMODEL: " + groupId);
        firebaseService.insertProduct(product, uri, groupId);
    }

    public void setBought(String productId, String uid, String groupId) {
        firebaseService.setBought(productId, uid, groupId);
    }


    /*@Override
    public void onDataUpload(Task<DocumentReference> task) {
        productListMutableLiveData.setValue(task);
    }*/
}