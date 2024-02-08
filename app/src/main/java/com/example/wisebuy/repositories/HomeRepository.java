package com.example.wisebuy.repositories;

import com.example.wisebuy.models.HomeScreenDeals;
import com.example.wisebuy.models.Offer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HomeRepository {
    private static final FirebaseFirestore fireStore;
    static {
        fireStore = FirebaseFirestore.getInstance();
    }
    public static void getOffers(Consumer<List<Offer>> onSuccess,Consumer<Exception> onError){


         fireStore.collection("Offers").get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && onSuccess!=null){
                onSuccess.accept(task.getResult().toObjects(Offer.class));
            }
            else if( onError!=null) {
                onError.accept(task.getException());

            }
        });
    }
    public static void getTopDeals(Consumer<List<HomeScreenDeals>> onSuccess, Consumer<Exception> onError) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("TopDeals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<HomeScreenDeals> dealsList = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String dealName = document.getString("dealName");

                    // Get the subcollection "Products" for each document
                    firestore.collection("TopDeals").document(document.getId())
                            .collection("Products").get().addOnCompleteListener(productsTask -> {
                                if (productsTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot productDocument : productsTask.getResult()) {
                                        String deal = productDocument.getString("deal");
                                        DocumentReference categoryRef = productDocument.getDocumentReference("dealCategory");

                                        categoryRef.get().addOnCompleteListener(categoryTask -> {
                                            if (categoryTask.isSuccessful()) {
                                                DocumentSnapshot categoryDocument = categoryTask.getResult();
                                                if (categoryDocument.exists()) {
                                                    String imageURL = categoryDocument.getString("imageURL");
                                                    String title = categoryDocument.getString("title");


                                                    // Create the Offer object and add it to the list
                                                    HomeScreenDeals homeScreenDeals = new HomeScreenDeals(title, deal, imageURL,dealName);
                                                    dealsList.add(homeScreenDeals);

                                                    // If this is the last product in the subcollection, notify success
                                                    if (dealsList.size() == productsTask.getResult().size()) {
                                                        onSuccess.accept(dealsList);
                                                    }
                                                }
                                            } else if (onError != null) {
                                                onError.accept(categoryTask.getException());
                                            }
                                        });
                                    }
                                } else if (onError != null) {
                                    onError.accept(productsTask.getException());
                                }
                            });
                }
            } else if (onError != null) {
                onError.accept(task.getException());
            }
        });
    }

}
