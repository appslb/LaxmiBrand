package com.e.laxmibrand.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*import com.app.sradesign.R;
import com.app.sradesign.fragment.admin.AdminMarketingFragment;*/

//import com.bumptech.glide.Glid
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.AdminMarketing;
import com.e.laxmibrand.admin.AdminPromotional;
import com.e.laxmibrand.user.MainPage2;
import com.e.laxmibrand.user.MyCart;
import com.e.laxmibrand.user.ProductDetails;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ProductDetailImageAdapter extends RecyclerView.Adapter<ProductDetailImageAdapter.MyViewHolder> {

    Context context,mContext;
    String[] imagesList;
Bitmap mBitmap;
    View itemView;

    public ProductDetailImageAdapter(Context context,  String[] imagesList) {
        this.context = context;
        this.imagesList = imagesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.multiple_images_item, viewGroup, false);
        this.mContext = viewGroup.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {

        String S1 = imagesList[i];
      /*  if (S1.contains("http"))
            S1 = S1.replace("http", "https");*/

        /*Glide.with(mContext)
                .load(S1)
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .dontTransform()
                .into(viewHolder.imgView);
*/

      /*  RequestOptions myOptions = new RequestOptions()
                .override(100, 100);

        Glide.with(mContext)
                .asBitmap()
                .apply(myOptions)
                .load(S1)
                .into(viewHolder.imgView);*/


      /*  RequestOptions myOptions = new RequestOptions()
                .centerCrop();

        Glide.with(mContext)
                .asBitmap()
                .apply(myOptions)
                .load(imagesList[i])
                .into(viewHolder.imgView);*/


        RequestOptions myOptions = new RequestOptions()
                .fitCenter() // or centerCrop
                .override(80, 80);

        Glide.with(mContext)
                .asBitmap()
                .apply(myOptions)
                .load(imagesList[i])
                .into(viewHolder.imgView);

       /* viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("on click :", imagesList[i]);
                ProductDetails activity = (ProductDetails) context;
                activity.loadImage(imagesList[i]);
            }
        });
*/
    }


    @Override
    public int getItemCount() {
        return imagesList.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView = itemView.findViewById(R.id.imgView);
        }
    }

}
