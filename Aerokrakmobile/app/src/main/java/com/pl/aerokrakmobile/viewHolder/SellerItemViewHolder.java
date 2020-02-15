package com.pl.aerokrakmobile.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pl.aerokrakmobile.R;

public class SellerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, productPrice, productState;
    public ImageView image;
    public com.pl.aerokrakmobile.viewHolder.ItemClickListener listener;

    public SellerItemViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.product_seller_image);
        txtProductName = itemView.findViewById(R.id.seller_product_name);
      //  txtProductDescription = itemView.findViewById(R.id.product_seller_description);
        productPrice = itemView.findViewById(R.id.product_seller_price);
        productState = itemView.findViewById(R.id.seller_product_state);

    }

    public void setItemClickListener(com.pl.aerokrakmobile.viewHolder.ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
