package com.e.laxmibrand.user;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.e.laxmibrand.R;

import com.e.laxmibrand.beans.BaseResponse;
import com.e.laxmibrand.beans.GridItem;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.Var;
import com.e.laxmibrand.user.adapter.CategoryAdapter;
import com.e.laxmibrand.user.adapter.DiscountedProductAdapter;
import com.e.laxmibrand.user.adapter.GridViewAdapter;
import com.e.laxmibrand.user.adapter.ProductAdapter;
import com.e.laxmibrand.user.adapter.RecentlyViewedAdapter;
import com.e.laxmibrand.beans.Category;
import com.e.laxmibrand.beans.DiscountedProducts;
import com.e.laxmibrand.beans.RecentlyViewed;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static com.e.laxmibrand.R.drawable.*;

public class UserHome extends Fragment implements BaseSliderView.OnSliderClickListener{
    TextView txtView3,txtView4;
    SliderLayout sliderShow;
    ConstraintLayout cl;
    String promotionalBanner;
    ArrayList<Products> productItemList;
    static ArrayList<Var> varItemList;
    Context context;
    ArrayList<String> sliderImages;
    RecyclerView discountRecyclerView, categoryRecyclerView, recentlyViewedRecycler;
    DiscountedProductAdapter discountedProductAdapter;
     CategoryAdapter categoryAdapter;
     List<Category> categoryList;
     EditText searchET;
     String query;
      TextView allCategory;
      JsonObject getProductParameters;
       ImageView advImg1;
         public View onCreateView(@NonNull LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
           View root = inflater.inflate(R.layout.activity_user_home, container, false);
           discountRecyclerView = root.findViewById(R.id.discountedRecycler);
            categoryRecyclerView = root.findViewById(R.id.categoryRecycler);
            allCategory = root.findViewById(R.id.allCategoryLink);
            sliderShow = root.findViewById(R.id.slider);
             sliderShow.setCustomIndicator((PagerIndicator) root.findViewById(R.id.custom_indicator));
             advImg1 = root.findViewById(R.id.advImg1);
            searchET = root.findViewById(R.id.editTextSearch);
            cl = root.findViewById(R.id.cl1);
            txtView3 = root.findViewById(R.id.textView3);
            txtView4 = root.findViewById(R.id.textView4);
             context=getActivity();
            sliderImages = new ArrayList<String>();
             View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
             searchET.setOnFocusChangeListener(ofcListener);
            getMarketingSlider();
       //     getPromotionalBanner();



        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();

            }
        });

        searchET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Intent i = new Intent(getActivity(), SearchActivity.class);
                    i.putExtra("search_text", query);
                    startActivity(i);
                    return true;
                }
                return false;
            }
        });

            allCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainPage2) getActivity()).openFragment(new UserCategory(), -1);
                }
            });

           getAllCategoryDetails();
           productItemList =new ArrayList<Products>();
           getProductParameters =new JsonObject();
           getProductParameters.addProperty("popularity",1);
           getProductParameters.addProperty("price",0);
           getProductParameters.addProperty("sort",1);
           getProductParameters.addProperty("category_id",9);
           getDiscountedProducts(getProductParameters);

           root.findViewById(R.id.cl1).setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   if(searchET.hasFocus()) {
                       InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                   }
                   return true;

               }
           });



            // adding data to model
            categoryList = new ArrayList<>();


         //   setDiscountedRecycler(discountedProductsList);
            return root;
        }

        private void setDiscountedRecycler(ArrayList<Products> dataList) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            discountRecyclerView.setLayoutManager(layoutManager);
            discountedProductAdapter = new DiscountedProductAdapter(getActivity(),dataList);
            discountRecyclerView.setAdapter(discountedProductAdapter);
        }
        private void setCategoryRecycler(List<Category> categoryDataList) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            categoryRecyclerView.setLayoutManager(layoutManager);
            categoryAdapter = new CategoryAdapter(getActivity(),categoryDataList,"h");
            categoryRecyclerView.setAdapter(categoryAdapter);
        }
        //Now again we need to create a adapter and model class for recently viewed items.
        // lets do it fast.

    @Override
    public void onSliderClick(BaseSliderView slider) {


    }


    private void getAllCategoryDetails() {

        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getAllCategory();
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
                                String name = obj.getString("category_name");
                                Category cat = new Category();
                                cat.setCatName(name);
                                cat.setCatImage(obj.getString("image"));

                                cat.setId(obj.getString("category_id"));
                                categoryList.add(cat);

                            }

                            if(categoryList.size()>0) {
                                txtView4.setVisibility(View.VISIBLE);
                                categoryRecyclerView.setVisibility(View.VISIBLE);
                                setCategoryRecycler(categoryList);
                            }
                            else
                            {
                                txtView4.setVisibility(GONE);
                                categoryRecyclerView.setVisibility(GONE);
                            }
                        }catch(JSONException je){
                          //  Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                       // Toast.makeText(context, "other than 200", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

//                    Utility.somethingWentWrong(context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.dismissDialog(dialog);
              //  Utility.somethingWentWrong(context);
            }
        });

    }


    private void getDiscountedProducts(JsonObject getProductParameters) {
        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getUserProductList(getProductParameters);
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

                            int k=0;
                            for (int i = 0; i < jA.length(); i++) {
                                varItemList = new ArrayList<Var>();

                                JSONObject obj = jA.getJSONObject(i);
                                JSONObject objP = obj.getJSONObject("product");

                                Products product = new Products();
                                product.setCategory_id(objP.getString("category_id"));
                                //   if(selCategoryId.equals( obj.getString("category_id"))) {
                                product.setPdt_id(objP.getString("pdt_id"));
                                product.setPdt_name(objP.getString("pdt_name"));
                                product.setPdt_about(objP.getString("pdt_about"));
                                product.setPrdt_images(objP.getString("prdt_images"));
                                product.setIs_active(objP.getString("is_active"));
                                JSONArray jVar = obj.getJSONArray("varient");
                                for(int j=0;j<jVar.length();j++)
                                {
                                    JSONObject objVar = jVar.getJSONObject(j);
                                    Var v = new Var();
                                    v.setVarType(objVar.getString("var_type"));
                                    v.setVarIsActive(objVar.getString("is_active"));
                                    v.setVarDisAmt(objVar.getString("var_discount_price"));
                                    varItemList.add(v);
                                }
                                product.setVarientItems(varItemList);
                               productItemList.add(product);
                                // }
                            }

                            if(productItemList.size()>0) {
                                txtView3.setVisibility(View.VISIBLE);
                                discountRecyclerView.setVisibility(View.VISIBLE);
                                setDiscountedRecycler(productItemList);
                            }
                            else
                            {
                                txtView3.setVisibility(GONE);

                                discountRecyclerView.setVisibility(GONE);

                            }

                        }catch(JSONException je){

                        }

                    }
                    else {
                        //.makeText(context, "other than 200", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

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

    public void getMarketingSlider() {
        if (Utility.isOnline(getActivity())) {

            final Dialog dialog = Utility.showProgress(context);
            Call<ResponseBody> get = Utility.retroInterface().getMarketingImages();
            get.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Utility.dismissDialog(dialog);
                    if (response.code() == 200) {
                        try {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            JSONObject object = new JSONObject(s);
                            JSONArray jA = object.getJSONArray("data");
                            for (int j = 0; j < jA.length(); j++) {
                                JSONObject objVar = jA.getJSONObject(j);
                                sliderImages.add(objVar.getString("image_path"));

                            }

                            if (sliderImages.size() > 0) {
                                sliderShow.setVisibility(View.VISIBLE);

                                for (int i = 0; i < sliderImages.size(); i++) {
                                    DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                                    // initialize a SliderLayout
                                    defaultSliderView
                                            .image(sliderImages.get(i))
                                            .setOnSliderClickListener(UserHome.this);

                                    sliderShow.addSlider(defaultSliderView);
                                }

                                sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            } else {
                                sliderShow.setVisibility(GONE);
                            }
                            getPromotionalBanner();

                        } catch (Exception e) {
                            Toast.makeText(context, "Marketing eror" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Utility.somethingWentWrong(context);
                        }


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Utility.dismissDialog(dialog);
                   //t.makeText(context, "Get Whatsapp Link Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    Utility.somethingWentWrong(context);
                }
            });
        }
        else
        {
            Utility.noInternetError(getActivity());
        }

    }


    public void getPromotionalBanner() {
             if(Utility.isOnline(getActivity())) {
                 final Dialog dialog = Utility.showProgress(context);
                 Call<ResponseBody> get = Utility.retroInterface().getPromotionalImages();
                 get.enqueue(new Callback<ResponseBody>() {
                     @Override
                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                         Utility.dismissDialog(dialog);
                         try {
                             if (response.code() == 200) {
                                 try {
                                     ResponseBody responseBody = response.body();
                                     String s = responseBody.string();
                                     JSONObject object = new JSONObject(s);
                                     JSONArray jA = object.getJSONArray("data");
                                     for (int j = 0; j < jA.length(); j++) {
                                         JSONObject objVar = jA.getJSONObject(j);
                                         promotionalBanner = objVar.getString("image_path");

                                     }
                                     JSONObject objVar2 = jA.getJSONObject(0);
                                     promotionalBanner = objVar2.getString("image_path");

                                     try {
                                         if (!promotionalBanner.equals("")) {// && (promotionalBanner.contains("https")||promotionalBanner.contains("http"))) {
                                             advImg1.setVisibility(View.VISIBLE);

                                             Glide.with(context)
                                                     .load(promotionalBanner)
                                                     .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                     .placeholder(R.drawable.watermark_icon)
                                                     .error(R.drawable.watermark_icon)
                                                     .into(advImg1);
                                         } else {
                                             advImg1.setVisibility(GONE);
                                            /* Glide.with(context)
                                                     .load(R.drawable.watermark_icon)
                                                     .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                     .placeholder(R.drawable.watermark_icon)
                                                     .error(R.drawable.watermark_icon)
                                                     .into(advImg1);*/
                                         }


                                     } catch (Exception e) {
                                         Log.e("exc", e.getMessage());
                                     }
                                 } catch (JSONException je) {

                                 }
                             }
                         } catch (Exception e) {
                             Toast.makeText(context, "Promotional Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

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
             else
             {
                 Utility.noInternetError(getActivity());
                 Glide.with(context)
                         .load(R.drawable.watermark_icon)
                         .diskCacheStrategy(DiskCacheStrategy.NONE)
                         .placeholder(R.drawable.watermark_icon)
                         .error(R.drawable.watermark_icon)
                         .into(advImg1);
             }

    }



}

 class MyFocusChangeListener implements View.OnFocusChangeListener {

     public void onFocusChange(View v, boolean hasFocus) {

         if (v.getId() == R.id.editTextSearch && !hasFocus) {

             InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
             imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

         } else {

         }
     }




 }








