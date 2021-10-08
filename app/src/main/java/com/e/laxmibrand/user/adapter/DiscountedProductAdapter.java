package com.e.laxmibrand.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.DiscountedProducts;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.UserCartItem;
import com.e.laxmibrand.user.ProductDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DiscountedProductAdapter extends RecyclerView.Adapter<DiscountedProductAdapter.DiscountedProductViewHolder> {

    Context context;
    List<Products> discountedProductsList;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private int PRIVATE_MODE = 0;
    int currQty=1;
    private String KEY_BASKET = "basket list";
    String mobileno;
    public DiscountedProductAdapter(Context context, List<Products> discountedProductsList) {
        this.context = context;
        this.discountedProductsList = discountedProductsList;

        mSharedPreferences = context.getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        mobileno=mSharedPreferences.getString("user_mobile_no",null);

    }

    @NonNull
    @Override
    public DiscountedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.discounted_row_items, parent, false);
        return new DiscountedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountedProductViewHolder holder, int position) {
        try {
            String images = discountedProductsList.get(position).getPrdt_images();
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
                        .into(holder.productImage);

            }

        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }
        String pname=discountedProductsList.get(position).getPdt_name();
        pname =pname.replace("_"," ");
        holder.productName.setText(pname);
        holder.priceText.setText("Rs. " + discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt());
        holder.varText.setText(discountedProductsList.get(position).getVarientItems().get(0).getVarType());


        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileno != null) {
                    holder.qut.setVisibility(View.VISIBLE);
                    holder.addBtn.setVisibility(View.GONE);
                    holder.prdctQty.setText(String.valueOf(currQty));
                    saveBasketItem(discountedProductsList.get(position).getPdt_id(), new UserCartItem(discountedProductsList.get(position).getCategory_id(), discountedProductsList.get(position).getVarientItems().get(0).getVar_id(),discountedProductsList.get(position).getPdt_id(), discountedProductsList.get(position).getPdt_name(), discountedProductsList.get(position).getPrdt_images(), currQty, discountedProductsList.get(position).getVarientItems().get(0).getVarType(), discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt(), "","20", String.valueOf(Integer.parseInt(holder.prdctQty.getText().toString()) * Integer.parseInt(discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt()))));

                }
                else
                {
                    Toast.makeText(context,"Please login to add product to cart.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currQty==5) {
                    Toast.makeText(v.getContext(), "You Cannot add any more quantity for this product", Toast.LENGTH_LONG).show();
                    holder.prdctQty.setText(String.valueOf(currQty));
                }
                else
                {
                    currQty=currQty+1;
                    holder.prdctQty.setText(String.valueOf(currQty));
                    saveBasketItem(discountedProductsList.get(position).getPdt_id(), new UserCartItem(discountedProductsList.get(position).getCategory_id(),discountedProductsList.get(position).getVarientItems().get(0).getVar_id(), discountedProductsList.get(position).getPdt_id(), discountedProductsList.get(position).getPdt_name(), discountedProductsList.get(position).getPrdt_images(), currQty, discountedProductsList.get(position).getVarientItems().get(0).getVarType(), discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt(), "","20", String.valueOf(Integer.parseInt(holder.prdctQty.getText().toString()) * Integer.parseInt(discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt()))));

                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currQty==1) {
                    currQty=1;
                    holder.qut.setVisibility(View.GONE);
                    holder.addBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    currQty=currQty-1;
                    holder.prdctQty.setText(String.valueOf(currQty));
                    saveBasketItem(discountedProductsList.get(position).getPdt_id(), new UserCartItem(discountedProductsList.get(position).getCategory_id(),discountedProductsList.get(position).getVarientItems().get(0).getVar_id(), discountedProductsList.get(position).getPdt_id(), discountedProductsList.get(position).getPdt_name(), discountedProductsList.get(position).getPrdt_images(), currQty, discountedProductsList.get(position).getVarientItems().get(0).getVarType(), discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt(), "","20", String.valueOf(Integer.parseInt(holder.prdctQty.getText().toString()) * Integer.parseInt(discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt()))));

                }
            }
        });
        holder.discountProductCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prdctDetails = new Intent(v.getContext(), ProductDetails.class);
                prdctDetails.putExtra("p_id",discountedProductsList.get(position).getPdt_id());
                prdctDetails.putExtra("p_name",discountedProductsList.get(position).getPdt_name());
                prdctDetails.putExtra("p_desc",discountedProductsList.get(position).getPdt_about());
                prdctDetails.putExtra("p_var",discountedProductsList.get(position).getVarientItems().get(0).getVarType());
                prdctDetails.putExtra("p_images",discountedProductsList.get(position).getPrdt_images());
                prdctDetails.putExtra("p_actualP",discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt());
                prdctDetails.putExtra("p_price",discountedProductsList.get(position).getVarientItems().get(0).getVarDisAmt());
                prdctDetails.putExtra("p_qty",currQty);
                prdctDetails.putExtra("p_cat",discountedProductsList.get(position).getCategory_id());
                v.getContext().startActivity(prdctDetails);

            }
        });


    }

    @Override
    public int getItemCount() {
        return discountedProductsList.size();
    }

    public static class DiscountedProductViewHolder extends  RecyclerView.ViewHolder{

        ImageView productImage,add,remove;
        TextView productName,varText,priceText,prdctQty;
        Button addBtn;
        LinearLayout qut;
        CardView discountProductCard;
        public DiscountedProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            varText= itemView.findViewById(R.id.variantTxt);
            priceText = itemView.findViewById(R.id.priceText);

            discountProductCard = itemView.findViewById(R.id.discountProductCard);
            addBtn = itemView.findViewById(R.id.addButton);
            qut= itemView.findViewById(R.id.ll12);
            prdctQty  = itemView.findViewById(R.id.productQTY);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);

        }
    }



    public void saveBasketItem(String pdt_id,UserCartItem cartItem)
    {
        ArrayList<UserCartItem> basketList;
        Gson gson = new Gson();
        basketList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());
        if (basketList == null) {
            basketList = new ArrayList<>();
        }
        boolean isAvailable = false;
        //  ArrayList<BasketModel.DesignList> designList = new ArrayList<>();
        for (int n = 0; n < basketList.size(); n++) {
            if (basketList.get(n).getPdt_id().equals(pdt_id)) {
                isAvailable = true;
                basketList.get(n).setQty(basketList.get(n).getQty() + 1);
                break;
            } else {
                isAvailable = false;
            }
        }
        if (!isAvailable) {
            /* basketList.add(new UserCartItem(catalogueid,
                     1, response.body().getResponseData().getNoofsets(),response.body().getResponseData().getCataloguename(),
                     response.body().getResponseData().getPrice(),
                     response.body().getResponseData().getCatalogueDesignViewList().get(0).getCataloguedesignpath(),
                     response.body().getResponseData().getSingledesign(),
                     true, designList));*/
            basketList.add(cartItem);
        }

        //Utility.mPreferenceSettings().setBasketList(basketList);
        Gson gsonp = new Gson();
        mEditor.putString(KEY_BASKET, gsonp.toJson(basketList)).commit();
        Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show();

    }

}
