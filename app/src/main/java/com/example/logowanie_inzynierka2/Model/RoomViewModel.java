package com.example.logowanie_inzynierka2.Model;

public class RoomViewModel {


    public String RoomId;

    public String Title;

    public String Description;

    public int Deposit;

    public int RentFee;

    public Boolean Status; //For Response

    public String ApartmentId;

    public String Photo; //For Response

    public RoomViewModel(String roomId, String title, String description, int deposit, int rentFee, Boolean status, String apartmentId, String photo) {
        RoomId = roomId;
        Title = title;
        Description = description;
        Deposit = deposit;
        RentFee = rentFee;
        Status = status;
        ApartmentId = apartmentId;
        Photo = photo;
    }


    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
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

    public int getDeposit() {
        return Deposit;
    }

    public void setDeposit(int deposit) {
        Deposit = deposit;
    }

    public int getRentFee() {
        return RentFee;
    }

    public void setRentFee(int rentFee) {
        RentFee = rentFee;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getApartmentId() {
        return ApartmentId;
    }

    public void setApartmentId(String apartmentId) {
        ApartmentId = apartmentId;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
