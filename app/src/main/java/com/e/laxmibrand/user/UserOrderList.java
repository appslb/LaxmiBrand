package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.AdminOrderAdapter;
import com.e.laxmibrand.admin.AdminOrderFragment;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.user.adapter.UserOrderAdapter;
import com.e.laxmibrand.user.adapter.UserOrderTitleListAdapter;
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

public class UserOrderList extends Fragment {
    RecyclerView orderRV;
    LinearLayout empty_order;
    UserOrderAdapter userOrderAdapter;
    Context context;
    private static SharedPreferences mSharedPreferences;
    private int PRIVATE_MODE = 0;
    private static SharedPreferences.Editor mEditor;
    public static ArrayList<Orders> orderList;
    JsonObject userOrderDetail;
    String userid,deviceid,phonenumber;
    ArrayList<Integer> othDis;
int othdis=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_order_list, container, false);
        mSharedPreferences = getActivity().getSharedPreferences("laxmi_brand", PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        orderRV = root.findViewById(R.id.orderRV);
        empty_order=root.findViewById(R.id.empty_order);
        context=getActivity();
        userid=mSharedPreferences.getString("user_id","");
        deviceid=mSharedPreferences.getString("device_id","");
        phonenumber=mSharedPreferences.getString("user_mobile_no","");
        orderList=new ArrayList<Orders>();
        userOrderDetail = new JsonObject();
        userOrderDetail.addProperty("user_mobile",phonenumber);
        userOrderDetail.addProperty("unique_deviceid",deviceid);
        userOrderDetail.addProperty("user_id",userid);

if(Utility.isOnline(getActivity())) {
    getAllOrder();
}
else
{
    Utility.noInternetError(getActivity());
}

        return root;
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
                                ArrayList<Var> varientList = new ArrayList<Var>();
                                othdis=0;
                                othDis = new ArrayList<Integer>();
                                Orders order = new Orders();
                                order.setOrderId(obj.getString("order_id"));
                                order.setPhoneNumber(obj.getString("user_mobile"));
                                order.setDeviceid(obj.getString("unique_deviceid"));
                                order.setDisAmt(obj.getString("discount_amount"));
                                order.setOrderDate(obj.getString("order_created_date"));
                                order.setAddress(obj.getString("address"));
                                order.setCity(obj.getString("city"));
                                order.setPinCode(obj.getString("pincode"));
                                order.setLandmark(obj.getString("landmark"));
                                order.setTotalOrderItem(obj.getString("amount"));
                                order.setOrderStatus(obj.getString("order_status"));
                                JSONArray jP = obj.getJSONArray("products");
                                for(int j=0;j< jP.length();j++) {
                                    JSONObject objP = jP.getJSONObject(j);
                                    OrderItem oi = new OrderItem();
                                    oi.setItemId(objP.getString("pdt_id"));
                                    JSONArray jV = objP.getJSONArray("variant");
                                    for(int k=0;k<jV.length();k++) {
                                        JSONObject objV = jV.getJSONObject(k);
                                        Var v = new Var();
                                        v.setVarType(objV.getString("var_type"));
                                        v.setVarActualAmt(objV.getString("var_actual_price"));
                                        v.setVarDisAmt(objV.getString("var_discount_price"));
                                        othdis=othdis+Integer.parseInt(v.getVarDisAmt());
                                        v.setVarQty(objV.getString("var_qty"));
                                        varientList.add(v);
                                        othDis.add(othdis);

                                    }
                                    oi.setOrder_varients(varientList);
                                    productsList.add(oi);
                                }
                                order.setOrderItems(productsList);
                                orderList.add(order);
                            }
                            if(orderList.size()>0){
                                empty_order.setVisibility(View.GONE);
                                orderRV.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                orderRV.setLayoutManager(layoutManager);
                                userOrderAdapter = new UserOrderAdapter(context,orderList,othDis);//,dataList);
                                orderRV.setAdapter(userOrderAdapter);
                                userOrderAdapter.notifyDataSetChanged();
                            }

                            else
                            {
                                orderRV.setVisibility(View.GONE);
                                empty_order.setVisibility(View.VISIBLE);
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

}
