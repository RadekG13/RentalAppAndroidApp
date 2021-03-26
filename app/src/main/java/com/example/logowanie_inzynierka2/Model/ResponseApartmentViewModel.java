package com.example.logowanie_inzynierka2.Model;

public class ResponseApartmentViewModel {


    public String ApartmentId;

    public String Title;

    public String Description;

    public String AppUserID;

    public String Photo;////???????

    public ResponseApartmentViewModel(String apartmentId, String title, String description, String appUserID, String photo) {
        ApartmentId = apartmentId;
        Title = title;
        Description = description;
        AppUserID = appUserID;
        Photo = photo;
    }

    public String getApartmentId() {
        return ApartmentId;
    }

    public void setApartmentId(String apartmentId) {
        ApartmentId = apartmentId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAppUserID() {
        return AppUserID;
    }

    public void setAppUserID(String appUserID) {
        AppUserID = appUserID;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
