package com.e.laxmibrand.user;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.category.CategoryList;
import com.e.laxmibrand.admin.category.CategoryListAdapter;
import com.e.laxmibrand.beans.AdminCategory;
import com.e.laxmibrand.beans.Category;
import com.e.laxmibrand.user.adapter.CategoryAdapter;
import com.e.laxmibrand.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.e.laxmibrand.R.drawable.cat1new;
import static com.e.laxmibrand.R.drawable.cat2new;
import static com.e.laxmibrand.R.drawable.cat3new;
import static com.e.laxmibrand.R.drawable.cat4new;
import static com.e.laxmibrand.R.drawable.newcat1;
import static com.e.laxmibrand.R.drawable.newcat2;
import static com.e.laxmibrand.R.drawable.newcat3;
import static com.e.laxmibrand.R.drawable.newcat4;
import static com.e.laxmibrand.R.drawable.newcat5;
import static com.e.laxmibrand.R.drawable.newcat6;
import static com.e.laxmibrand.R.drawable.newcat7;
import static com.e.laxmibrand.R.drawable.newcat8;
import static com.e.laxmibrand.R.drawable.newcat9;

public class UserCategory extends Fragment {
    RecyclerView  categoryRecyclerView;
    List<Category> categoryList;
    CategoryAdapter categoryAdapter;
    LinearLayout empty_category;
    static ArrayList<String> newCList;
int noOfPages;
    Context context;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_category, container, false);
        categoryRecyclerView = root.findViewById(R.id.categoryRecycler);
        empty_category = root.findViewById(R.id.empty_category);
          context=getActivity();
        categoryList = new ArrayList<>();
        noOfPages=1;
        if(Utility.isOnline(getActivity())){
        getAllCategoryDetails(noOfPages);}
        else
        {
            Utility.noInternetError(getActivity());
        }
        return root;
    }

    private void setCategoryRecycler(List<Category> categoryDataList) {
        if(categoryDataList.size()>0) {
            categoryRecyclerView.setVisibility(View.VISIBLE);
            empty_category.setVisibility(View.GONE);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            categoryRecyclerView.setLayoutManager(layoutManager);
            categoryAdapter = new CategoryAdapter(getActivity(), categoryDataList, "c");
            categoryRecyclerView.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
        }
        else
        {
            categoryRecyclerView.setVisibility(View.GONE);
            empty_category.setVisibility(View.VISIBLE);

        }
    }



    private void getAllCategoryDetails(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
        //    Call<ResponseBody> get = Utility.retroInterface().getAllCategory();
        Call<ResponseBody> get = Utility.retroInterface().getCategoryByPage("http://dev.polymerbazaar.com/laxmibrand/admin/category/list/"+pageno);
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
                            int noOfPages2 = objRO.getInt("total_pages");

                            JSONArray jA = objRO.getJSONArray("result");

                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                String name = obj.getString("category_name");
                                Category cat = new Category();
                                cat.setCatName(name);
                                cat.setCatImage(obj.getString("image"));

                                cat.setId(obj.getString("category_id"));
                                categoryList.add(cat);

                            }

                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllCategoryDetails(noOfPages);
                                Log.i("noOfPages : ", "" + noOfPages2);
                            }
                            setCategoryRecycler(categoryList);


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
