package com.example.mosque;

public class User {
    String userID;
    String name;
    String dni;
    String email;

    String phone;
    String dateBirth;
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
