package com.e.laxmibrand.admin.product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.admin.PageNoAdapter;
import com.e.laxmibrand.retrofit.RetroInterface;
import com.squareup.picasso.Picasso;


import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.AdminCategory;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.utils.Utility;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   // private ViewPager mViewPager;
    ImageView addProduct,backPressed;
    static String[] cList;
    static RecyclerView pagenorv;
    static PageNoAdapter pagenoAdapter;
    TabLayout tabLayout;
    FrameLayout productListArea;
    DemoObjectFragment productListPage;
    FragmentTransaction transaction;
    static Spinner categorySpinner;
    ArrayList<String> list;
    static ArrayList<AdminCategory> catList;
    static int noOfPages=1;
    static int[] pages;
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        context=ProductList.this;
       // mViewPager = (ViewPager) findViewById(R.id.pager);
        addProduct = (ImageView) findViewById(R.id.addProduct);
     //   tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        productListArea = (FrameLayout) findViewById(R.id.productListArea);
        categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
        backPressed = (ImageView) findViewById(R.id.back_pressed);
        catList = new ArrayList<AdminCategory>();
        pagenorv =(RecyclerView) findViewById(R.id.pagenoRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pagenorv.setLayoutManager(layoutManager);
        addItemsOnSpinner1(noOfPages);
        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categorySpinner.setOnItemSelectedListener(this);

addProduct.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ProductList.this, AddProduct.class));
    }
});
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            productListPage = new DemoObjectFragment();
            Bundle bundle = new Bundle();
            bundle.putString("selected_cat_id", catList.get(pos).getCatid());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            productListPage.setArguments(bundle);
            transaction.replace(R.id.productListArea, productListPage);
            transaction.addToBackStack(null);
            transaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public static void addItemsOnSpinner1(int pageno) {
        if (Utility.isOnline(context)) {

            getAllCategoryDetails(pageno);
        }
        else
        {
            Utility.noInternetError(context);
        }
    }


    private static void getAllCategoryDetails(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
     //   Call<ResponseBody> get = Utility.retroInterface().getAllACategory("2");
        Call<ResponseBody> get = Utility.retroInterface().getCatNew("http://dev.polymerbazaar.com/laxmibrand/admin/category/list/"+pageno);
        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        try {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            JSONObject object = new JSONObject(s);
                            // JSONArray jsonElements = object.getJSONArray("data");
                            JSONObject objRO = object.getJSONObject("data");
                            // JSONObject objR = jsonElements.getJSONObject(0);
                            noOfPages = objRO.getInt("total_pages");
                            JSONArray jA = objRO.getJSONArray("result");
                            cList = new String[jA.length()];
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                String name = obj.getString("category_name");
                                AdminCategory cat = new AdminCategory();
                                cat.setCatName(name);
                                cat.setCatId(obj.getString("category_id"));
                                catList.add(cat);
                                cList[i]=name;
                            }
                         /*   SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context,catList);
                            categorySpinner.setAdapter(spinnerAdapter);*/

                            if(noOfPages>1) {
                                pages = new int[noOfPages];
                                for (int i = 1; i <= noOfPages; i++)
                                    pages[i] = i + 1;
                                pagenoAdapter = new PageNoAdapter(context, pages);
                                pagenorv.setAdapter(pagenoAdapter);
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, cList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinner.setAdapter(dataAdapter);
                        }catch(JSONException je){
                            //Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        // }
                        //  }
                    }
                    else {
                     //   Toast.makeText(context, "other than 200", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                    Utility.somethingWentWrong(context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(context);
            }
        });

    }


}