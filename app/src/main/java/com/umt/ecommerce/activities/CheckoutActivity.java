package com.umt.ecommerce.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.umt.ecommerce.Adapters.CartAdapter;
import com.umt.ecommerce.R;
import com.umt.ecommerce.databinding.ActivityCheckoutBinding;
import com.umt.ecommerce.model.Product;
import com.umt.ecommerce.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);

        // Handle system UI insets like status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show a dialog when processing the order
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        // Initialize cart and product list
        products = new ArrayList<>();
        cart = TinyCartHelper.getCart();

        // Get all items from cart and add them to products list
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            products.add(product);
        }

        // Set adapter for cart list and define what to do on quantity change
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                // Update subtotal when quantity changes
                binding.subtotal.setText(String.format("PKR %.2f", cart.getTotalPrice()));
            }
        });

        // RecyclerView setup with vertical layout and divider
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        // Show subtotal and total (including tax)
        binding.subtotal.setText(String.format("PKR %.2f", cart.getTotalPrice()));
        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("PKR " + totalPrice);

        // When checkout button is clicked
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    processOrder(); // Submit order if form valid
                } else {
                    Toast.makeText(CheckoutActivity.this, "Please fill all required fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show back button on top bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // ✅ Validate user input
    private boolean validateForm() {
        if (binding.nameBox.getText().toString().trim().isEmpty()) {
            binding.nameBox.setError("Name is required");
            binding.nameBox.requestFocus();
            return false;
        }

        String email = binding.emailBox.getText().toString().trim();
        if (email.isEmpty()) {
            binding.emailBox.setError("Email is required");
            binding.emailBox.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailBox.setError("Enter a valid email address");
            binding.emailBox.requestFocus();
            return false;
        }

        String phone = binding.phoneBox.getText().toString().trim();
        if (phone.isEmpty()) {
            binding.phoneBox.setError("Phone number is required");
            binding.phoneBox.requestFocus();
            return false;
        } else if (phone.length() != 11 || !phone.matches("[0-9]+")) {
            binding.phoneBox.setError("Phone must be 11 digits");
            binding.phoneBox.requestFocus();
            return false;
        }

        if (binding.addressBox.getText().toString().trim().isEmpty()) {
            binding.addressBox.setError("Address is required");
            binding.addressBox.requestFocus();
            return false;
        }

        return true;
    }

    // ✅ Function to submit order using Volley POST request
    void processOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject productOrder = new JSONObject();     // Order data
        JSONObject dataObject = new JSONObject();       // Final data to send
        try {
            // Basic order info
            productOrder.put("address", binding.addressBox.getText().toString());
            productOrder.put("buyer", binding.nameBox.getText().toString());
            productOrder.put("comment", binding.commentBox.getText().toString());
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email", binding.emailBox.getText().toString());
            productOrder.put("phone", binding.phoneBox.getText().toString());
            productOrder.put("serial", "cab8c1a4e4421a3b");
            productOrder.put("shipping", "");
            productOrder.put("shipping_location", "");
            productOrder.put("shipping_rate", "0.0");
            productOrder.put("status", "WAITING");
            productOrder.put("tax", tax);
            productOrder.put("total_fees", totalPrice);

            // Add order items detail
            JSONArray product_order_detail = new JSONArray();
            for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                JSONObject productObj = new JSONObject();
                productObj.put("amount", quantity);
                productObj.put("price_item", product.getPrice());
                productObj.put("product_id", product.getId());
                productObj.put("product_name", product.getName());
                product_order_detail.put(productObj);
            }

            // Put all info into one data object
            dataObject.put("product_order", productOrder);
            dataObject.put("product_order_detail", product_order_detail);

            Log.e("err", dataObject.toString()); // log data for debugging

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  POST request to API with headers
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(CheckoutActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                        String orderNumber = response.getJSONObject("data").getString("code");

                        // Show confirmation dialog
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Successful")
                                .setCancelable(false)
                                .setMessage("Your order number is: " + orderNumber)
                                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Move to payment screen
                                        Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
                                        intent.putExtra("orderCode", orderNumber);
                                        startActivity(intent);
                                    }
                                }).show();
                    } else {
                        showOrderFailedDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showOrderFailedDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                showOrderFailedDialog();
            }
        }) {
            // ✅ Add security header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Security", "secure_code");
                return headers;
            }
        };

        queue.add(request);
    }

    // ✅ Dialog when order fails
    private void showOrderFailedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Order Failed")
                .setMessage("Something went wrong, please try again.")
                .setCancelable(false)
                .setPositiveButton("Close", null)
                .show();
    }

    // ✅ Handle back button press in top action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
