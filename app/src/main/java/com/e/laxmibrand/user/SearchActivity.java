package com.e.laxmibrand.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.product.ProductListAdapter;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.user.adapter.ProductAdapter;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    LinearLayout ll;
    public static final String PREFS = "PREFS";
    SharedPreferences sp;
    int cart_count;
    Context context;
    LinearLayout empty_search;
    RecyclerView search_list;
    static int noOfPages;
     String search_text;
    static ProductAdapter productAdapter;
    static ArrayList<Products> productItemList;
    static ArrayList<Var> varItemList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.search_activity);
        super.onCreate(savedInstanceState);
        ll = findViewById(R.id.ll_products);
        empty_search = findViewById(R.id.ll_empty);
        search_list = findViewById(R.id.search_list);
        Bundle bundle = getIntent().getExtras();
         search_text = getIntent().getStringExtra("search_text");
        productItemList = new ArrayList<Products>();
        varItemList = new ArrayList<Var>();

        context = SearchActivity.this;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        search_list.setLayoutManager(layoutManager);

        noOfPages=1;
getAllProducts(noOfPages);
    }


    public   void getAllProducts(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
        JsonObject prdctObject;
        prdctObject = new JsonObject();

        prdctObject.addProperty("popularity", "0");
        prdctObject.addProperty("price", "0");
        prdctObject.addProperty("sort", "0");

        Call<ResponseBody> get = Utility.retroInterface().getProductsByPage("http://dev.polymerbazaar.com/laxmibrand/admin/product/list/"+pageno,prdctObject);
        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        try {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            Log.i("Response : ",s);
                            JSONObject object = new JSONObject(s);
                            // JSONArray jsonElements = object.getJSONArray("data");
                            JSONObject objRO = object.getJSONObject("data");
                            // JSONObject objR = jsonElements.getJSONObject(0);
                            int noOfPages2 = objRO.getInt("total_pages");
                            JSONArray jA = objRO.getJSONArray("result");
                            for (int i = 0; i < jA.length(); i++) {
                                varItemList = new ArrayList<Var>();
                                JSONObject obj = jA.getJSONObject(i);
                                JSONObject objP = obj.getJSONObject("product");
                                Products product = new Products();
                                product.setPdt_name(objP.getString("pdt_name"));
                             if(( objP.getString("pdt_name").toLowerCase().contains(search_text.toLowerCase()))) {

                                product.setCategory_id(objP.getString("category_id"));
                                    product.setPdt_id(objP.getString("pdt_id"));
                                    product.setPdt_about(objP.getString("pdt_about"));
                                    product.setPrdt_images(objP.getString("prdt_images"));
                                    product.setIs_active(objP.getString("is_active"));
                                    JSONArray jVar = obj.getJSONArray("varient");
                                    for(int j=0;j<jVar.length();j++)
                                    {
                                        JSONObject objVar = jVar.getJSONObject(j);
                                        Var v = new Var();
                                        v.setVarType(objVar.getString("var_type"));
                                        v.setVarIsActive(objVar.getString("is_active"));
                                        v.setVarDisAmt(objVar.getString("var_discount_price"));
                                        v.setVarActualAmt(objVar.getString("var_actual_price"));
                                        varItemList.add(v);
                                    }
                                    product.setVarientItems(varItemList);

                                    productItemList.add(product);
                                }
                            }
                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllProducts(noOfPages);
                                Log.i("noOfPages : " ,""+noOfPages2);
                                }

                            if(productItemList.size()>0)
                            {
                                search_list.setVisibility(View.VISIBLE);
                                empty_search.setVisibility(View.GONE);
                                productAdapter = new ProductAdapter(context,productItemList,"","");
                                search_list.setAdapter(productAdapter);
                                productAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                search_list.setVisibility(View.GONE);
                                empty_search.setVisibility(View.VISIBLE);
                            }

                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        //recyclerView.setAdapter(new AdminSalesmanUserAdapter(getActivity(),salesmanDetails));
                        // }
                        //  }
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

}