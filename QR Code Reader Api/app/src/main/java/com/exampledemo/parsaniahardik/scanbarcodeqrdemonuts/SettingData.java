package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import com.orm.SugarRecord;

/**
 * Created by Stech on 3/1/2018.
 */

public class SettingData extends SugarRecord {

    String UrlLink;
    String UserNameData;
    String PasswordData;
    String BranchID;
    String entryPoint;
    String ImagePath;
    int ColorBackground;

   public SettingData(){


    }

   public SettingData(String UrlLink, String UserNameData,String PasswordData,String BranchID,String entryPoint, String ImagePath ,int ColorBackground){

        this.UrlLink=  UrlLink;
        this.UserNameData = UserNameData;
        this.PasswordData =PasswordData;
        this.BranchID = BranchID;
        this.entryPoint = entryPoint;
        this.ImagePath = ImagePath;
        this.ColorBackground= ColorBackground;


    }

    public String getUserNameData() {
        return UserNameData;
    }

    public void setUserNameData(String userNameData) {
        UserNameData = userNameData;
    }

    public String getPasswordData() {
        return PasswordData;
    }

    public void setPasswordData(String passwordData) {
        PasswordData = passwordData;
    }

    public String getUrlLink() {
        return UrlLink;
    }

    public void setUrlLink(String urlLink) {
        UrlLink = urlLink;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }


    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public int getColorBackground() {
        return ColorBackground;
    }

    public void setColorBackground(int colorBackground) {
        ColorBackground = colorBackground;
    }
}
