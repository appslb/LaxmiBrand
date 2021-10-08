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
import com.e.laxmibrand.admin.product.AddProduct;
import com.e.laxmibrand.admin.product.SpinnerAdapter;
import com.e.laxmibrand.admin.product.VariantListAdapter;
import com.e.laxmibrand.beans.AdminCategory;
import com.e.laxmibrand.beans.Category;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.UserCartItem;
import com.e.laxmibrand.user.MainPage2;
import com.e.laxmibrand.user.MyCart;
import com.e.laxmibrand.user.ProductDetails;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemViewHolder> {
ArrayList<String> vList;
Context context;
    int varPosition;
    ArrayList<Products> productItemList;
    ArrayList<UserCartItem> cartItemList;
    UserCartItem cartItem;
    boolean isAvailable = false;
    boolean isAvailableVar = false;

    String selectedVariantPrice,selectedVarianceDiscount;
    ArrayList<UserCartItem> cartList;
    private SharedPreferences mSharedPreferences;
    private int PRIVATE_MODE = 0;
    int addCntr=0,remCntr=0,currQty=1,getcurQty=0;
    private SharedPreferences.Editor mEditor;
    private String KEY_BASKET = "basket list";
    String mobileno;
    String selectedVariant,selectedCategoryName,selectedCategoryId;
    public ProductAdapter(Context context, ArrayList<Products> productItemList,String selectedCategoryName,String selectedCategoryId) {
        this.context = context;
        this.productItemList = productItemList;
        this.cartList = new ArrayList<UserCartItem>();
        this.cartItemList = new ArrayList<UserCartItem>();
        this.selectedCategoryName = selectedCategoryName;
        this.selectedCategoryId =selectedCategoryId;
        mSharedPreferences = context.getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        mobileno=mSharedPreferences.getString("user_mobile_no",null);
       getPreviousBasket();

    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_product_list_item, viewGroup, false);
        return new ProductItemViewHolder(itemView);
    }

    @Override
        public void onBindViewHolder(@NonNull final ProductItemViewHolder viewHolder, final int i) {
     //   viewHolder.productImage.setImageResource(R.drawable.newcat1);
        getPreviousBasket();

        varPosition=0;
        getcurQty=checkBasketItemQty(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id());
        vList = new ArrayList<String>();
        for(int n=0;n<productItemList.get(i).getVarientItems().size();n++)
        {
            vList.add(productItemList.get(i).getVarientItems().get(n).getVarType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, vList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.variantSelect.setAdapter(dataAdapter);
      //  getPreviousBasket(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id());
        try {
            String images = productItemList.get(i).getPrdt_images();
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
        String pname=productItemList.get(i).getPdt_name();
        if(pname.contains("_"))
        pname =pname.replace("_"," ");
        viewHolder.productName.setText(pname);
        viewHolder.priceText.setText("\u20B9 " + productItemList.get(i).getVarientItems().get(0).getVarActualAmt());
        viewHolder.discountAmtText.setText("\u20B9 " + productItemList.get(i).getVarientItems().get(0).getVarDisAmt());
        int disper = (Integer.parseInt(productItemList.get(i).getVarientItems().get(0).getVarDisAmt())*100)/Integer.parseInt(productItemList.get(i).getVarientItems().get(0).getVarActualAmt());
        viewHolder.discountPerTag.setText(String.valueOf(disper)+" %");

        if(getcurQty==0)
        {
            currQty=1;
            viewHolder.qut.setVisibility(View.GONE);
            viewHolder.addBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            currQty=getcurQty;
            viewHolder.qut.setVisibility(View.GONE);
            viewHolder.addBtn.setVisibility(View.GONE);
            viewHolder.prdctQty.setText(String.valueOf(currQty));
        }


        viewHolder.variantSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    getPreviousBasket(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id());
                varPosition=position;
                getcurQty=checkBasketItemQty(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id());

                if(getcurQty==0)
                {
                    currQty=1;
                    viewHolder.qut.setVisibility(View.GONE);
                    viewHolder.addBtn.setVisibility(View.VISIBLE);
                }
                else
                {
                    currQty=getcurQty;
                    viewHolder.qut.setVisibility(View.GONE);
                    viewHolder.addBtn.setVisibility(View.GONE);
                    viewHolder.prdctQty.setText(String.valueOf(currQty));
                }

              /*  if(isAvailableVar){
                    viewHolder.qut.setVisibility(View.VISIBLE);
                    viewHolder.addBtn.setVisibility(View.GONE);
                }
                else {
                    viewHolder.qut.setVisibility(View.GONE);
                    viewHolder.addBtn.setVisibility(View.VISIBLE);
                }
              */
                selectedVariant = productItemList.get(i).getVarientItems().get(position).getVarType();
                selectedVariantPrice = productItemList.get(i).getVarientItems().get(position).getVarActualAmt();
                selectedVarianceDiscount= productItemList.get(i).getVarientItems().get(position).getVarDisAmt();
                viewHolder.priceText.setText("\u20B9 " + productItemList.get(i).getVarientItems().get(position).getVarActualAmt());
                viewHolder.discountAmtText.setText("\u20B9 " + productItemList.get(i).getVarientItems().get(position).getVarDisAmt());
                int dispers = (Integer.parseInt(productItemList.get(i).getVarientItems().get(position).getVarDisAmt())*100)/Integer.parseInt(productItemList.get(i).getVarientItems().get(position).getVarActualAmt());
                viewHolder.discountPerTag.setText(String.valueOf(dispers)+" %");

                Log.i("sel vrrr-",selectedVariant);
                Log.i("sel vrrpp-",selectedVariantPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        viewHolder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileno != null) {
                    viewHolder.qut.setVisibility(View.GONE);
                    viewHolder.addBtn.setVisibility(View.GONE);
                    selectedVariant=productItemList.get(i).getVarientItems().get(varPosition).getVarType();
                    selectedVariantPrice=productItemList.get(i).getVarientItems().get(varPosition).getVarActualAmt();
                    selectedVarianceDiscount=productItemList.get(i).getVarientItems().get(varPosition).getVarDisAmt();

                    viewHolder.prdctQty.setText(String.valueOf(currQty));
                    saveBasketItem(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id(), new UserCartItem(selectedCategoryName, productItemList.get(i).getVarientItems().get(varPosition).getVar_id(),productItemList.get(i).getPdt_id(), productItemList.get(i).getPdt_name(), productItemList.get(i).getPrdt_images(), currQty, selectedVariant, selectedVariantPrice,selectedVarianceDiscount, "20", String.valueOf(Integer.parseInt(viewHolder.prdctQty.getText().toString()) * Integer.parseInt(selectedVariantPrice))));
                //    getPreviousBasket(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id());
                getPreviousBasket();
                }
                else
                {
                    Toast.makeText(context,"Please login to add product to cart.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPreviousBasket();
                currQty = Integer.parseInt(viewHolder.prdctQty.getText().toString());

                    if(currQty==5) {
                        Toast.makeText(v.getContext(), "You Cannot add any more quantity for this product", Toast.LENGTH_LONG).show();
                        viewHolder.prdctQty.setText(String.valueOf(currQty));
                        cartList.get(i).setQty(currQty);
                    }
                    else
                    {
                        currQty=currQty+1;
                        viewHolder.prdctQty.setText(String.valueOf(currQty));
                      //  cartList.get(i).setQty(currQty);
                      //  cartList.get(i).setTotalItemPrice(String.valueOf(Integer.parseInt(viewHolder.prdctQty.getText().toString()) * Integer.parseInt(selectedVariantPrice)));
                        cartList.get(i).setQty(currQty);
                        cartList.get(i).setTotalItemPrice(String.valueOf(currQty*Integer.parseInt(cartList.get(i).getActual_price())));
                   }
                //saveBasketItem(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id(), new UserCartItem(selectedCategoryName, productItemList.get(i).getVarientItems().get(varPosition).getVar_id(),productItemList.get(i).getPdt_id(), productItemList.get(i).getPdt_name(), productItemList.get(i).getPrdt_images(), currQty, selectedVariant, selectedVariantPrice, "20", String.valueOf(Integer.parseInt(viewHolder.prdctQty.getText().toString()) * Integer.parseInt(selectedVariantPrice))));
                saveBasket();
                }
        });

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPreviousBasket();
                currQty = Integer.parseInt(viewHolder.prdctQty.getText().toString());

                if(currQty<=1) {
                   viewHolder.qut.setVisibility(View.GONE);
                   viewHolder.addBtn.setVisibility(View.VISIBLE);
                    cartList.remove(i);
                    viewHolder.remove.setAlpha(0.5f);
                    viewHolder.remove.setEnabled(false);
                    notifyDataSetChanged();
                }
                else
                {
                    viewHolder.remove.setAlpha(1f);
                    viewHolder.remove.setEnabled(true);
                    currQty=currQty-1;
                    viewHolder.prdctQty.setText(String.valueOf(currQty));
                    cartList.get(i).setQty(currQty);
                    cartList.get(i).setTotalItemPrice(String.valueOf(Integer.parseInt(viewHolder.prdctQty.getText().toString()) * Integer.parseInt(selectedVariantPrice)));

                }
 //               saveBasketItem(productItemList.get(i).getPdt_id(),productItemList.get(i).getVarientItems().get(varPosition).getVar_id(), new UserCartItem(selectedCategoryName, productItemList.get(i).getVarientItems().get(varPosition).getVar_id(),productItemList.get(i).getPdt_id(), productItemList.get(i).getPdt_name(), productItemList.get(i).getPrdt_images(), currQty, selectedVariant, selectedVariantPrice, "20", String.valueOf(Integer.parseInt(viewHolder.prdctQty.getText().toString()) * Integer.parseInt(selectedVariantPrice))));
                saveBasket();
            }
        });


        viewHolder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prdctDetails = new Intent(v.getContext(), ProductDetails.class);
                prdctDetails.putExtra("p_id",productItemList.get(i).getPdt_id());
                prdctDetails.putExtra("p_name",productItemList.get(i).getPdt_name());
                prdctDetails.putExtra("p_desc",productItemList.get(i).getPdt_about());
                prdctDetails.putExtra("var_id",productItemList.get(i).getVarientItems().get(varPosition).getVar_id());
                prdctDetails.putExtra("p_var",selectedVariant);
                prdctDetails.putExtra("p_images",productItemList.get(i).getPrdt_images());
                prdctDetails.putExtra("p_actualP",selectedVariantPrice);
                prdctDetails.putExtra("p_dis",selectedVarianceDiscount);
                prdctDetails.putExtra("p_price",selectedVariantPrice);
                prdctDetails.putExtra("p_qty",currQty);
                prdctDetails.putExtra("p_cat",selectedCategoryName);
                prdctDetails.putExtra("p_catid",selectedCategoryId);

                v.getContext().startActivity(prdctDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
        TextView priceText,prdctQty,productName,discountAmtText,discountPerTag;
        ImageView productImage,add,remove;
        Spinner variantSelect;
        Button addBtn;
        LinearLayout qut;
        CardView productCard;
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
          /*  catImage = itemView.findViewById(R.id.catImage);
            catText = itemView.findViewById(R.id.catText); */
            productName = itemView.findViewById(R.id.productName);
            discountAmtText = itemView.findViewById(R.id.discountText);
            discountPerTag = itemView.findViewById(R.id.discountTag);
            productImage = itemView.findViewById(R.id.productImage);
            priceText = itemView.findViewById(R.id.priceText);
            addBtn = itemView.findViewById(R.id.addButton);
            variantSelect = itemView.findViewById(R.id.spinnerVariant);
            qut = itemView.findViewById(R.id.ll12);
            prdctQty  = itemView.findViewById(R.id.productQTY);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);
            productCard = itemView.findViewById(R.id.prdctCard);
        }
    }

    public void saveBasketItem(String pdt_id,String var_id,UserCartItem cartItem)
     {
        // Toast.makeText(context,"Saving Basket Item",Toast.LENGTH_LONG).show();
         ArrayList<UserCartItem> basketList;
         Gson gson = new Gson();
         basketList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
         }.getType());
         if (basketList == null) {
             basketList = new ArrayList<>();
         }

         for (int n = 0; n < basketList.size(); n++) {
             if (basketList.get(n).getPdt_id().equals(pdt_id)) {
             //    Toast.makeText(context,"Product exists",Toast.LENGTH_LONG).show();
                 if(basketList.get(n).getVar_id().equals(var_id)) {
               //      Toast.makeText(context,"Product Variant exists"+basketList.get(n).getVariant(),Toast.LENGTH_LONG).show();

                     isAvailable = true;
                     basketList.get(n).setQty(basketList.get(n).getQty() + 1);
                     basketList.get(n).setTotalItemPrice(String.valueOf((basketList.get(n).getQty()) * Integer.parseInt(basketList.get(n).getActual_price())));
                     MyCart.setTotalPrice();
                     break;
                 }
                 else {
                     isAvailable = false;
             }
             }
         }
         if (!isAvailable) {
                        basketList.add(cartItem);
         }

         //Utility.mPreferenceSettings().setBasketList(basketList);
         Gson gsonp = new Gson();
         mEditor.putString(KEY_BASKET, gsonp.toJson(basketList)).commit();
         Toast.makeText(context, "Product added successfully!", Toast.LENGTH_SHORT).show();
     }

    public int checkBasketItemQty(String pdt_id,String var_id)
    {
        ArrayList<UserCartItem> basketList;
        Gson gson = new Gson();
        basketList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());
        if (basketList == null) {
            return 0;
        }
        else
        {
            for (int n = 0; n < basketList.size(); n++) {
                if (basketList.get(n).getPdt_id().equals(pdt_id)) {
                    if(basketList.get(n).getVar_id().equals(var_id)) {
                        return basketList.get(n).getQty();
                    }
                }  }

        }
        return 0;
    }


    private void getPreviousBasket(){//(String pdt_id,String var_id) {
        Gson gson = new Gson();
        cartList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());
      }

    public  void saveBasket()
    {
     //   Toast.makeText(context,"Saving product..",Toast.LENGTH_LONG).show();
        Gson gsonp = new Gson();
        mEditor.putString(KEY_BASKET, gsonp.toJson(cartList)).commit();
        if(MyCart.basketList!=null)
            MyCart.basketList=cartList;
    }
}
