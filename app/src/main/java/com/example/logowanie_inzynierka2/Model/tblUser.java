package com.example.logowanie_inzynierka2.Model;

public class tblUser {
    private String Email;
    private String Password;
    private String ConfirmPassword;
    private String Telephone;
    private String NameAndSurname;
    private String AccountNumber;
    public tblUser() {
    }

    public tblUser(String email, String password, String confirmPassword, String telephone, String nameAndSurname, String accountNumber) {
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
        Telephone = telephone;
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
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
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
