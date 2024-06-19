package com.example.bonussqlite_room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {
    private EditText edtName, edtPrice, edtDesc;
    private Button btnUpdate, btnDelete;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDesc = findViewById(R.id.editDesc);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        new Thread(() -> {
            product = MainActivity.db.productDAO().getProductById(productId);
            runOnUiThread(() -> {
                if (product != null) {
                    edtName.setText(product.productName);
                    edtPrice.setText(String.valueOf(product.productPrice));
                    edtDesc.setText(product.productDescription);
                }
            });
        }).start();

        btnUpdate.setOnClickListener(v -> updateProduct());
        btnDelete.setOnClickListener(v -> deleteProduct());
    }

    private void updateProduct() {
        product.productName = edtName.getText().toString();
        product.productDescription = edtDesc.getText().toString();
        product.productPrice = Double.parseDouble(edtPrice.getText().toString());

        new Thread(() -> {
            MainActivity.db.productDAO().update(product);
            runOnUiThread(() -> {
                Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
                finish(); // Trở lại MainActivity
            });
        }).start();
    }

    private void deleteProduct() {
        new Thread(() -> {
            MainActivity.db.productDAO().delete(product);
            runOnUiThread(() -> {
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                finish(); // Trở lại MainActivity
            });
        }).start();
    }
}
