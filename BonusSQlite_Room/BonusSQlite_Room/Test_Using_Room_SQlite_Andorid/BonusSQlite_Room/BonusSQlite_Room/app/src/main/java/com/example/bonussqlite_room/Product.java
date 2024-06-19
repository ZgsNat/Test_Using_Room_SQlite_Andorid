package com.example.bonussqlite_room;

// Product.java
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {
    public Product(String productName, String productDescription, double productPrice) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public Product() {
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String productName;
    public String productDescription;
    public double productPrice;
}
