package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.BaseResponse;
import com.e.laxmibrand.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends Fragment {

    Button shareAppBtn;
    String description,email,mobno;
TextInputEditText descET,mobilenoET,emailET;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contactus, container, false);
        descET = root.findViewById(R.id.descET);
        mobilenoET = root.findViewById(R.id.mobileNoET);
        emailET = root.findViewById(R.id.emailET);


        getContactUs();
        return root;
    }



    private void getContactUs() {
        if (Utility.isOnline(getActivity())) {
            final Dialog dialog = Utility.showProgress(getActivity());
            Call<ResponseBody> get = Utility.retroInterface().getContactUs();
            get.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Utility.dismissDialog(dialog);
                    try {
                        if (response.code() == 200) {

                                ResponseBody responseBody = response.body();
                                String s = responseBody.string();
                                JSONObject object = new JSONObject(s);
                               // JSONObject objRO = object.getJSONObject("data");
                            JSONArray jA = object.getJSONArray("data");
                            for (int j = 0; j < jA.length(); j++) {
                                JSONObject objVar = jA.getJSONObject(j);
                                description = objVar.getString("description");
                                 email = objVar.getString("email");
                                 mobno = objVar.getString("mob_no");

                            }

                                descET.setText(description);
                                mobilenoET.setText(mobno);
                                emailET.setText(email);


                        }
                    } catch (Exception e) {
                        Utility.somethingWentWrong(getActivity());
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utility.dismissDialog(dialog);
                    Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();

                    Utility.somethingWentWrong(getActivity());
                }
            });
        }
        else
        {
            Utility.noInternetError(getActivity());

        }


    }


}
