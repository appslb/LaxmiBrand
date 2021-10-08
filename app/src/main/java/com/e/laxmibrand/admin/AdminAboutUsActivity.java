package com.e.laxmibrand.admin;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/*import com.app.sradesign.R;
import com.app.sradesign.model.response.BaseResponse;
import com.app.sradesign.model.response.GetAllAboutusResponse;
import com.app.sradesign.utils.Utility;*/
import com.e.laxmibrand.R;
import com.e.laxmibrand.user.UserLogin;
import com.e.laxmibrand.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

/*
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
*/

public class AdminAboutUsActivity extends AppCompatActivity {

    Button saveBTN;
    ImageView logoutBTN,backPressed;
    TextInputEditText descET;
    String id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_about_us);
        // Inflate the layout for this fragment

        saveBTN = findViewById(R.id.saveBTN);
        logoutBTN = findViewById(R.id.logoutBTN);
        backPressed = findViewById(R.id.backPressed);
        descET = findViewById(R.id.descET);
        descET.setText("Write About Your CompanyHere............");

        saveBTN.setEnabled(false);
        saveBTN.setAlpha(0.5f);

        descET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setEnabled(true);
                saveBTN.setAlpha(1f);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), UserLogin.class));

            }
        });

        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!descET.getText().toString().trim().isEmpty()) {
                    if (Utility.isOnline(AdminAboutUsActivity.this)) {
                       // upsertAboutUsAPI(descET.getText().toString().trim());
                        Toast.makeText(AdminAboutUsActivity.this, "About Us Details added.", Toast.LENGTH_SHORT).show();

                    } else {
                       // Utility.noInternetError(getActivity());
                        Toast.makeText(AdminAboutUsActivity.this, "No Internet.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(AdminAboutUsActivity.this, "Please enter about us details.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        getAboutUsAPI();

    }

   /* private void getAboutUsAPI() {

        final Dialog dialog = Utility.showProgress(getActivity());
        String authkey = Utility.mPreferenceSettings().getFirebaseToken();
        int uid = Utility.mPreferenceSettings().getUserId();
        Call<GetAllAboutusResponse> get = Utility.retroInterface().getAllAboutus(uid, authkey);
        get.enqueue(new Callback<GetAllAboutusResponse>() {
            @Override
            public void onResponse(Call<GetAllAboutusResponse> call, Response<GetAllAboutusResponse> response) {
                Utility.dismissDialog(dialog);
                if (response.code() == 200) {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {
                            id = response.body().getResponseData().get(0).getId();
                            descET.setText(response.body().getResponseData().get(0).getDescription());

                            saveBTN.setEnabled(false);
                            saveBTN.setAlpha(0.5f);

                            descET.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    saveBTN.setEnabled(true);
                                    saveBTN.setAlpha(1f);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                        }
                    } else {
                        if (response.body().getResponseMessage().equalsIgnoreCase("Authentication Key is wrong.")) {
                            Utility.showUnauthorisedDialog(getActivity());
                        } else {
                            Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Utility.somethingWentWrong(getActivity());
                }
            }

            @Override
            public void onFailure(Call<GetAllAboutusResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(getActivity());
            }
        });

    }
*/
  /*  private void upsertAboutUsAPI(String desc) {

        final Dialog dialog = Utility.showProgress(getActivity());
        String authkey = Utility.mPreferenceSettings().getFirebaseToken();
        int uid = Utility.mPreferenceSettings().getUserId();
        Call<BaseResponse> save = Utility.retroInterface().upsertAboutus(id, "", "", "",
                desc, authkey, uid);
        save.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                if (response.code() == 200) {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        saveBTN.setEnabled(false);
                        saveBTN.setAlpha(0.5f);
                        Toast.makeText(getActivity(), "Details saved successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body().getResponseMessage().equalsIgnoreCase("Authentication Key is wrong.")) {
                            Utility.showUnauthorisedDialog(getActivity());
                        } else {
                            Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Utility.somethingWentWrong(getActivity());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(getActivity());
            }
        });

    }
*/
}