package com.e.laxmibrand.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.telephony.CellSignalStrength;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.Category;
import com.e.laxmibrand.user.ProductDetails;
import com.e.laxmibrand.user.UserProductList;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<Category> categoryList;
    String fromScreen;
    String cname;
    public CategoryAdapter(Context context, List<Category> categoryList,String fromScreen) {
        this.context = context;
        this.categoryList = categoryList;
        this.fromScreen=fromScreen;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_items, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
       // holder.categoryImage.setImageResource(R.drawable.newcat1);


        try {
            String images = categoryList.get(position).getCatImage();
            if (!images.equals("") && (images.contains("https")||images.contains("http"))) {
                //String[] imagesList = images.split(",");
                String S1 = images;
                if (S1.contains("https"))
                    S1 = S1.replace("https", "http");
                Log.i("image image :", S1);
                Glide.with(context)
                        .load(S1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(holder.categoryImage);
            }

            else {
                Glide.with(context)
                        .load(R.drawable.watermark_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(holder.categoryImage);

            }
        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }



        cname=categoryList.get(position).getCatName();
        cname =cname.replace("_"," ");
        holder.categoryTitle.setText(cname);

        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProductList.class);
                cname=categoryList.get(position).getCatName();
                if(cname.contains("_"))
                        cname =cname.replace("_"," ");

                intent.putExtra("selectedCategoryName",cname);
                intent.putExtra("selectedCategoryId",categoryList.get(position).getId());

                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        if(fromScreen.equals("c"))
        return categoryList.size();
        else {
            if(categoryList.size()>=9)
            return 9;
            else
                return categoryList.size();
        }
    }

    public  static class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryTitle;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.grid_item_image);
            categoryTitle = itemView.findViewById(R.id.grid_item_title);

        }
    }

}