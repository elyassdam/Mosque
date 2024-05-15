package com.example.mosque;

public class User {
    String userID;
    String name;
    String dni;
    String email;

    String phone;
    String dateBirth;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    String password;
    String confirmPassword;




    public User(String userID, String name, String dni, String email,String phone, String dateBirth, String password, String confirmPassword) {
        this.userID = userID;
        this.name = name;
        this.email=email;
        this.dni = dni;
        this.phone = phone;
        this.dateBirth = dateBirth;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }



}
