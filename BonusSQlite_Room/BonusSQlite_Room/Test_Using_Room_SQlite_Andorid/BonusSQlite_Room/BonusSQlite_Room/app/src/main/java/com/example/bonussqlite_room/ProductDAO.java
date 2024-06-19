package com.example.bonussqlite_room;

// ProductDao.java
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM Product")
    List<Product> getAllProducts();
    @Query("SELECT * FROM product WHERE id = :id")
    Product getProductById(int id);
}
