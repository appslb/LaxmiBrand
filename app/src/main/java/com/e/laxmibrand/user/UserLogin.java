package com.e.laxmibrand.user;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.PreferenceSettings;
import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.Login;
import com.e.laxmibrand.admin.MainActivity;
import com.e.laxmibrand.beans.BaseResponse;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.user.UserHome;
import com.e.laxmibrand.user.adapter.UserOrderAdapter;
import com.e.laxmibrand.utils.Utility;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin extends AppCompatActivity {
    Button btnContinue;
    TextInputEditText mobileNumberET;
    TextView skipToHome,loginAsAdmin;
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    JsonObject userDetails;
    private String android_id;
    String mobileno,userid,deviceid;
    Context context;

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = UserLogin.this;
        sharedPref = context.getSharedPreferences("laxmi_brand",Context.MODE_PRIVATE);

        mobileno = sharedPref.getString("user_mobile_no", null);
        android_id  = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
       if(mobileno!=null)
        {
            userid = sharedPref.getString("user_id", null);
            deviceid = sharedPref.getString("device_id", null);
            startActivity(new Intent(UserLogin.this, MainPage2.class));


        }
       else
           {

               setContentView(R.layout.activity_login);
            btnContinue = (Button) findViewById(R.id.btn_continue);
            mobileNumberET = (TextInputEditText) findViewById(R.id.mobileNoET);
            skipToHome = (TextView) findViewById(R.id.skipLoginText);
            loginAsAdmin = (TextView) findViewById(R.id.loginAsAdmin);
            userDetails = new JsonObject();
            btnContinue.setEnabled(false);
            btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg_disabled));

            mobileNumberET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mobileNumberET.getText().toString().length() == 10 && !mobileNumberET.getText().toString().equals("0000000000") && (mobileNumberET.getText().toString().startsWith("6") || mobileNumberET.getText().toString().startsWith("7") || mobileNumberET.getText().toString().startsWith("8") || mobileNumberET.getText().toString().startsWith("9"))) {
                        btnContinue.setEnabled(true);
                      //  btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg));
                        btnContinue.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        btnContinue.setEnabled(false);
                        btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg_disabled));
                     //   btnContinue.setTextColor(getResources().getColor(R.color.primaryColor));
                        mobileNumberET.setError("Enter Valid Mobile Number");
                    }
                }
            });


            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  startActivity(new Intent(UserLogin.this, MainPage2.class));

                    if (!mobileNumberET.getText().toString().trim().isEmpty()) {
                        if ((mobileNumberET.getText().toString().startsWith("6") ||
                                mobileNumberET.getText().toString().startsWith("7") ||
                                mobileNumberET.getText().toString().startsWith("8") ||
                                mobileNumberET.getText().toString().startsWith("9")) &&
                                mobileNumberET.getText().toString().trim().length() == 10) {

                            if (Utility.isOnline(v.getContext())) {
                               // userDetails.addProperty("uniqueUserId", android_id);
                                userDetails.addProperty("uniqueUserId", android_id);

                                userDetails.addProperty("contactnumber", mobileNumberET.getText().toString().trim());
                                addNewUser(userDetails);

                            } else {
                                Utility.noInternetError(v.getContext());
                            }
                        } else {
                            Toast.makeText(context, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context, "Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            skipToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserLogin.this, MainPage2.class));
                }
            });

            loginAsAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserLogin.this, Login.class));
                }
            });
        }
    }

    private void addNewUser(JsonObject userDetails) {
        final Dialog dialog = Utility.showProgress(context);
        Call<BaseResponse> addCategory = Utility.retroInterface().save_device(userDetails);
        addCategory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                          //  Toast.makeText(context, "Welcome."+ response.body().getResponseData().getMobile_no() + "dev id-"+response.body().getResponseData().getUniquedevice_id(), Toast.LENGTH_SHORT).show();
                          //  SharedPreferences.Editor editor = sharedPref.edit();
                            if(mobileNumberET.getText().toString().equals(response.body().getResponseData().getMobile_no()) && android_id.equals(response.body().getResponseData().getUniquedevice_id())) {
                                startActivity(new Intent(UserLogin.this, MainPage2.class));
                                userid=response.body().getResponseData().getuser_id();
                                sharedPref.edit().putString("user_id", userid).apply();
                                sharedPref.edit().putString("user_mobile_no", mobileNumberET.getText().toString().trim()).apply();
                                sharedPref.edit().putString("device_id",android_id).apply();
                                sharedPref.edit().putInt("order_count",0).apply();

                            }
                            else
                            {
                                Toast.makeText(context, response.body().getResponseData().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            Toast.makeText(context, "Could Not Add User Details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(response.code() == 409){
                        String eb = response.errorBody().string();
                        JSONObject object = new JSONObject(eb);
                        JSONObject objRO = object.getJSONObject("data");
                        String objuid = objRO.getString("user_id");
                        String message = objRO.getString("message");
                        userid=objuid;
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(context, "Could Not Add User Details.", Toast.LENGTH_SHORT).show();


                    }
                } catch (Exception e) {
                    Utility.somethingWentWrong(context);
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(context);
            }
        });


    }

    private void showOfferDialog() {

        new MaterialAlertDialogBuilder(context,R.style.MyMaterialTheme_TabLayout)
                .setTitle("Congratulations")
                .setMessage("Avail for 50 Rs Cashback")
                .setPositiveButton("Ok", /* listener = */    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(),
                                "You clicked on OK", Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .show();

    }


    public synchronized static String getUniqueID(Context context) {
        /*if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }*/

        return uniqueID;
    }


}
