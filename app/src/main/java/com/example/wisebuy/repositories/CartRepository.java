package com.example.wisebuy.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wisebuy.MyPreference;
import com.example.wisebuy.models.CartModel;
import com.example.wisebuy.models.Offer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CartRepository {

    private static final FirebaseFirestore fireStore;
    private static String currentUserId;


    static {
        fireStore = FirebaseFirestore.getInstance();
    }

    public static void addToCart(String userId, String productId) {


        fireStore.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentReference productReference = fireStore.collection("Products").document(productId);
                productReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot productSnapshot) {
                                if (productSnapshot.exists()) {

                                    List<String> imageUrls = (List<String>) productSnapshot.get("imageURLs");
                                    String title = productSnapshot.getString("name");
                                    Double price = productSnapshot.getDouble("price");


                                    for (QueryDocumentSnapshot userDocument : task.getResult()) {
                                        String documentId = userDocument.getId();
                                        if (Objects.equals(currentUserId, documentId)) {
                                            DocumentReference userReference = userDocument.getReference();
                                            CollectionReference cartReference = userReference.collection("Cart");


                                            Query query = cartReference.whereEqualTo("productId", productId);
                                            query.get().addOnCompleteListener(cartTask -> {
                                                if (cartTask.isSuccessful()) {
                                                    if (cartTask.getResult().isEmpty()) {
                                                        CartModel cartItem = new CartModel();

                                                        cartItem.setProductId(productId);
                                                        cartItem.setImageUrl(imageUrls.get(0));
                                                        cartItem.setTitle(title);
                                                        cartItem.setPrice(price.intValue());
                                                        cartItem.setQuantity(1);
                                                        cartItem.setTotalPriceofSingleItem(cartItem.getQuantity() * cartItem.getPrice());

                                                        cartReference.add(cartItem)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Handle failure
                                                                    }
                                                                });
                                                    } else {
                                                        DocumentSnapshot cartItemSnapshot = cartTask.getResult().getDocuments().get(0);
                                                        CartModel existingCartItem = cartItemSnapshot.toObject(CartModel.class);

                                                        if (existingCartItem != null) {
                                                            int currentQuantity = existingCartItem.getQuantity();
                                                            int updatedQuantity = currentQuantity + 1;
                                                            int updatedTotalPrice = updatedQuantity * price.intValue();

                                                            // Update existing cart item
                                                            cartItemSnapshot.getReference().update(
                                                                    "quantity", updatedQuantity,
                                                                    "totalPriceofSingleItem", updatedTotalPrice
                                                            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Cart item updated successfully
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Handle failure
                                                                }
                                                            });
                                                        }
                                                    }
                                                } else {
                                                    // Handle failure
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }

    public static void showCart(String userId, Consumer<Double> totalSum, Consumer<List<CartModel>> onSuccess, Consumer<Exception> onError) {
        currentUserId = userId;
        fireStore.collection("Users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot queryDocumentSnapshot = task.getResult();
                DocumentReference userReference = queryDocumentSnapshot.getReference();
                CollectionReference cartReference = userReference.collection("Cart");
                cartReference.get().addOnCompleteListener(taskCart -> {
                    if (taskCart.isSuccessful() && onSuccess != null) {
                        List<CartModel> cartModels = taskCart.getResult().toObjects(CartModel.class);


                        double sum = 0;
                        for (CartModel cartModel : cartModels) {

                            sum += cartModel.getTotalPriceofSingleItem();
                        }

                        // Pass the total sum to the totalSum consumer
                        if (totalSum != null) {
                            totalSum.accept(sum);
                        }


                        onSuccess.accept(cartModels);
                    } else if (onError != null) {
                        onError.accept(taskCart.getException());
                    }
                });
            }
        });
    }

    public static void updateQuantity(int quantity, String productId) {
        fireStore.collection("Users").document(currentUserId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot userDocumentSnapshot = task.getResult();
                DocumentReference userReference = userDocumentSnapshot.getReference();

                CollectionReference cartCollectionReference = userReference.collection("Cart");
                Query query = cartCollectionReference.whereEqualTo("productId", productId);
                query.get().addOnCompleteListener(cartTask -> {
                    if (cartTask.isSuccessful()) {
                        DocumentSnapshot cartSnapShot = cartTask.getResult().getDocuments().get(0);
                        if (cartSnapShot.exists()) {
                            Double price = cartSnapShot.getDouble("price");
                            if (price != null) {
                                int updatedTotalPrice = quantity * price.intValue();
                                cartSnapShot.getReference().update("quantity", quantity, "totalPriceofSingleItem", updatedTotalPrice)
                                        .addOnCompleteListener(taskUpdated -> {
                                            if (taskUpdated.isSuccessful()) {

                                            } else {
                                                // Handle the case where the update fails
                                            }
                                        });
                            }


                        } else {
                        }


                    } else {
                    }
                });
            } else {
            }
        });
    }
}