package com.example.mosque;

public class Content {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String type;

    public Content() {
        // Constructor vacío necesario para Firestore
    }

    public Content(String title, String description, String type) {
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public Content(String title, String description, String imageUrl, String type) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
