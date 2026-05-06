package com.umt.ecommerce.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.umt.ecommerce.R;
import com.umt.ecommerce.activities.ProductDetailActivity;
import com.umt.ecommerce.databinding.ItemProductBinding;
import com.umt.ecommerce.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        // ✅ Load image with rounded corners
        Glide.with(context)
                .load(product.getImage())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.image);

        // ✅ Set product name
        holder.binding.label.setText(product.getName());

        // ✅ Handle discount
        double originalPrice = product.getPrice();
        double discountAmount = product.getDiscount();

        if (discountAmount > 0 && discountAmount < originalPrice) {
            double discountedPrice = originalPrice - discountAmount;

            holder.binding.price.setText("PKR " + discountedPrice);
            holder.binding.oldPrice.setText("PKR " + originalPrice);
            holder.binding.oldPrice.setVisibility(View.VISIBLE);
            holder.binding.oldPrice.setPaintFlags(
                    holder.binding.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.binding.price.setText("PKR " + originalPrice);
            holder.binding.oldPrice.setVisibility(View.GONE);
        }






        // ✅ On item click, go to product detail activity
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("image", product.getImage());
            intent.putExtra("id", product.getId());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("discount", product.getDiscount()); // pass discount too
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
}
