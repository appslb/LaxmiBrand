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
import com.e.laxmibrand.admin.FAQListAdapter;
import com.e.laxmibrand.beans.AdminFAQ;
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

public class UserFAQ extends Fragment {

    RecyclerView faqRV;
    LinearLayout empty_faq;
    Context context;
    FAQListAdapter faqListAdapter;
    ArrayList<AdminFAQ> faq_list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_faq, container, false);

        faqRV = root.findViewById(R.id.faqsList);
        empty_faq=root.findViewById(R.id.empty_faq);
        context=getActivity();
    faq_list = new ArrayList<AdminFAQ>();
        getAllFaqs();

        return root;
    }



    private void getAllFaqs() {

        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getFaqList();
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
                                String faqT = obj.getString("faq_title");
                                String faqA = obj.getString("faq_answer");

                                AdminFAQ faq = new AdminFAQ();
                                faq.setFaqQues(faqT);
                                faq.setFaqid(obj.getString("id"));
                                faq.setFaqis_active(obj.getString("is_active"));
                                faq.setFaqAns(faqA);
                                faq_list.add(faq);

                            }

                            if(faq_list.size()>0) {
                                empty_faq.setVisibility(View.GONE);
                                faqRV.setVisibility(View.VISIBLE);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
faqRV.setLayoutManager(layoutManager);
                                faqListAdapter = new FAQListAdapter(context, faq_list);
                                faqRV.setAdapter(faqListAdapter);
                                faqListAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                empty_faq.setVisibility(View.VISIBLE);
                                faqRV.setVisibility(View.GONE);
                            }

                        } catch (JSONException je) {
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
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
