package com.example.mosque;

public class Store {
    private String name;
    private String address;
    private String type;
    private String imageUrl; // Nuevo campo para la URL de la imagen

    public Store() {
        // Constructor vacío necesario para Firestore
    }

    public Store(String name, String address, String type, String imageUrl) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.imageUrl = imageUrl; // Asignar la URL de la imagen
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
