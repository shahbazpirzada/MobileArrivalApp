package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Stech on 3/12/2018.
 */

public class GlobalClass  {

    private static GlobalClass instance;

    public String UrlPost;
    public String UserName;
    public String Password;
    public String BranchId;
    public String entryPoint;
    public String imagePath;
    public String getUrlPost() {
        return UrlPost;
    }
    public int BackgroundColor;

    public void setUrlPost(String urlPost) {
        UrlPost = urlPost;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getBackgroundColor() {
        return BackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        BackgroundColor = backgroundColor;
    }

    public static synchronized GlobalClass getInstance(){
        if(instance==null){
            instance=new GlobalClass();
        }
        return instance;
    }
}
