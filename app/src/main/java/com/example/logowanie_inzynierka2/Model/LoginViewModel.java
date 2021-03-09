package com.example.logowanie_inzynierka2.Model;

public class LoginViewModel {

    private String Email;
    private String Password;

    public LoginViewModel(){

    }

    public LoginViewModel(String email, String password) {
        Email = email;
        Password = password;
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
}
