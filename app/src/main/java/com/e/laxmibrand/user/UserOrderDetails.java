package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.UserCartItem;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.user.adapter.UserCartAdapter;
import com.e.laxmibrand.user.adapter.UserOrderAdapter;
import com.e.laxmibrand.user.adapter.UserOrderItemListAdapter;
import com.e.laxmibrand.user.adapter.UserOrderTitleListAdapter;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderDetails extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private int PRIVATE_MODE = 0;
    private SharedPreferences.Editor mEditor;
    private String KEY_ORDER = "order list";
    ArrayList<Orders> orderList;
    String orderid;
    int noOfPages;
    ArrayList<Products> productList;
    ArrayList<Products> orderedProductsList;
    LinearLayout not_available;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Var> varList;
    RecyclerView userOrderRV;
    TextView pdiscountAmt,customerNameTV1,deliveryChargesText,deliveryChargeAmount,customerName,customerAddress,customerMobile,dateValue,orderStatusValue,discountAmount,grandTotalValue,grandTotalValue2,offerAvailText;
    int order_count,othdis=0;
    JsonObject userOrderDetail;
    String userid,deviceid,phonenumber,add,total,status,date,mob;
    UserOrderTitleListAdapter orderTitleListAdapter;
   Context context;
   int i=0,m=0,n=0;
    int totalwdis=0;
    ArrayList<Var> varientList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order_details);
        mSharedPreferences = UserOrderDetails.this.getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        order_count = mSharedPreferences.getInt("order_count", 0);

        mEditor = mSharedPreferences.edit();
        not_available = findViewById(R.id.not_available);
        orderid = getIntent().getStringExtra("orderid");
        userOrderRV = (RecyclerView) findViewById(R.id.orderItemsList);
        customerName = findViewById(R.id.customerNameTV2);
        customerAddress= findViewById(R.id.customerAddress);
        customerMobile=findViewById(R.id.customerMobile);
        dateValue=findViewById(R.id.dateValue);
        grandTotalValue=findViewById(R.id.grandTotalValue);
        grandTotalValue2=findViewById(R.id.grandTotalValue2);
        discountAmount=findViewById(R.id.discountAmount);
        pdiscountAmt=findViewById(R.id.pdiscountAmount);
        deliveryChargesText=findViewById(R.id.deliveryChargesText);
        deliveryChargeAmount=findViewById(R.id.deliveryChargeAmt);
        orderStatusValue=findViewById(R.id.orderStatusValue);
        offerAvailText=findViewById(R.id.offerAvailText);
        customerNameTV1=findViewById(R.id.customerNameTV1);
        context = UserOrderDetails.this;
        orderList = new ArrayList<Orders>();
        orderItemList = new ArrayList<OrderItem>();
        productList = new ArrayList<Products>();
        orderedProductsList = new ArrayList<Products>();
        noOfPages=1;
        userid=mSharedPreferences.getString("user_id","");
        deviceid=mSharedPreferences.getString("device_id","");
        phonenumber=mSharedPreferences.getString("user_mobile_no","");
        userOrderDetail = new JsonObject();
        userOrderDetail.addProperty("user_mobile",phonenumber);
        userOrderDetail.addProperty("unique_deviceid",deviceid);
        userOrderDetail.addProperty("user_id",userid);
        if(Utility.isOnline(context)) {
            getAllOrder();
        }
        else
        {
            Utility.noInternetError(context);
        }
    }


    public void setOrderItems22()
    {
            if (orderid.equals(orderList.get(0).getOrderID())) {
                for (int p = 0; p < orderList.get(0).getOrderItems().size(); p++) {
                    ArrayList<Var> varItemList = new ArrayList<Var>();
                    Log.i("product id lln:", orderList.get(0).getOrderItems().get(p).getItemid());
                    String pname = getProductName(orderList.get(0).getOrderItems().get(p).getItemid(), orderList.get(0).getOrderItems().get(p).getOrder_varients());
                    OrderItem oi = new OrderItem();
                    oi.setItemName(pname);
                    oi.setOrder_varients(orderList.get(0).getOrderItems().get(p).getOrder_varients());
                    orderItemList.add(oi);
                }

            add=(orderList.get(0).getAddress() + "\n" + orderList.get(0).getLandmark() + "\n" + orderList.get(0).getCity() + " " + orderList.get(0).getPinCode());
            mob=(orderList.get(0).getPhoneNumber());
            date=(orderList.get(0).getOrderDate());
            status=(orderList.get(0).getOrderStatus());


          /*  if(orderList.get(0).getDisAmt().equals(null) || orderList.get(0).getDisAmt().equals(""))
                total = (String.valueOf(Integer.valueOf(orderList.get(0).getTotalOrderItem()) + 0));
            else
                total = (String.valueOf(Integer.valueOf(orderList.get(0).getTotalOrderItem()) + Integer.valueOf(orderList.get(0).getDisAmt())));
*/


        }

        total = orderList.get(0).getTotalOrderItem();
            customerNameTV1.setText(orderList.get(0).getOrderID());
                customerAddress.setText(add);
                customerMobile.setText(mob);
                dateValue.setText(date);
                orderStatusValue.setText(status);
               int chkTotal = Integer.valueOf(total);
               int tdis=chkTotal;
               //Calculation of special discount and product level discount





        pdiscountAmt.setText(String.valueOf(othdis));

        if(orderList.get(0).getDisAmt().equals(null) || orderList.get(0).getDisAmt().equals(""))
        {

        }
        else
        {
            tdis = tdis-Integer.valueOf(orderList.get(0).getDisAmt());
        }


            if(chkTotal <= 500)
                {
                   // totalwdis = chkTotal+50;
                    totalwdis = tdis+50;
                    deliveryChargeAmount.setText("50.0");
                }
                else
                {
                    deliveryChargeAmount.setText("0.0");
                    //totalwdis=chkTotal;
                    totalwdis=tdis;
                }


        /*if (order_count == 1 || order_count <2) {
          //  offerAvailText.setVisibility(View.VISIBLE);
          //  discountAmount.setVisibility(View.VISIBLE);
          discountAmount.setText(orderList.get(0).getDisAmt());

        }
        else
        {
           // discountAmount.setVisibility(View.GONE);
           // offerAvailText.setVisibility(View.GONE);
            discountAmount.setText(orderList.get(0).getDisAmt());

        }
*/

        discountAmount.setText(orderList.get(0).getDisAmt());
       grandTotalValue.setText("Total Amount To Pay :" + (totalwdis-othdis));
       grandTotalValue2.setText(String.valueOf(chkTotal));

       /* if (order_count == 1 || order_count <2) {
                    offerAvailText.setVisibility(View.VISIBLE);
                    discountAmount.setVisibility(View.VISIBLE);
                    grandTotalValue2.setText(String.valueOf(totalwdis-25));

                }
                else
                    {
                    discountAmount.setVisibility(View.GONE);
                    offerAvailText.setVisibility(View.GONE);
                    grandTotalValue2.setText(String.valueOf(totalwdis));

            }*/


        userOrderRV.setLayoutManager(new LinearLayoutManager(
                UserOrderDetails.this));


        if (orderItemList.size() > 0) {
            userOrderRV.setVisibility(View.VISIBLE);
            not_available.setVisibility(View.GONE);
            orderTitleListAdapter = new UserOrderTitleListAdapter(UserOrderDetails.this, orderItemList, orderedProductsList);
            userOrderRV.setAdapter(orderTitleListAdapter);
            orderTitleListAdapter.notifyDataSetChanged();
        } else {
            userOrderRV.setVisibility(View.GONE);
            not_available.setVisibility(View.VISIBLE);
        }

    }

    public void getAllProducts(int pageno) {
        m=m+1;
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
                            else
                            {
                                setOrderItems22();
                            }
                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
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

    private String getProductName(String itemid, ArrayList<Var> order_varients) {

        for(int p=0;p<productList.size();p++)
        {

            if(productList.get(p).getPdt_id().equals(itemid)) {
                Products pr= new Products();
                pr.setPdt_id(productList.get(p).getPdt_id());
                pr.setPdt_name(productList.get(p).getPdt_name());
                return productList.get(p).getPdt_name();
            }
        }

            return "";
        }



    private void getAllOrder() {
        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getUserOrderList(userOrderDetail);
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
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                ArrayList<OrderItem> productsList = new ArrayList<OrderItem>();
                                Orders order = new Orders();
                                if (orderid.equals(obj.getString("order_id"))) {
                                    order.setOrderId(obj.getString("order_id"));
                                    order.setPhoneNumber(obj.getString("user_mobile"));
                                    order.setDeviceid(obj.getString("unique_deviceid"));
                                    order.setOrderDate(obj.getString("order_created_date"));
                                    order.setAddress(obj.getString("address"));
                                    order.setCity(obj.getString("city"));
                                    order.setDisAmt(obj.getString("discount_amount"));
                                    order.setPinCode(obj.getString("pincode"));
                                    order.setLandmark(obj.getString("landmark"));
                                    order.setTotalOrderItem(obj.getString("amount"));
                                    order.setOrderStatus(obj.getString("order_status"));
                                    JSONArray jP = obj.getJSONArray("products");
                                    for (int j = 0; j < jP.length(); j++) {
                                        JSONObject objP = jP.getJSONObject(j);
                                        OrderItem oi = new OrderItem();
                                        oi.setItemId(objP.getString("pdt_id"));
                                        JSONArray jV = objP.getJSONArray("variant");
                                         varientList = new ArrayList<Var>();
                                        for (int k = 0; k < jV.length(); k++) {
                                            JSONObject objV = jV.getJSONObject(k);
                                            Var v = new Var();
                                            v.setVarType(objV.getString("var_type"));
                                            v.setVarActualAmt(objV.getString("var_actual_price"));
                                            v.setVarDisAmt(objV.getString("var_discount_price"));
                                            othdis=othdis+Integer.parseInt(v.getVarDisAmt());
                                            v.setVarQty(objV.getString("var_qty"));
                                            varientList.add(v);
                                        }
                                        oi.setOrder_varients(varientList);
                                        productsList.add(oi);
                                    }
                                    order.setOrderItems(productsList);

                                    orderList.add(order);
                                }
                            }
                            getAllProducts(noOfPages);


                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
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


}