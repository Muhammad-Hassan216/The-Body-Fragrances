package com.umt.ecommerce.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.umt.ecommerce.Adapters.CartAdapter;
import com.umt.ecommerce.databinding.ActivityCartBinding;
import com.umt.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;

    CartAdapter adapter;


    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        Cart cart = TinyCartHelper.getCart();

        // Loop through cart items and convert to Product list
        for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey(); // Product object
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        // Initialize adapter with CartListener to update subtotal when quantity changes
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("PKR %.2f",cart.getTotalPrice()));
            }
        });

        // Set layout for RecyclerView (vertical list)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        //  Attach layout and adapter to RecyclerView
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        // Set subtotal when screen opens
        binding.subtotal.setText(String.format("PKR %.2f",cart.getTotalPrice()));

        // Button to go to CheckoutActivity
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start checkout screen when continue button is pressed
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });

        //  Show back arrow in top action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //  What happens when user presses back arrow
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close current activity
        return super.onSupportNavigateUp();
    }
}
