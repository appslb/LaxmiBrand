package com.e.laxmibrand.user.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/*
import com.app.sradesign.model.response.GetAllOrderDetailResponse;
import com.app.sradesign.utils.Utility;
*/

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.BaseResponse;
import com.e.laxmibrand.beans.Orders;
import com.e.laxmibrand.user.MainPage2;
import com.e.laxmibrand.user.UserOrderDetails;
import com.e.laxmibrand.utils.Utility;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.MyViewHolder> implements Filterable {
    Context context;
    // ArrayList<GetAllOrderDetailResponse.ResponseData> dataList, dataListFiltered;
    ArrayList<Orders> dataList, dataListFiltered;
    ArrayList<Integer> totdis;
        JsonObject orderid;
    public UserOrderAdapter(Context context, ArrayList<Orders> dataList, ArrayList<Integer> totdis) {
        this.context = context;
        this.dataList = dataList;
        this.dataListFiltered = dataList;
        this.totdis=totdis;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_order_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        try {
            viewHolder.dateTV.setText(dataList.get(i).getOrderDate());
            viewHolder.orderNoTV.setText("Order no - " + dataList.get(i).getOrderID() + ", ordered by - " +dataListFiltered.get(i).getPhoneNumber());
            viewHolder.orderStatus.setText("Order Status : " + dataList.get(i).getOrderStatus());
            int tot = Integer.parseInt(dataList.get(i).getTotalOrderItem());
            int totwithdel=0;
            int totwithdis=0;
            if(Integer.parseInt(dataList.get(i).getTotalOrderItem())<=500)
            {
                totwithdel = tot+50;
               // viewHolder.totalOrderAmount.setText("Total Amount : " + dataList.get(i).getTotalOrderItem());
            }
            else
            {
                totwithdel = tot;
              //  viewHolder.totalOrderAmount.setText("Total Amount : " + dataList.get(i).getTotalOrderItem());

            }
            if(Integer.parseInt(dataList.get(i).getDisAmt()) > 0)
                totwithdel = totwithdel-Integer.parseInt(dataList.get(i).getDisAmt());



            viewHolder.totalOrderAmount.setText("Total Amount : " + (totwithdel-totdis.get(i)));
            /*totwithdel=0;
            tot=0;
*/
            if(dataList.get(i).getOrderStatus().equalsIgnoreCase("cancelled"))
            {
                viewHolder.cancelOrderBtn.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.cancelOrderBtn.setVisibility(View.VISIBLE);
            }

            viewHolder.orderCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailActivity = new Intent(context,UserOrderDetails.class);
                    orderDetailActivity.putExtra("orderid",dataList.get(i).getOrderID());
                    context.startActivity(orderDetailActivity);
                }
            });

            viewHolder.cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderid=new JsonObject();
                    orderid.addProperty("order_id",dataList.get(i).getOrderID());
                    cancelUserOrder(orderid);
                }
            });

        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }
     }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

     public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName,dateTV,orderNoTV, orderStatus, totalOrderAmount;
        Button cancelOrderBtn;
        ImageView productImage;
        CardView orderCard;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
            orderNoTV = itemView.findViewById(R.id.orderid);
            dateTV = itemView.findViewById(R.id.orderdate);
            productName=itemView.findViewById(R.id.productName);
            orderStatus= itemView.findViewById(R.id.orderStatus);
            productImage=itemView.findViewById(R.id.productImage);
            totalOrderAmount= itemView.findViewById(R.id.totalAmount);
            orderCard = itemView.findViewById(R.id.orderCard);
            cancelOrderBtn = itemView.findViewById(R.id.cancelOrderBtn);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                   dataListFiltered = dataList;

                } else {
                    // filtered_salesmanDetailsList.clear();

                    ArrayList<Orders> filteredList = new ArrayList<Orders>();
                    for (Orders row : dataList) {
                        //change this to filter according to your case
                        if (row.getOrderDetail().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);

                        }
                    }
                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }


    private void cancelUserOrder(JsonObject userDetails) {
        final Dialog dialog = Utility.showProgress(context);
        Call<BaseResponse> addCategory = Utility.retroInterface().cancelOrder(userDetails);
        addCategory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context,"Order has been Cancelled",Toast.LENGTH_LONG).show();
                        }
                        else if(response.body().getStatus().equalsIgnoreCase("conflict"))
                        {
                            Toast.makeText(context,"Order cannot be cancelled.Time is elapsed with your order",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context,"Order cannot be cancelled.Time is elapsed with your order",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                }
            }

        @Override
        public void onFailure(Call<BaseResponse> call, Throwable t) {
            Utility.dismissDialog(dialog);
            Utility.somethingWentWrong(context);
        }
    });
}

}
