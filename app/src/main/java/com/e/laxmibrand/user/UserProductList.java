package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.product.ProductListAdapter;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.user.adapter.DiscountedProductAdapter;
import com.e.laxmibrand.user.adapter.ProductAdapter;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProductList extends AppCompatActivity {
    ImageView searchProduct,backPressed;
    TextView selectedCategoryName;
    RecyclerView productList;
    ProductAdapter productAdapter;
        String selCategoryName,selCategoryId;
        Context context;
        ArrayList<String> vList;
        ArrayList<Products> productItemList;
    static ArrayList<Var> varItemList;
    LinearLayout emptyProductArea;
    JsonObject getProductParameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_product_list);
        searchProduct = findViewById(R.id.searchProduct);
        backPressed = findViewById(R.id.back_pressed);
        selectedCategoryName = findViewById(R.id.selectedCategoryName);
        emptyProductArea = findViewById(R.id.empty_product);
        productList = findViewById(R.id.productList);
        selCategoryName = getIntent().getStringExtra("selectedCategoryName");
        selCategoryId = getIntent().getStringExtra("selectedCategoryId");

        selectedCategoryName.setText(selCategoryName);
        getProductParameters =new JsonObject();
        getProductParameters.addProperty("popularity",1);
        getProductParameters.addProperty("price",0);
        getProductParameters.addProperty("sort",1);
        getProductParameters.addProperty("category_id",Integer.valueOf(selCategoryId));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UserProductList.this, LinearLayoutManager.VERTICAL, false);
        productList.setLayoutManager(layoutManager);

        context = UserProductList.this;
        productItemList = new ArrayList<Products>();
        vList = new ArrayList<String>();

        if(Utility.isOnline(context)) {
            getAllProducts(getProductParameters);
        }
        else
        {
            Utility.noInternetError(context);
        }

        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getAllProducts(JsonObject getProductParameters) {
        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getUserProductList(getProductParameters);
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
                            JSONObject objRO = object.getJSONObject("data");
                            JSONArray jA = objRO.getJSONArray("result");

                            int k=0;
                            for (int i = 0; i < jA.length(); i++) {
                                varItemList = new ArrayList<Var>();

                                JSONObject obj = jA.getJSONObject(i);
                                JSONObject objP = obj.getJSONObject("product");

                                Products product = new Products();
                                product.setCategory_id(objP.getString("category_id"));
                             //   if(selCategoryId.equals( obj.getString("category_id"))) {
                                  product.setPdt_id(objP.getString("pdt_id"));
                                  product.setPdt_name(objP.getString("pdt_name"));
                                  product.setPdt_about(objP.getString("pdt_about"));
                                  product.setPrdt_images(objP.getString("prdt_images"));
                                  product.setIs_active(objP.getString("is_active"));
                                JSONArray jVar = obj.getJSONArray("varient");
                                for(int j=0;j<jVar.length();j++)
                                {
                                    JSONObject objVar = jVar.getJSONObject(j);
                                    Var v = new Var();
                                    v.setVar_id(objVar.getString("var_id"));
                                    v.setVarType(objVar.getString("var_type"));
                                    v.setVarIsActive(objVar.getString("is_active"));
                                    v.setVarDisAmt(objVar.getString("var_discount_price"));
                                    v.setVarActualAmt(objVar.getString("var_actual_price"));
                                    varItemList.add(v);
                                }
                                product.setVarientItems(varItemList);


                                productItemList.add(product);
                               // }
                            }

                            if(productItemList.size()>0) {
                                productList.setVisibility(View.VISIBLE);
                                emptyProductArea.setVisibility(View.GONE);
                                productAdapter = new ProductAdapter(context, productItemList, selCategoryName,selCategoryId);//,dataList);
                                productList.setAdapter(productAdapter);
                                productAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                productList.setVisibility(View.GONE);
                                emptyProductArea.setVisibility(View.VISIBLE);

                            }

                        }catch(JSONException je){

                        }

                    }
                    else {
                        Toast.makeText(context, "other than 200", Toast.LENGTH_LONG).show();

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

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(UserProductList.this, MainPage2.class));

    }


}