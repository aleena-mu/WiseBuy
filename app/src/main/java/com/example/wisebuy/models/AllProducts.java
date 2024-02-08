package com.example.wisebuy.models;

import java.util.List;

public class AllProducts {
    private List<String> imageUrls;
    private String title;
    private int price;
   private  String description;
   private  String type;




    public AllProducts() {
        // Default constructor required for Firestore
    }

    public AllProducts(List<String> imageUrls, String title, double price,String description,String type) {
        this.imageUrls = imageUrls;
        this.title = title;
        this.price = (int)price;
        this.description=description;
        this.type=type;
    }

    public String getDescription() {
        return description;
    }


    public String getType() {
        return type;
    }

    // Use the 0th image URL for simplicity
    public String getImageUrl() {
        if (imageUrls != null && imageUrls.size() > 0) {
            return imageUrls.get(0);
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }



}
