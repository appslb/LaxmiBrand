package com.e.laxmibrand.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.UserCartItem;
import com.e.laxmibrand.user.MainPage2;
import com.e.laxmibrand.user.MyCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ReviewOrderAdapter extends RecyclerView.Adapter<ReviewOrderAdapter.CartViewHolder>{
    Context context;
    String chkCategory="",chkCategory2="";
    int currQty=1;
    public ReviewOrderAdapter(Context context){//, ArrayList<Category> productList) {
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_list_item, viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder viewHolder, final int i) {

        currQty=MyCart.basketList.get(i).getQty();

        if(i>0) {
            chkCategory = MyCart.basketList.get(i).getCategory();
            chkCategory2 = MyCart.basketList.get(i - 1).getCategory();
            if (chkCategory2.equals(chkCategory)) {

                viewHolder.categoryName.setVisibility(View.GONE);
            } else {
                viewHolder.categoryName.setVisibility(View.VISIBLE);

            }
        }

        viewHolder.categoryName.setText(MyCart.basketList.get(i).getCategory());
        try {
            String images = MyCart.basketList.get(i).getProductImage();
            if (!images.equals("") && (images.contains("https")||images.contains("http"))) {
                String[] imagesList = images.split(",");
                String S1 = imagesList[0];
                if (S1.contains("https"))
                    S1 = S1.replace("https", "http");

                Log.i("image image :", S1);

                Glide.with(context)
                        .load(S1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(viewHolder.productImage);
            }
            else
            {
                Glide.with(context)
                        .load(R.drawable.watermark_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(viewHolder.productImage);
            }

        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }

        viewHolder.qtyText.setText(String.valueOf(currQty));
        viewHolder.productImage.setImageResource(R.drawable.newcat1);
        viewHolder.productName.setText(MyCart.basketList.get(i).getProductname());
        viewHolder.variant.setText(MyCart.basketList.get(i).getVariant());
        viewHolder.priceActual.setText("Unit Price - \u20B9 "+ MyCart.basketList.get(i).getActual_price());
        if(Integer.parseInt(MyCart.basketList.get(i).getVar_dis()) > 0)
            viewHolder.priceDiscounted.setText("Unit Discount - \u20B9 "+MyCart.basketList.get(i).getVar_dis());
        else
            viewHolder.priceDiscounted.setText("");
        viewHolder.totalItemPrice.setText("Sub Total - \u20B9" + String.valueOf(currQty*Integer.parseInt(MyCart.basketList.get(i).getActual_price())));
viewHolder.more.setVisibility(View.GONE);
viewHolder.less.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return MyCart.basketList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName,variant,priceActual,priceDiscounted,categoryName,qtyText,totalItemPrice;
        ImageView productImage,add,remove;
        CardView more,less;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            priceActual = itemView.findViewById(R.id.actualPrice);
            priceDiscounted = itemView.findViewById(R.id.offerPrice);
            productName = itemView.findViewById(R.id.productName);
            categoryName = itemView.findViewById(R.id.titleText);
            variant = itemView.findViewById(R.id.variant);
            qtyText = itemView.findViewById(R.id.prnumber);
            add = itemView.findViewById(R.id.qty_more);
            remove = itemView.findViewById(R.id.less_qty);
            more = (CardView) itemView.findViewById(R.id.more);
            less = (CardView) itemView.findViewById(R.id.less);
            totalItemPrice = (TextView) itemView.findViewById(R.id.totalItemPrice);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), ProductDetails.class));
                }
            });*/

        }
    }


}



