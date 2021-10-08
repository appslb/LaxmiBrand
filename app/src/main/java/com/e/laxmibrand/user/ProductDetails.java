package com.e.laxmibrand.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.product.ProductList;
import com.e.laxmibrand.beans.UserCartItem;
import com.e.laxmibrand.user.AddToCart;
import com.e.laxmibrand.user.UserLogin;
import com.e.laxmibrand.user.adapter.DiscountedProductAdapter;
import com.e.laxmibrand.user.adapter.ProductDetailImageAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProductDetails extends AppCompatActivity {
    public static final String PREFS = "laxmi_namkeen";
    TextView tvVariantText,tvProductName, tvProductDesc, tvMRP, tvPrice, tvSaved, tvSavedPer, tvQty, addtocart, product_id, buy_now;
    ImageView ivProductImage, add, remove;
    String v_id,p_id,p_name,p_desc,p_var,p_image,p_actualP,p_price,p_qty,p_cat,p_catid;
    SharedPreferences sp;
    RecyclerView multipleImagesRV;
    String chkAR="A";
    ScrollView view;
    private ProgressBar mProgressBar;
    AddToCart addToCart;
    private SharedPreferences mSharedPreferences;
    private int PRIVATE_MODE = 0;
    private SharedPreferences.Editor mEditor;
    private String KEY_BASKET = "basket list";
    int addCntr=0,remCntr=0,currQty=1,getcurQty=0;
    String mobileno;
    ArrayList<UserCartItem> cartList;
    boolean isAvailable = false;
    ProductDetailImageAdapter productDetailImageAdapter;
    String[] imagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        view = findViewById(R.id.product_page);
        mProgressBar = findViewById(R.id.progressBar);
        addToCart=new AddToCart(ProductDetails.this);
        multipleImagesRV = (RecyclerView) findViewById(R.id.multipleImagesRV);
        cartList = new ArrayList<UserCartItem>();
        mSharedPreferences = getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        mobileno=mSharedPreferences.getString("user_mobile_no",null);
        getPreviousBasket();
        Bundle extras = getIntent().getExtras();
        v_id = extras.getString("var_id");
        p_id = extras.getString("p_id");
        p_name = extras.getString("p_name");
        p_desc = extras.getString("p_desc");
        p_var = extras.getString("p_var");
        p_image= extras.getString("p_images");
        p_actualP = extras.getString("p_actualP");
        p_price = extras.getString("p_price");
        p_qty = String.valueOf(extras.getInt("p_qty"));
        p_cat=extras.getString("p_cat");
        p_catid=extras.getString("p_catid");
        sp = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        addtocart = findViewById(R.id.addtocart);
        product_id = findViewById(R.id.product_id);
        tvProductName = findViewById(R.id.product_name);
        tvProductDesc = findViewById(R.id.product_desc);
        tvVariantText = findViewById(R.id.variant_text);
        tvMRP = findViewById(R.id.mrp);
        tvPrice = findViewById(R.id.price);
        tvSaved = findViewById(R.id.saved);
        tvSavedPer = findViewById(R.id.saved_per);
        tvQty = findViewById(R.id.product_qty);
        ivProductImage = findViewById(R.id.product_img);
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        buy_now = findViewById(R.id.buynow);
     //   tvMRP.setPaintFlags(tvMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mSharedPreferences = ProductDetails.this.getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        final int[] number = {1};
      //  tvQty.setText("" + number[0]);
        buy_now.setVisibility(View.GONE);
        tvProductName.setText(p_name);
        tvProductDesc.setText(p_desc);
        tvVariantText.setText(p_var);
        tvMRP.setText(p_actualP);
        tvPrice.setText(p_price);
        tvQty.setText(p_qty);
        if (!p_image.equals("") && (p_image.contains("https")||p_image.contains("http"))) {
            imagesList = null;
            imagesList = p_image.split(",");
            for(int i=0;i<imagesList.length;i++)
                 Log.i("Image ===",i +" : "+imagesList[i]);
            String S1 = imagesList[0];
            loadImage(S1);
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductDetails.this, LinearLayoutManager.HORIZONTAL, false);
        multipleImagesRV.setLayoutManager(layoutManager);
        productDetailImageAdapter = new ProductDetailImageAdapter(ProductDetails.this,imagesList);
        multipleImagesRV.setAdapter(productDetailImageAdapter);

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String loginid = sp.getString("user_mobile_no", null);
                if (mobileno != null) {
                   saveBasketItem(chkAR,p_id,v_id,new UserCartItem(p_cat,v_id,p_id,p_name,p_image,Integer.parseInt(tvQty.getText().toString()),p_var,p_price,"","20",String.valueOf(Integer.parseInt(tvQty.getText().toString())*Integer.parseInt(p_price))));
                Toast.makeText(v.getContext(),"Added To Cart",Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                    builder.setTitle("Heyy..")
                            .setMessage("To add this item in your cart you have to login first. Do you want to login ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(ProductDetails.this,UserLogin.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No Just Continue ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false);
                    builder.show();
                }
            }
        });
       /* buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String loginid = sp.getString("user_mobile_no", null);
                if (mobileno != null) {
                 saveBasketItem(p_id,v_id,);
                    //   addToCart.addToCart(product_id.getText().toString(),tvQty.getText().toString());
                  //  Intent intent = new Intent(getApplicationContext(), MyCart.class);
                   // startActivity(intent);
                    finish();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                    builder.setTitle("Heyy..")

                            .setMessage("To add this item in your cart you have to login first. Do you want to login ")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(ProductDetails.this, UserLogin.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No Just Continue ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false);
                    builder.show();
                }
            }
        });*/
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkAR="A";
               number[0] = number[0] + 1;
                tvQty.setText("" + number[0]);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkAR="R";
                if (number[0] == 1) {
                    tvQty.setText("" + number[0]);
                }
                if (number[0] > 1) {
                    number[0] = number[0] - 1;
                    tvQty.setText("" + number[0]);
                }
            }
        });
    }

    public void saveBasketItem(String chkAR,String pdt_id, String var_id,UserCartItem cartItem) {
        ArrayList<UserCartItem> basketList;
        Gson gson = new Gson();
        basketList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());
        if (basketList == null) {

            basketList = new ArrayList<>();
        }
      //  boolean isAvailable = false;
        for (int n = 0; n < basketList.size(); n++) {
            if (basketList.get(n).getPdt_id().equals(pdt_id)) {
                if (basketList.get(n).getVar_id().equals(var_id)) {
                    isAvailable = true;
                    if(chkAR.equals("A"))
                    basketList.get(n).setQty(basketList.get(n).getQty() + 1);
                    else
                        basketList.get(n).setQty(basketList.get(n).getQty() - 1);

                    basketList.get(n).setTotalItemPrice(String.valueOf((basketList.get(n).getQty()) * Integer.parseInt(basketList.get(n).getActual_price())));
                    MyCart.setTotalPrice();
                    break;
                } else {
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
    }
@Override
public void onBackPressed()
{
    Intent intent = new Intent(ProductDetails.this, UserProductList.class);


    intent.putExtra("selectedCategoryName",p_cat);
    intent.putExtra("selectedCategoryId",p_catid);
   startActivity(intent);
}



    private void getPreviousBasket(){//(String pdt_id,String var_id) {
        Gson gson = new Gson();
        cartList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());
    }

    public  void saveBasket()
    {
        Gson gsonp = new Gson();
        mEditor.putString(KEY_BASKET, gsonp.toJson(cartList)).commit();
    }

    public void loadImage(String Simg1)
    {
        try {

               /* Glide.with(ProductDetails.this)
                        .load(Simg1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(ivProductImage);*/
            Glide
                    .with(ProductDetails.this)
                    .load(Simg1)
                    .apply(new RequestOptions().override(600, 200))
                    .into(ivProductImage);


        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }
    }
}
