package com.wolin.warehouseapp.firebase.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.wolin.warehouseapp.firebase.repo.FirebaseService;
import com.wolin.warehouseapp.firebase.repo.OnDataUploaded;
import com.wolin.warehouseapp.utils.model.Product;

import java.util.List;

public class FirebaseProductViewModel extends ViewModel /*implements OnDataUploaded*/ {

    private FirebaseService firebaseService;
    private MutableLiveData<List<Product>> productListMutableLiveData;

    public MutableLiveData<List<Product>> getProductListMutableLiveData() {
        return productListMutableLiveData;
    }

    public FirebaseProductViewModel(){
        firebaseService = FirebaseService.getInstance();
        productListMutableLiveData = new MutableLiveData<>();
    }

    public void insertProduct(Product product, String groupId){
        firebaseService.insertProduct(product, groupId);
    }


    /*@Override
    public void onDataUpload(Task<DocumentReference> task) {
        productListMutableLiveData.setValue(task);
    }*/
}