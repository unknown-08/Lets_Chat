package com.example.letschat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class UIutils {
    private Activity mActivity;
    private  Context context;

    public UIutils(Activity activity){
        mActivity = activity;
    }

    public UIutils(Context context){
        this.context=context;
    }

    public void showPhoto(Uri photoUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        mActivity.startActivity(intent);
    }

    public void showPhotoContext(Uri photoUri){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        context.startActivity(intent);
    }
}
