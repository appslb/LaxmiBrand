package com.e.laxmibrand.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


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
public class ContactUsActivity extends AppCompatActivity {

    Button saveBTN;
    ImageView logoutBTN,backPressed;
    TextInputEditText descET, emailET, mobileNoET;
    String srno = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        saveBTN = findViewById(R.id.saveBTN);
       logoutBTN = findViewById(R.id.logoutBTN);
        backPressed = findViewById(R.id.backPressed);
        descET = findViewById(R.id.descET);
        emailET = findViewById(R.id.emailET);
        mobileNoET = findViewById(R.id.mobileNoET);
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


        descET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setAlpha(1f);
                saveBTN.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setAlpha(1f);
                saveBTN.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mobileNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setAlpha(1f);
                saveBTN.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveBTN.setAlpha(0.5f);
        saveBTN.setEnabled(false);


        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!descET.getText().toString().trim().isEmpty()) {
                    if (!emailET.getText().toString().trim().isEmpty()) {
                        if (Utility.isValidEmail(emailET.getText().toString().trim())) {
                            if (!mobileNoET.getText().toString().trim().isEmpty()) {
                                if (Utility.isOnline(ContactUsActivity.this)) {
                                    saveDetailsAPI(descET.getText().toString().trim(),
                                            emailET.getText().toString().trim(),
                                            mobileNoET.getText().toString().trim());
                                } else {
                                  //  Utility.noInternetError(ContactUsActivity.this);
                                }
                            } else {
                                Toast.makeText(ContactUsActivity.this, "Please enter phone no.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ContactUsActivity.this, "Please enter valid email address.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContactUsActivity.this, "Please enter email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContactUsActivity.this, "Please enter description.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //getContactUsDetailsAPI();

    }

    private void saveDetailsAPI(String description, String email, String phone) {

       /* final Dialog dialog = Utility.showProgress(getActivity());
        String authkey = Utility.mPreferenceSettings().getFirebaseToken();
        int uid = Utility.mPreferenceSettings().getUserId();
        Call<BaseResponse> save = Utility.retroInterface().upsertContactus(srno, "0", phone, "",
                email, "", "", "0", description, authkey, uid);
        save.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                if (response.code() == 200) {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        Toast.makeText(getActivity(), "Details saved successfully.", Toast.LENGTH_SHORT).show();
                        saveBTN.setAlpha(0.5f);
                        saveBTN.setEnabled(false);
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
*/
        Toast.makeText(ContactUsActivity.this,"Details Saved", Toast.LENGTH_SHORT).show();

    }

   /* private void getContactUsDetailsAPI() {

        final Dialog dialog = Utility.showProgress(getActivity());
        String authkey = Utility.mPreferenceSettings().getFirebaseToken();
        int uid = Utility.mPreferenceSettings().getUserId();
        Call<GetAllContactusResponse> get = Utility.retroInterface().getAllContactus(uid, authkey);
        get.enqueue(new Callback<GetAllContactusResponse>() {
            @Override
            public void onResponse(Call<GetAllContactusResponse> call, Response<GetAllContactusResponse> response) {
                Utility.dismissDialog(dialog);
                if (response.code() == 200) {
                    if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                        if (response.body().getResponseData() != null &&
                                response.body().getResponseData().size() > 0) {
                            srno = response.body().getResponseData().get(0).getSrno();
                            descET.setText(response.body().getResponseData().get(0).getDescription());
                            emailET.setText(response.body().getResponseData().get(0).getEmail());
                            mobileNoET.setText(response.body().getResponseData().get(0).getMobileNo());

                            saveBTN.setAlpha(0.5f);
                            saveBTN.setEnabled(false);

                            descET.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    saveBTN.setAlpha(1f);
                                    saveBTN.setEnabled(true);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            emailET.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    saveBTN.setAlpha(1f);
                                    saveBTN.setEnabled(true);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            mobileNoET.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    saveBTN.setAlpha(1f);
                                    saveBTN.setEnabled(true);
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
            public void onFailure(Call<GetAllContactusResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(getActivity());
            }
        });

    }*/
}