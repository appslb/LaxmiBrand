package com.e.laxmibrand.user;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ShareAppScreen extends Fragment {
        Button shareAppBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.share_screen, container, false);
       shareAppBtn = root.findViewById(R.id.shareAppBtn);

       shareAppBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i;
               String text = "Text To share";
               i = new Intent(Intent.ACTION_SEND);
               i.setType("text/plain");
               i.putExtra(Intent.EXTRA_TEXT, text);
               startActivity(Intent.createChooser(i, "Suggest to Friends"));
           }
       });
        return root;
    }




}
