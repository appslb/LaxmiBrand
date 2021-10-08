package com.e.laxmibrand.user.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.OrderItem;
import com.e.laxmibrand.beans.Products;
import com.e.laxmibrand.beans.Var;
import java.util.ArrayList;

public class UserOrderTitleListAdapter extends RecyclerView.Adapter<UserOrderTitleListAdapter.MyViewHolder>{
    Context context;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Var> varItemList;
    UserOrderItemListAdapter adminOrderDetailsItemsAdapter;
    ArrayList<Products> productList;
    String pname;
    int noOfPages;
    public UserOrderTitleListAdapter(Context context, ArrayList<OrderItem> orderItemList,    ArrayList<Products> productList)
    {
        this.context = context;
        noOfPages=1;
        this.orderItemList=orderItemList;
        this.productList =productList;
     //   varItemList = new ArrayList<Var>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.orderitem_row_list_title, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        viewHolder.ItemName.setText(orderItemList.get(i).getItemName());
        varItemList=new ArrayList<Var>();
            viewHolder.varItem.setLayoutManager(new LinearLayoutManager(context));
            adminOrderDetailsItemsAdapter = new UserOrderItemListAdapter(context,orderItemList.get(i).getOrder_varients());
            viewHolder.varItem.setAdapter(adminOrderDetailsItemsAdapter);
            adminOrderDetailsItemsAdapter.notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName;
        RecyclerView varItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
            varItem=itemView.findViewById(R.id.orderItemsVarList);

        }
    }




}



