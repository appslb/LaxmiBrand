package com.e.laxmibrand.admin;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.product.ProductListAdapter;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderDetailsTitleAdapter extends RecyclerView.Adapter<AdminOrderDetailsTitleAdapter.MyViewHolder>{
    Context context;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Var> varItemList;
AdminOrderDetailsItemAdapter adminOrderDetailsItemsAdapter;
    ArrayList<Products> productList;

    int noOfPages;
    public AdminOrderDetailsTitleAdapter(Context context, ArrayList<OrderItem> orderItemList) {
        this.context = context;
        noOfPages=1;
        this.orderItemList=orderItemList;
        productList =new ArrayList<Products>();
        varItemList = new ArrayList<Var>();
        getAllProducts(noOfPages);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.orderitem_row_list_title, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        String pname = getProductName(orderItemList.get(i).getItemid());
        viewHolder.ItemName.setText(pname);
        varItemList=orderItemList.get(i).getOrder_varients();
        viewHolder.varItem.setLayoutManager(new LinearLayoutManager(context));
        adminOrderDetailsItemsAdapter = new AdminOrderDetailsItemAdapter(context,varItemList);
        viewHolder.varItem.setAdapter(adminOrderDetailsItemsAdapter);
        adminOrderDetailsItemsAdapter.notifyDataSetChanged();

    }

    private String getProductName(String itemid) {
        for(int p=0;p<productList.size();p++)
        {
            if(productList.get(p).getPdt_id().equals(itemid))
                return productList.get(p).getPdt_name();
        }
        return itemid;
    }


    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName;
        RecyclerView varItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
varItem=itemView.findViewById(R.id.orderItemsVarList);

        }
    }

    public void getAllProducts(int pageno) {
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
                                JSONObject obj = jA.getJSONObject(i);
                                JSONObject objP = obj.getJSONObject("product");
                                Products product = new Products();
                                product.setPdt_id(objP.getString("pdt_id"));
                                product.setPdt_name(objP.getString("pdt_name"));

                                productList.add(product);
                            }

                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllProducts(noOfPages);


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



