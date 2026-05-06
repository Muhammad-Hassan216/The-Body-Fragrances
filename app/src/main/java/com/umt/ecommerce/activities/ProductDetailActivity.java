// Package declaration and required imports
package com.umt.ecommerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.umt.ecommerce.R;
import com.umt.ecommerce.databinding.ActivityProductDetailBinding;
import com.umt.ecommerce.model.Product;
import com.umt.ecommerce.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {

    // ViewBinding ka object — XML layout ke views ko access krne ke liye
    ActivityProductDetailBinding binding;

    // Product object jisme current product ki details save hongi
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // XML layout ko inflate kr rahe hain using ViewBinding
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Intent se product ka naam, image URL aur ID receive kr rahe hain
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id", 0);

        // Glide library ka use kr ke image ko load krwa rahe hain
        Glide.with(this)
                .load(image)
                .into(binding.productImage);

        // Product ki details fetch krne ke liye API call kr rahe hain
        getProductDetails(id);

        // Action bar mein product ka naam set kr rahe hain aur back button show kr rahe hain
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Cart object get kr rahe hain using TinyCartHelper library
        Cart cart = TinyCartHelper.getCart();

        // Add to Cart button pr click listener set kr rahe hain
        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Product ki image aur quantity set kr rahe hain
                currentProduct.setImage(image);
                currentProduct.setQuantity(1);

                // Cart mein item add kr rahe hain
                cart.addItem(currentProduct, currentProduct.getQuantity());

                // Button disable kr rahe hain aur text change kr rahe hain
                binding.addToCartBtn.setEnabled(false);
                binding.addToCartBtn.setText("Added in cart");
            }
        });
    }

    // Cart icon ko action bar mein show krne ke liye menu inflate kr rahe hain
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Cart icon pr click hone pr CartActivity open krte hain
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    // ✅ Function: API call to get product details from server
    void getProductDetails(int id) {
        // Volley library se request queue create krte hain
        RequestQueue queue = Volley.newRequestQueue(this);

        // URL set kr rahe hain — GET_PRODUCT_DETAILS_URL + id
        String url = Constants.GET_PRODUCT_DETAILS_URL + id;

        // StringRequest create kr rahe hain to fetch product detail
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Response ko JSON object mein convert kr rahe hain
                    JSONObject object = new JSONObject(response);

                    // Agar response "success" hai to product object access krte hain
                    if (object.getString("status").equals("success")) {
                        JSONObject product = object.getJSONObject("product");

                        // Product ka description set krte hain — HTML formatting k sath
                        String description = product.getString("description");
                        binding.productDescription.setText(Html.fromHtml(description));

                        // Price aur discount get krte hain
                        double price = product.getDouble("price");
                        double discount = product.getDouble("price_discount");

                        // Agar discount hai to formatted price show krte hain
                        if (discount > 0) {
                            double discountedPrice = price - discount;
                            String priceText = "<font color='#808080'><s>PKR " + price + "</s></font><br>" +
                                    "<font color='#FF5722'><b>PKR " + discountedPrice + "</b></font>";
                            binding.productPrice.setText(Html.fromHtml(priceText));
                        } else {
                            // Agar discount nahi hai to simple price show hota hai
                            binding.productPrice.setText("PKR " + price);
                        }

                        // Product object create krte hain currentProduct mein
                        currentProduct = new Product(
                                product.getString("name"),
                                product.getString("image_url"),
                                product.getString("status"),
                                price,
                                discount,
                                product.getInt("stock"),
                                product.getInt("id")
                        );
                    }

                } catch (JSONException e) {
                    // Agar JSON parsing mein error ho to exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Agar request fail ho jaye to yahan handle kr sakte hain
            }
        });

        // Request ko queue mein add krte hain taake execute ho
        queue.add(request);
    }

    // Back arrow click hone pr activity close krte hain
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
