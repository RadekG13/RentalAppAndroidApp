package com.example.logowanie_inzynierka2.Model;

public class RegisterViewModel {

    private String Email;
    private String Password;
    private String ConfirmPassword;
    private String PhoneNumber;
    private String NameAndSurname;
    private String AccountNumber;

    public RegisterViewModel(){

    }


    public RegisterViewModel(String email, String password, String confirmPassword, String telephone, String nameAndSurname, String accountNumber) {
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
        PhoneNumber = telephone;
        NameAndSurname = nameAndSurname;
        AccountNumber = accountNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getTelephone() {
        return PhoneNumber;
    }

    public void setTelephone(String telephone) {
        PhoneNumber = telephone;
    }

    public String getNameAndSurname() {
        return NameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        NameAndSurname = nameAndSurname;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }
}
