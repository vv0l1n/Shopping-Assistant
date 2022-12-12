package com.wolin.warehouseapp.firebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.MyCallback;
import com.wolin.warehouseapp.utils.model.Product;


public class FirebaseProductViewModel extends ViewModel {

    private FirebaseService firebaseService;
    private MutableLiveData<Product> productLiveData;

    public FirebaseProductViewModel(){
        firebaseService = FirebaseService.getInstance();
    }

    public void insertProduct(Product product, Uri uri, String groupId){
        firebaseService.insertProduct(product, uri, groupId);
    }

    public void setBought(String productId, String uid, String groupId) {
        firebaseService.setBought(productId, uid, groupId);
    }

    public void deleteProduct(String productId, String uid, String groupId) {
        firebaseService.deleteProduct(productId, uid, groupId);
    }

    public LiveData<Product> getProduct(String productId, String groupId) {
        if(productLiveData == null) {
            productLiveData = new MutableLiveData<Product>();
            loadProduct(productId, groupId);
        }
        return productLiveData;
    }

    private void loadProduct(String productId, String groupId) {
        firebaseService.getProduct(productId, groupId, new MyCallback<Product>() {
            @Override
            public void onCallback(Product data) {
                productLiveData.postValue(data);
            }
        });
    }

    public void update(Product product, Uri mImageURI, String currentGroupId) {
        firebaseService.updateProduct(product, mImageURI, currentGroupId);
    }
}