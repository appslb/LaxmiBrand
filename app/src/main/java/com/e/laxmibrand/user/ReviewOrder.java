package com.e.laxmibrand.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.category.CategoryList;
import com.e.laxmibrand.beans.AdminCategory;
import com.e.laxmibrand.beans.BaseResponse;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.UserCartItem;
import com.e.laxmibrand.user.adapter.ProductAdapter;
import com.e.laxmibrand.user.adapter.ReviewOrderAdapter;
import com.e.laxmibrand.user.adapter.UserCartAdapter;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewOrder extends Fragment {

    RecyclerView cartList;
    public static TextView locationText,grandTotalValue,discountAmount,totalItemDiscount,deliveryCharges,location,totalAmount,totalSaved,start_shoppingBtn; Button placeOrderButton;
    public static ArrayList<UserCartItem> basketList;
    ReviewOrderAdapter reviewOrderAdapter;
    public  Dialog addUserAddressDialog;
    String address,city,pincode,landmark,userid,deviceid,udid,unum,phonenumber,add,cty,landm,pin;
    LinearLayout emptyCartDiaply;
    public static LinearLayout  placeOrderArea;
    RelativeLayout basketArea;
    private static SharedPreferences mSharedPreferences;
    private int PRIVATE_MODE = 0;
    int order_count,devordercnt;
    private static SharedPreferences.Editor mEditor;
    private static String KEY_BASKET = "basket list";
    private String KEY_ORDER = "order list";
    Context context;
    JsonObject orderDetails;

    public static int grandTotal=0,grandDisTot=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.review_order, container, false);
        Gson gson = new Gson();
        mSharedPreferences = getActivity().getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        cartList = (RecyclerView) root.findViewById(R.id.cartList);
        totalAmount = (TextView) root.findViewById(R.id.totalAmount);
        placeOrderArea = (LinearLayout) root.findViewById(R.id.placeOrderArea);
//        totalSaved = (TextView) root.findViewById(R.id.savedAmount);
        placeOrderButton = (Button)root.findViewById(R.id.placeOrderBtn);
        emptyCartDiaply =(LinearLayout) root.findViewById(R.id.empty_cart);
        basketArea=(RelativeLayout) root.findViewById(R.id.basketArea);
        discountAmount = (TextView) root.findViewById(R.id.discountAmount);
        totalItemDiscount = (TextView) root.findViewById(R.id.varDiscountAmt);

        deliveryCharges = (TextView) root.findViewById(R.id.deliveryCharges);
        grandTotalValue = (TextView) root.findViewById(R.id.grandTotalValue);
        locationText = (TextView) root.findViewById(R.id.locationText);
        start_shoppingBtn=(TextView) root.findViewById(R.id.startshopping);
        basketList = new ArrayList<UserCartItem>();
        context = getActivity();
        basketList = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());


        // Toast.makeText(context,"My Cart Order Count -"+order_count,Toast.LENGTH_SHORT).show();
        userid=mSharedPreferences.getString("user_id","");
        deviceid=mSharedPreferences.getString("device_id","");
        phonenumber=mSharedPreferences.getString("user_mobile_no","");
        udid=mSharedPreferences.getString("udevice_id","");
        unum=mSharedPreferences.getString("u_mobile_no","");
      //  devordercnt=mSharedPreferences.getInt("devordercnt",0);
        add = mSharedPreferences.getString("useraddress","");
        cty = mSharedPreferences.getString("usercity","");
        pin = mSharedPreferences.getString("userpincode","");
        landm = mSharedPreferences.getString("userlandmark","");

        if(basketList!=null){
            order_count = mSharedPreferences.getInt("order_count",0);

            basketArea.setVisibility(View.VISIBLE);
            emptyCartDiaply.setVisibility(View.GONE);
            //   Toast.makeText(getActivity(),"basket not null :" + basketList.get(0).getProductname(),Toast.LENGTH_LONG).show();
            placeOrderArea.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            cartList.setLayoutManager(layoutManager);
            reviewOrderAdapter = new ReviewOrderAdapter(getActivity());//,dataList);
            cartList.setAdapter(reviewOrderAdapter);
            locationText.setText(add + " , " + landm + " , " + cty + " , " + pin);
            setTotalPrice();
            int gts = grandTotal-grandDisTot;
          //  int gtd = grandDisTot;
            if(order_count==0 ||order_count==1) {
        //    if(devordercnt==0 ||devordercnt==1) {
                discountAmount.setText("25");
                gts= gts-25;

            }
            else
            {
                discountAmount.setText("0");
                gts= gts-0;

            }

            if(grandTotal > 500)
            {
                deliveryCharges.setText("0.0");
                gts=gts+0;

            }
            else
            {
                deliveryCharges.setText("50.0");
                gts=gts+50;

            }

            grandTotalValue.setText("Amount to Pay : " + String.valueOf(gts));
        }
        else
        {
            placeOrderArea.setVisibility(View.GONE);
            basketArea.setVisibility(View.GONE);
            emptyCartDiaply.setVisibility(View.VISIBLE);
            grandTotalValue.setText("0.0");
            discountAmount.setText("0.0");
            deliveryCharges.setText("0.0");
        }


        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*  ArrayList<OrderItem> orderItems= new ArrayList<OrderItem>();
              Orders order = new Orders();
              order.setDeviceid(mSharedPreferences.getString("device_id",""));
              order.setOrderByUserId("user_"+mSharedPreferences.getString("device_id",""));
              order.setOrderDate("7/22/2021");
              order.setOrderId("1111");
              order.setOrderStatus("request");
              order.setTotalOrderItem(String.valueOf(grandTotal));
              for(int i=0;i<basketList.size();i++){
                  OrderItem oitem = new OrderItem();
                  oitem.setItemId(basketList.get(i).getPdt_id());
                  oitem.setItemName(basketList.get(i).getProductname());
                  oitem.setItemQty(String.valueOf(basketList.get(i).getQty()));
                  oitem.setItemRate(basketList.get(i).getActual_price());
                  oitem.setItemAmount(basketList.get(i).getTotalItemPrice());
                  oitem.setItemWeight(basketList.get(i).getVariant());
                  orderItems.add(oitem);
              }
              order.setOrderItems(orderItems);*/
                saveOrder();
                //    Toast.makeText(v.getContext(),"Order has been placed",Toast.LENGTH_LONG).show();

            }
        });



        start_shoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainPage2.class);
                startActivity(intent);
            }
        });


        return root;
    }


    public static void setTotalPrice()
    {
        grandTotal =0;
        for(int k=0;k<basketList.size();k++)
        {
            grandTotal = grandTotal+Integer.parseInt(basketList.get(k).getTotalItemPrice());
        }
        setTotalDiscount();
        totalAmount.setText("Total Amount: "+"\u20B9 " + String.valueOf(grandTotal));
    }
    public static void setTotalDiscount()
    {
        grandDisTot =0;
        for(int k=0;k<basketList.size();k++)
        {
            //   grandTotal = Common.totalCartPrice;
            grandDisTot = grandDisTot+Integer.parseInt(basketList.get(k).getVar_dis());
        }
        totalItemDiscount.setText("\u20B9 " + String.valueOf(grandDisTot));


    }



    public void saveOrder()
    {
        ArrayList<String> prdctList = new ArrayList<String>();
        ArrayList<String> varList = new ArrayList<String>();
        saveUserAddress();
   }
    public static void saveBasket()
    {
        Gson gsonp = new Gson();
        mEditor.putString(KEY_BASKET, gsonp.toJson(basketList)).commit();
    }

    private void placeOrder() {
        final Dialog dialog = Utility.showProgress(context);
        Call<BaseResponse> placeOrder = Utility.retroInterface().place_order(orderDetails);
        placeOrder.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show();
                            int orderid = response.body().getResponseData().getOrder_id();
                            mSharedPreferences.edit().putInt("order_count",order_count+1).apply();
                            MainPage2.discountLabelshow();

                            ((MainPage2) getActivity()).openFragment(new OrderPlacedScreen(), orderid);

                        } else {
                            Toast.makeText(context, "Could Not Place Order ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Could Not Place Order ", Toast.LENGTH_SHORT).show();

//                        Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Could Not Place Order  Exc"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    Utility.somethingWentWrong(context);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Toast.makeText(context, "Could Not Place Order  Exc"+t.getMessage(), Toast.LENGTH_SHORT).show();

                Utility.somethingWentWrong(context);
            }
        });


    }


    public void saveUserAddress(){
        String add,cty,pin,landm;
        add = mSharedPreferences.getString("useraddress","");
        cty = mSharedPreferences.getString("usercity","");
        pin = mSharedPreferences.getString("userpincode","");
        landm = mSharedPreferences.getString("userlandmark","");

        addUserAddressDialog = new Dialog(getActivity());
        addUserAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addUserAddressDialog.setContentView(R.layout.add_address);
        addUserAddressDialog.show();

        EditText userAddress = (EditText) addUserAddressDialog.findViewById(R.id.addressValue);
        EditText userCity = (EditText) addUserAddressDialog.findViewById(R.id.cityValue);
        EditText userPincode = (EditText) addUserAddressDialog.findViewById(R.id.pincodeValue);
        EditText userLandmark = (EditText) addUserAddressDialog.findViewById(R.id.landmarkValue);
        Button addAddressBtn = (Button) addUserAddressDialog.findViewById(R.id.addAddressButton);


        if(!add.equals("") && !cty.equals("") &&!pin.equals("")&&!landm.equals("")){
            userAddress.setText(add);
            userCity.setText(cty);
            userPincode.setText(pin);
            userLandmark.setText(landm);
            addAddressBtn.setText("PROCEED");
        }
        else {
            userAddress.setText("");
            userCity.setText("");
            userPincode.setText("");
            userLandmark.setText("");
            addAddressBtn.setText("ADD ADDRESS");

        }

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_count = mSharedPreferences.getInt("order_count",0);

                if(!userAddress.getText().toString().trim().equals(""))
                {
                    if(!userCity.getText().toString().trim().equals(""))
                    {
                        if(!userPincode.getText().toString().trim().equals(""))
                        {
                            if(!userLandmark.getText().toString().trim().equals(""))
                            {
                                if (Utility.isOnline(v.getContext())) {
                                    address = (userAddress.getText().toString().trim());
                                    city = (userCity.getText().toString().trim());
                                    pincode = (userPincode.getText().toString().trim());
                                    landmark = (userLandmark.getText().toString().trim());

                                    mSharedPreferences.edit().putString("useraddress",address).apply();
                                    mSharedPreferences.edit().putString("usercity",city).apply();
                                    mSharedPreferences.edit().putString("userpincode",pincode).apply();
                                    mSharedPreferences.edit().putString("userlandmark",landmark).apply();

                                    orderDetails = new JsonObject();
                                    orderDetails.addProperty("user_mobile",phonenumber);
                                    orderDetails.addProperty("unique_deviceid",deviceid);
                                    orderDetails.addProperty("user_id",userid);

                                    if(order_count==0){// && (udid.equals(deviceid) && (unum.equals(phonenumber)))) {
                                        orderDetails.addProperty("total_amount", String.valueOf(grandTotal));
                                        orderDetails.addProperty("discount_amount","25");
                                       // orderDetails.addProperty("discount_amount",String.valueOf(25+grandDisTot));

                                    }
                                    else if(order_count==1){// && (udid.equals(deviceid) && (unum.equals(phonenumber)))) {
                                        orderDetails.addProperty("total_amount", String.valueOf(grandTotal));
                                        orderDetails.addProperty("discount_amount","25");
                                       // orderDetails.addProperty("discount_amount",String.valueOf(25+grandDisTot));

                                    }
                                    else {
                                        orderDetails.addProperty("total_amount", String.valueOf(grandTotal));
                                        orderDetails.addProperty("discount_amount","0");
                                       // orderDetails.addProperty("discount_amount",String.valueOf(grandDisTot));

                                    }

                                    orderDetails.addProperty("address" , address);
                                    orderDetails.addProperty("city" ,city);
                                    orderDetails.addProperty("pincode", pincode);
                                    orderDetails.addProperty("landmark" , landmark);
                                    JsonArray parray = new JsonArray();
                                    for(int i=0;i<basketList.size();i++) {
                                        JsonObject productDetail = new JsonObject();
                                        productDetail.addProperty("pdt_id", basketList.get(i).getPdt_id());
                                        JsonArray varray = new JsonArray();
                                        JsonObject variantDetail = new JsonObject();
                                        variantDetail.addProperty("var_id", basketList.get(i).getVar_id());
                                        variantDetail.addProperty("qty", basketList.get(i).getQty());
                                        varray.add(variantDetail);
                                        productDetail.add("variant",varray);
                                        parray.add(productDetail);
                                    }
                                    orderDetails.add("products",parray);
                                    placeOrder();
                                    addUserAddressDialog.dismiss();
                                    userAddress.setText("");
                                    userCity.setText("");
                                    userPincode.setText("");
                                    userLandmark.setText("");
                                    basketList.clear();
                                    reviewOrderAdapter.notifyDataSetChanged();
                                    mEditor.remove(KEY_BASKET).apply();
                                    grandTotal=0;
                                    totalAmount.setText(String.valueOf(grandTotal));
                                }
                                else
                                {
                                    Utility.noInternetError(v.getContext());

                                }
                            }
                            else
                            {
                                userLandmark.setError("Please Enter Landmark");
                            }

                        }
                        else
                        {
                            userPincode.setError("Please Enter Pin Code");
                        }
                    }
                    else
                    {
                        userCity.setError("Please Enter City");
                    }


                }
                else
                {
                    userAddress.setError("Please Enter Address");
                }

            }
        });

    }

}



