package com.exampledemo.parsaniahardik.scanbarcodeqrdemonuts;

import com.orm.SugarRecord;

/**
 * Created by Stech on 3/1/2018.
 */

public class SettingData extends SugarRecord {

    String UrlLink;
    String UserNameData;
    String PasswordData;
    String ImagePath;

   public SettingData(){


    }

    SettingData(String UrlLink, String UserNameData,String PasswordData, String ImagePath){

        this.UrlLink=  UrlLink;
        this.UserNameData = UserNameData;
        this.PasswordData =PasswordData;
        this.ImagePath = ImagePath;

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

}
