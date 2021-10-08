package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class AboutUs extends Fragment {

    TextInputEditText descET;String description;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_aboutus, container, false);
        descET = root.findViewById(R.id.descET);
        getAboutus();
        return root;
    }



    private void getAboutus() {
        if (Utility.isOnline(getActivity())) {
            final Dialog dialog = Utility.showProgress(getActivity());
            Call<ResponseBody> get = Utility.retroInterface().getAboutUs();
            get.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Utility.dismissDialog(dialog);
                    try {
                        if (response.code() == 200) {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            JSONObject object = new JSONObject(s);
                            JSONArray jA = object.getJSONArray("data");
                            for (int j = 0; j < jA.length(); j++) {
                                JSONObject objVar = jA.getJSONObject(j);
                                description = objVar.getString("about_us_desc");

                            }

                            descET.setText(description);

                        }
                    } catch (Exception e) {
                        Utility.somethingWentWrong(getActivity());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utility.dismissDialog(dialog);

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
