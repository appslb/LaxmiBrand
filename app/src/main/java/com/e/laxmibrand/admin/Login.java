package com.e.laxmibrand.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.LoginResponse;
import com.e.laxmibrand.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
Button btnContinue;
TextInputEditText userid,password;
String str_userid,str_password;
Context context;
RequestBody email,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        btnContinue = (Button) findViewById(R.id.btn_continue);
        userid = (TextInputEditText) findViewById(R.id.userid);
        password= (TextInputEditText) findViewById(R.id.password);
        context=Login.this;
        btnContinue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                str_userid=userid.getText().toString();
                str_password=password.getText().toString();
                if (!userid.getText().toString().trim().isEmpty()) {
                    if (!password.getText().toString().trim().isEmpty()) {
                        if (Utility.isOnline(v.getContext())) {
                            email = RequestBody.create(MediaType.parse("multipart/form-data"), userid.getText().toString().trim());
                            pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString().trim());
                            loginAPI(email,
                                    pass);
                        } else {
                            Utility.noInternetError(v.getContext());
                        }
                    } else {
                        Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Enter Username", Toast.LENGTH_SHORT).show();
                }
           }
        });
    }
    private void loginAPI(RequestBody email, RequestBody password) {
        final Dialog dialog = Utility.showProgress(Login.this);
     //   String authkey = Utility.mPreferenceSettings().getFirebaseToken();
        Call<LoginResponse> login = Utility.retroInterface().verify_login(email, password);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus()) {
                            Toast.makeText(context, "Login success.", Toast.LENGTH_SHORT).show();
                            /*Utility.mPreferenceSettings().setIsLogin(true);
                            Utility.mPreferenceSettings().setUserDetails(response.body().getResponseData());
                            Utility.mPreferenceSettings().setUserId(response.body().getResponseData().getUserid());
                            Log.i("USERNAME : ",response.body().getResponseData().getUserName());
                            Log.i("PASSWORD : ",response.body().getResponseData().getPassword());
*/
                          //  if (response.body().getResponseData().getUserName() == email) {
                                startActivity(new Intent(context, MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            //}
                            //else {
                              //  Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();
                            //}
                            /*else {
                                startActivity(new Intent(context, MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                           // }*/

                           // finish();
                        }
                        else {
                            // Utility.somethingWentWrong(context);
                            Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else {
                       // Utility.somethingWentWrong(context);
                        Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                                Utility.somethingWentWrong(context);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(Login.this);
            }
        });


    }

}
