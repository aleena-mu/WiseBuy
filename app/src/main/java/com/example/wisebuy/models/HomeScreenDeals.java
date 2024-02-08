package com.example.wisebuy.models;

public class HomeScreenDeals {
    private final String imageURL;
    private final String title;
    private final String deal;
    private final String dealName;

    public  HomeScreenDeals(String title, String deal, String imageURL, String dealName){
        this.title=title;
        this.deal=deal;
        this.imageURL=imageURL;
        this.dealName=dealName;
    }
    public String getImageURL() {
        return imageURL;
    }

    public String getDealName() {
        return dealName;
    }

    public String getTitle() {
        return title;
    }

    public String getDeal() {
        return deal;
    }
}
