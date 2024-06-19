package com.example.bonussqlite_room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static AppDatabase db;
    public static MySQLiteHelper mySQLiteHelper;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnInsert;
    private EditText edtName;
    private EditText edtPrice;
    private EditText edtDesc;
    private List<Product> productList;
    private void bindingView(){
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDesc = findViewById(R.id.editDesc);
        btnInsert = findViewById(R.id.btnInsert);
        recyclerView = findViewById(R.id.recyclerView);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "goods.db").build();
        mySQLiteHelper = new MySQLiteHelper(this);
    }
    private void bindingAction(){
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        new Thread(() -> {
            productList = MainActivity.db.productDAO().getAllProducts();
            runOnUiThread(() -> {
                adapter = new ProductAdapter(productList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(product -> {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("PRODUCT_ID", product.id); // Chuyển ID của sản phẩm
                    startActivity(intent);
                });
            });
        }).start();
        btnInsert.setOnClickListener(this::onBtnInsertClick);
        // Trong onCreate của MainActivity
    }

    private void onBtnInsertClick(View view) {
        Product product = new Product(edtName.getText().toString(), edtDesc.getText().toString(), Double.parseDouble(edtPrice.getText().toString()));

        new Thread(() -> {
            MainActivity.db.productDAO().insert(product);
            List<Product> updatedProductList = MainActivity.db.productDAO().getAllProducts();

            runOnUiThread(() -> {
                productList.clear();
                productList.addAll(updatedProductList);
                adapter.notifyDataSetChanged();
            });
        }).start();
        Log.d("TAGKId", adapter.getItemCount() + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Lấy lại danh sách sản phẩm và cập nhật RecyclerView
    }

    private void loadProducts() {
        new Thread(() -> {
            productList = MainActivity.db.productDAO().getAllProducts();
            runOnUiThread(() -> {
                adapter.updateData(productList);
            });
        }).start();
    }
}