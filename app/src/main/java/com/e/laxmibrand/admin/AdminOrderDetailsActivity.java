package com.e.laxmibrand.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.app.sradesign.adapter.AdminOrderCatalogueAdapter;
//import com.app.sradesign.model.request.PlaceOrderRequest;
//import com.app.sradesign.model.response.ApiVerifyLoginResponse;
//import com.app.sradesign.model.response.BaseResponse;
//import com.app.sradesign.model.response.GetOrderDetailsByIDResponse;
//import com.app.sradesign.utils.Utility;
//import com.google.gson.Gson;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminOrderDetailsActivity extends AppCompatActivity {

    LinearLayout saveCompleteLL;
    ImageView backBTN, editBTN;
    RecyclerView catalogueRV;
    RecyclerView itemRV;
    String orderid;
    ArrayList<Orders> orderList;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Var> varList;

    AdminOrderDetailsTitleAdapter adminOrderDetailsTitleAdapter;
    Button saveBTN, completeBTN;
    AdminOrderDetailsActivity context;
    TextView totalTV,userDetailsTV,totalQtyTV;
    TextView customerName,customerAddress,customerMobile,dateValue,orderStatusValue,grandTotalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details2);
        context = this;
        orderid = getIntent().getStringExtra("orderid");
        itemRV = findViewById(R.id.orderItemsList);
        customerName = findViewById(R.id.customerNameTV2);
        customerAddress= findViewById(R.id.customerAddress);
        customerMobile=findViewById(R.id.customerMobile);
        dateValue=findViewById(R.id.dateValue);
        grandTotalValue=findViewById(R.id.grandTotalValue);
        orderStatusValue=findViewById(R.id.orderStatusValue);

        itemRV.setLayoutManager(new LinearLayoutManager(AdminOrderDetailsActivity.this));
        orderList = new ArrayList<Orders>();
        orderItemList = new ArrayList<OrderItem>();
        orderList=AdminOrderFragment.orderList;


     for(int i=0;i<orderList.size();i++){
         Log.i("order id:",String.valueOf(orderid));
         Log.i("order id ll:",String.valueOf(orderList.get(i).getOrderID()));

         if(orderid.equals(orderList.get(i).getOrderID())){
             orderItemList=orderList.get(i).getOrderItems();
             customerAddress.setText(orderList.get(i).getAddress()+"\n"+orderList.get(i).getLandmark()+"\n"+orderList.get(i).getCity()+" "+orderList.get(i).getPinCode());
             customerMobile.setText(orderList.get(i).getPhoneNumber());
             dateValue.setText(orderList.get(i).getOrderDate());
             orderStatusValue.setText(orderList.get(i).getOrderStatus());
             grandTotalValue.setText(orderList.get(i).getTotalOrderItem());
         }
     }
        adminOrderDetailsTitleAdapter = new AdminOrderDetailsTitleAdapter(AdminOrderDetailsActivity.this,orderItemList);
        itemRV.setAdapter(adminOrderDetailsTitleAdapter);
        adminOrderDetailsTitleAdapter.notifyDataSetChanged();
    }

}