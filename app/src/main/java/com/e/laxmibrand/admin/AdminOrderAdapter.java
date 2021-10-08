package com.e.laxmibrand.admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
import com.app.sradesign.model.response.GetAllOrderDetailResponse;
import com.app.sradesign.utils.Utility;
*/

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.Orders;

import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyViewHolder> implements Filterable {
    Context context;
 // ArrayList<GetAllOrderDetailResponse.ResponseData> dataList, dataListFiltered;
   ArrayList<Orders> dataList, dataListFiltered;

    TextView noDataTV;
 //   public AdminOrderAdapter(Context context, ArrayList<GetAllOrderDetailResponse.ResponseData> dataList, TextView noDataTV) {
 public AdminOrderAdapter(Context context, ArrayList<Orders> dataList) {
     this.context = context;
        this.dataList = dataList;
        this.dataListFiltered = dataList;
        this.noDataTV = noDataTV;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.admin_order_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        try {

            viewHolder.dateTV.setText(dataList.get(i).getOrderDate());
            viewHolder.orderNoTV.setText(dataList.get(i).getOrderID());
  /*          String name;
            if (dataListFiltered.get(i).getFirmName() != null &&
                    dataListFiltered.get(i).getFirmName().length() > 0) {
                name = dataListFiltered.get(i).getFirmName()+"/"+dataListFiltered.get(i).getFname()+" "+dataListFiltered.get(i).getLname() +" - " + dataListFiltered.get(i).getCity() ;
            } else {
                name = dataListFiltered.get(i).getFname();
            }

            String date = dataListFiltered.get(i).getOrderDate().split("T")[0];
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd"),
                    stringFormat = new SimpleDateFormat("dd/MM/yyyy");
  */

            /*viewHolder.orderNoTV.setText(name + " ordered on " + stringFormat.format(apiFormat.parse(date)) + " with Order No as SR" +
                    dataListFiltered.get(i).getOrderID());
*/

//            viewHolder.orderNoTV.setText("Order No. : " + dataListFiltered.get(i).getOrderNo());
//            viewHolder.firmTV.setText("Firm : " + dataListFiltered.get(i).getFirmName());
//            viewHolder.dateTV.setText("Ordered on : " + dataListFiltered.get(i).getDate());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                     Intent orderDetailActivity = new Intent(context,AdminOrderDetailsActivity.class);
                                   orderDetailActivity.putExtra("orderid",dataList.get(i).getOrderID());
                                     context.startActivity(orderDetailActivity);
//                    context.startActivity(new Intent(context, AdminOrderDetailsActivity.class));

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

   /* @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
//                    ArrayList<GetAllOrderDetailResponse.ResponseData> filteredList = new ArrayList<>();
                    ArrayList<Orders> filteredList = new ArrayList<>();

                    *//*for (GetAllOrderDetailResponse.ResponseData row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getFirmName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getFname().toLowerCase().contains(charString.toLowerCase()) ||
                                String.valueOf("SR"+row.getOrderID()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }
*//*
                    dataListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
             //   dataListFiltered = (ArrayList<GetAllOrderDetailResponse.ResponseData>) filterResults.values;
                dataListFiltered = (ArrayList<Orders>) filterResults.values;

                if (dataListFiltered != null && dataListFiltered.size() > 0) {
                    noDataTV.setVisibility(View.GONE);
                } else {
                    noDataTV.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }*/


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderNoTV, firmTV, dateTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNoTV = itemView.findViewById(R.id.orderNoTV);
            firmTV = itemView.findViewById(R.id.firmTV);
            dateTV = itemView.findViewById(R.id.dateTV);
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


}
