package com.wolin.warehouseapp.firebase.repo;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public interface OnDataUploaded{
    void onDataUpload(Task<DocumentReference> task);
}