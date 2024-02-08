package com.example.wisebuy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisebuy.R;
import com.example.wisebuy.models.AllProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllProductsAdapter extends BaseAdapter {

    private final Context context;
    private List<AllProducts> productList;

    public AllProductsAdapter(Context context, List<AllProducts> productList) {
        this.context = context;

        if (productList == null) {
            this.productList = new ArrayList<>();
        } else {
            this.productList = productList;
        }
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.all_products_design, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AllProducts allProducts = productList.get(position);
        holder.bind(allProducts);

        return convertView;
    }

    public void setProductList(List<AllProducts> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView productImage;
        TextView productTitle;
        TextView productPrice;
        TextView productDescription;

        ViewHolder(View itemView) {
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.description);
        }

        void bind(AllProducts product) {
            Picasso.get().load(product.getImageUrl()).into(productImage);

            productTitle.setText(product.getTitle());
            String currencySymbol = "₹";
            String productPriceValue = currencySymbol + " " + product.getPrice();
            productPrice.setText(productPriceValue);

            productDescription.setText(product.getDescription());
        }
    }
}
