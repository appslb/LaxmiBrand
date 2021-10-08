package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.e.polymerbazaar_poc.ui.events.EventsFragment;
//import com.e.polymerbazaar_poc.ui.plans.PlansFragment;
import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.BaseResponse;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.utils.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.e.laxmibrand.MyApplication.TAG;


public class MainPage2 extends AppCompatActivity {// implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    boolean doubleBackToExitPressedOnce;
    NavigationView navigationView;
    public static ArrayList<Orders> orderList;

    Toolbar toolbar;
    Menu menu;

    public static int order_count,devordercnt;
    BottomNavigationView bnavView;
    TextView textView;
    public static TextView offernews;
    TextView userName,userNo,versionNo;
    View appbarmain2,contentmain;
    FragmentManager fragmentManager;
    Fragment mainF = new Fragment();
    FrameLayout contentFrame;
    public static SharedPreferences sharedPref;
    public static String  mobileno,whatsapp_link,userid,deviceid;
    JsonObject userOrderDetail;
    boolean isFragment;
    int noOfPages;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        context = MainPage2.this;
        sharedPref = getSharedPreferences("laxmi_brand",Context.MODE_PRIVATE);
        mobileno = sharedPref.getString("user_mobile_no", null);
        userid = sharedPref.getString("user_id", null);
        deviceid = sharedPref.getString("device_id", null);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        bnavView = findViewById(R.id.bottom_navigation_view);
        appbarmain2 = findViewById(R.id.appbarmain2);
        contentmain = appbarmain2.findViewById(R.id.maincontent);
        userName = navigationView.findViewById(R.id.userName);
        userNo = header.findViewById(R.id.userNo);
        versionNo = navigationView.findViewById(R.id.versionNo);
        offernews = header.findViewById(R.id.offerNews);
        getWALink();

        if(userid!=null) {
            userOrderDetail = new JsonObject();
            userOrderDetail.addProperty("user_mobile", mobileno);
            userOrderDetail.addProperty("unique_deviceid", deviceid);
            userOrderDetail.addProperty("user_id", userid);
            getAllOrder();
            noOfPages=1;
         //   devordercnt=0;
            orderList = new ArrayList<Orders>();
           // getAdminOrder(noOfPages);
        }

        if(mobileno==null)
        navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Login");
        else
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Logout");

        userNo.setText(mobileno);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("LAXMI NAMKEEN");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        contentFrame =findViewById(R.id.content_frame);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        sharedPref.edit().putString("user_mobile_no",null).apply();
                       startActivity(new Intent(MainPage2.this,UserLogin.class));

                        break;
                    case R.id.nav_home:
                        isFragment=false;

                        openFragment(new UserHome(),-1);
                        toolbar.setTitle("LAXMI NAMKEEN");
                        bnavView.setSelectedItemId(R.id.nav_home);

                        break;
                    case R.id.nav_faq:
                        isFragment=true;
                        openFragment(new UserFAQ(),-1);
                        toolbar.setTitle("FAQs");
                        bnavView.setSelectedItemId(0);

                        break;


                    case R.id.nav_orders:
                        isFragment=true;

                        if(mobileno!=null) {
                            openFragment(new UserOrderList(), -1);
                            toolbar.setTitle("ORDERS");
                        }
                        else{
                            Toast.makeText(MainPage2.this,"Please login to view your orders",Toast.LENGTH_SHORT).show();
                        }
                        bnavView.setSelectedItemId(R.id.nav_orders);

                        break;

                    case R.id.nav_cart:

                        if(mobileno!=null) {
                            isFragment = true;

                            openFragment(new MyCart(), -1);
                            toolbar.setTitle("MY CART");
                            bnavView.setSelectedItemId(R.id.navigation_cart);
                        }
                        else
                        {
                            Toast.makeText(context,"Please login to view Cart Items.",Toast.LENGTH_SHORT).show();

                        }

                        break;

                    case R.id.nav_worder:

                        isFragment=true;

                        try {
                            Intent intentWhatsapp = new Intent(Intent.ACTION_VIEW);
                        //    String url = "https://chat.whatsapp.com/Gi4bfKvqhow6lkI8phJtu2";
                            intentWhatsapp.setData(Uri.parse(whatsapp_link));
                            intentWhatsapp.setPackage("com.whatsapp");
                            startActivity(intentWhatsapp);
                        } catch (Exception e){
                            Toast.makeText(MainPage2.this, "Could not open link.", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case R.id.nav_category:
                        isFragment=true;

                        openFragment(new UserCategory(),-1);
                        toolbar.setTitle("CATEGORIES");
                        bnavView.setSelectedItemId(R.id.navigation_category);

                        break;


                    case R.id.nav_share:
                        isFragment=true;

                        openFragment(new ShareAppScreen(),-1);
                        toolbar.setTitle("SHARE");
                        break;

                    case R.id.nav_contactus:
                        isFragment=true;

                        openFragment(new ContactUs(),-1);
                        toolbar.setTitle("CONTACT US");
                        break;

                    case R.id.nav_aboutus:
                        isFragment=true;

                        openFragment(new AboutUs(),-1);
                        toolbar.setTitle("ABOUT US");
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START); return true;
            }
        });
        navigationView.setCheckedItem(R.id.nav_home);
        bnavView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
       // openFragment(HomeFragment.newInstance("", ""));
openFragment(new UserHome(),-1);
//loadFragment(new Fragment_main());

   }
    public void openFragment(Fragment fragment, int child_id) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(child_id!=-1) {
            Bundle b = new Bundle();
            b.putString("orderid", String.valueOf(child_id));
            fragment.setArguments(b);
        }
        transaction.replace(R.id.content_frame, fragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            isFragment=false;
                            //openFragment(HomeFragment.newInstance("", ""));
                            openFragment(new UserHome(),-1);
                            toolbar.setTitle("LAXMI NAMKEEN");
                            return true;

                        case R.id.navigation_category:
                            isFragment=true;
                            //openFragment(HomeFragment.newInstance("", ""));
                            openFragment(new UserCategory(),-1);
                            toolbar.setTitle("CATEGORIES");
                            return true;

                        case R.id.navigation_order:

                            if(mobileno!=null) {
                                isFragment=true;
                                openFragment(new UserOrderList(), -1);
                                toolbar.setTitle("ORDERS");
                            }
                            else{
                                isFragment=false;

                                Toast.makeText(MainPage2.this,"Please login to view your orders",Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        case R.id.navigation_cart :
                            if(mobileno!=null) {
                                isFragment = true;

                                openFragment(new MyCart(), -1);
                                toolbar.setTitle("MY CART");
                            }
                            else
                                Toast.makeText(context,"Please login to view Cart Items.",Toast.LENGTH_SHORT).show();


                            return true;


                    }
                    return false;
                }
            };

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm= getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit(); // save the changes
    }


    private void getWALink() {
        if (Utility.isOnline(MainPage2.this)) {
            final Dialog dialog = Utility.showProgress(MainPage2.this);
            Call<BaseResponse> get = Utility.retroInterface().getWhatsappGroupLink();
            get.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Utility.dismissDialog(dialog);
                    try {
                        if (response.code() == 200) {
                            if (response.body().getStatus().equalsIgnoreCase("success")) {
                                whatsapp_link = response.body().getResponseData().getWhatsapp_grp_redirect_link();
                                // Toast.makeText(MainPage2.this, whatsapp_link, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                      //                       Utility.somethingWentWrong(MainPage2.this);
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Utility.dismissDialog(dialog);
                  //  Toast.makeText(MainPage2.this, "Get Whatsapp Link Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

                 //   Utility.somethingWentWrong(MainPage2.this);
                }
            });
        }
        else
        {
            Utility.noInternetError(MainPage2.this);

        }


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
                            int records = objRO.getInt("total_record");
                            JSONArray jA = objRO.getJSONArray("result");
                            order_count=records;
                            sharedPref.edit().putInt("order_count",order_count).apply();
                            discountLabelshow();
                           /* if(mobileno!=null &&  order_count < 2)
                            {
                                offernews.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                offernews.setVisibility(View.GONE);
                            }*/
                        }catch(JSONException je){
                            Toast.makeText(context, "Network Error.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                  //      Toast.makeText(context, "other than 200", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                 //   Utility.somethingWentWrong(context);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.dismissDialog(dialog);
               // Utility.somethingWentWrong(context);
            }
        });

    }

    private  void getAdminOrder(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
        //   Call<ResponseBody> get = Utility.retroInterface().getAllACategory("2");
        Call<ResponseBody> get = Utility.retroInterface().getOrderNew("http://dev.polymerbazaar.com/laxmibrand/admin/order/list/"+pageno);

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
                            int noOfPages2 = objRO.getInt("total_pages");
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                Orders order = new Orders();
                                order.setDeviceid(obj.getString("unique_deviceid"));
                                orderList.add(order);
                            }


                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAdminOrder(noOfPages);
                                Log.i("noOfPages : ", "" + noOfPages2);
                            }
                            else {

                                for(int k=0;k<orderList.size();k++)
                                {
                                    if(deviceid.equals(orderList.get(k).getDeviceid()))
                                    {
                                        devordercnt = devordercnt+1;
                                    }

                                }
                                sharedPref.edit().putInt("devordercnt",devordercnt).apply();

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



    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        if (!isFragment) {
            if (doubleBackToExitPressedOnce) {
                Log.i(TAG, "double click");
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.logo)
                        .setTitle("Laxmi Namkeen")
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //MainPage2.super.onBackPressed();
                                    MainPage2.this.finish();
                                    }

                                }).setNegativeButton("No", null).show();
                return;
            } else {

            }

            this.doubleBackToExitPressedOnce = true;
           /* if (getApplicationContext() == null) {
                return;
            } else {
                Toast.makeText(this, "Please click BACK again to exit",
                        Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);*/
        }
        else{
            super.onBackPressed();
        }
    }


    public static void discountLabelshow()
    {
        order_count =  sharedPref.getInt("order_count",0);

        if(mobileno!=null &&  order_count < 2)
        {
            offernews.setVisibility(View.VISIBLE);
        }
        else
        {
            offernews.setVisibility(View.GONE);
        }
    }

}
