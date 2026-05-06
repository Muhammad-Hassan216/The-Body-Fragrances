package com.umt.ecommerce.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.umt.ecommerce.Adapters.ProductAdapter;
import com.umt.ecommerce.R;
import com.umt.ecommerce.databinding.ActivityCategoryBinding;
import com.umt.ecommerce.model.Product;
import com.umt.ecommerce.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {


    ActivityCategoryBinding binding;

    ProductAdapter productAdapter;

    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize empty product list
        products = new ArrayList<>();

        // Set adapter with empty list
        productAdapter = new ProductAdapter(this, products);

        // Get category ID and name from Intent sent by previous activity
        int catId = getIntent().getIntExtra("catId", 0);
        String categoryName = getIntent().getStringExtra("categoryName");

        // Set title of ActionBar to category name and enable back button
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // all function to fetch products of selected category
        getProducts(catId);

        // Set RecyclerView layout as 2-column grid
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    //  Handle back arrow in ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        finish();  // Close activity and go back
        return super.onSupportNavigateUp();
    }

    // Function to fetch products from API using category ID
    void getProducts(int catId) {
        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Build API URL using category ID
        String url = Constants.GET_PRODUCTS_URL + "?category_id=" + catId;

        // Make GET request to fetch product data
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                // Convert response to JSON
                JSONObject object = new JSONObject(response);

                // Check if response status is "success"
                if(object.getString("status").equals("success")){

                    // Get products array from response
                    JSONArray productsArray = object.getJSONArray("products");

                    // Loop through each product JSON object
                    for(int i = 0; i < productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);

                        // Create Product object from JSON
                        Product product = new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getString("image"),
                                childObj.getString("status"),
                                childObj.getDouble("price"),
                                childObj.getDouble("price_discount"),
                                childObj.getInt("stock"),
                                childObj.getInt("id")
                        );

                        // Add product to list
                        products.add(product);
                    }

                    //  Notify adapter that data has changed, so RecyclerView refreshes
                    productAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
        });


        queue.add(request);
    }
}
