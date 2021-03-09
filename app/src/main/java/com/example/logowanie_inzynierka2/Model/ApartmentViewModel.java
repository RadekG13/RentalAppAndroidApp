package com.example.logowanie_inzynierka2.Model;

import com.google.gson.annotations.SerializedName;

public class ApartmentViewModel {


    @SerializedName("Title")
    public String Title;

    @SerializedName("Description")
    public String Description;

    public ApartmentViewModel(String title, String description) {
        Title = title;
        Description = description;
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
}
