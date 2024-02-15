package com.example.wisebuy.repositories;

import android.util.Log;

import com.example.wisebuy.models.AllProducts;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AllProductsRepository {
    private static final FirebaseFirestore fireStore;
    private static List<AllProducts> productList;

    static {
        fireStore = FirebaseFirestore.getInstance();
    }

    public static void getProducts(Consumer<List<AllProducts>> onSuccess, Consumer<Exception> onError) {
        fireStore.collection("Products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && onSuccess != null) {
                productList = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    List<String> imageUrls = (List<String>) document.get("imageURLs");
                    String title = document.getString("name");
                    Double price = document.getDouble("price");
                    String description = document.getString("description");
                    String type = document.getString("type");
                    String brand = document.getString("brand");
                    String details = document.getString("details");


                    AllProducts product = new AllProducts(documentId, imageUrls, title, price, description, type,brand,details);
                    productList.add(product);
                }

                onSuccess.accept(productList);
            } else if (onError != null) {
                onError.accept(task.getException());
            }
        });
    }

    public void searchProducts(String query, Consumer<List<AllProducts>> onSuccess, Consumer<Exception> onError) {
        String lowerQuery = query.toLowerCase();
        Log.d("QueyinRep0", lowerQuery);
        List<AllProducts> searchResults = new ArrayList<>();


        for (AllProducts product : productList) {

            String lowerTitle = product.getTitle().toLowerCase();
            String lowerType = product.getType().toLowerCase();

            if (lowerTitle.contains(lowerQuery) || lowerType.contains(lowerQuery)) {
                Log.d("SearchProducts1", "Condition is true for: " + lowerTitle + " | " + lowerType + " | " + lowerQuery);
                searchResults.add(product);
            } else {
                Log.d("SearchProducts1", "Condition is false for: " + lowerTitle + " | " + lowerType + " | " + lowerQuery);
                // Add any additional logic or logging for the else condition if needed
            }
        }

        Log.d("SearchProducts", "Product List Size: " + productList.size());
        Log.d("SearchProducts", "Search Results Size: " + searchResults.size());

        if (!searchResults.isEmpty()) {
            onSuccess.accept(searchResults);
        } else if (onError != null) {
            onError.accept(new Exception("No matching products found."));
        }
    }

    public void getProductDetails(String id, Consumer <AllProducts> onSuccess, Consumer<Exception> onError) {

        AllProducts searchResults = new AllProducts();


        for (AllProducts product : productList) {


            if (id.equals(product.getDocumentId())) {
                     searchResults=product;
                break;
            }
        }

        if(onSuccess !=null)
            onSuccess.accept(searchResults);
         else{
            onError.accept(new Exception("No matching products found."));
        }


    }
}