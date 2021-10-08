package com.e.laxmibrand.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.Var;

import java.util.ArrayList;

public class UserOrderItemListAdapter extends RecyclerView.Adapter<UserOrderItemListAdapter.MyViewHolder>{
    Context context;
    ArrayList<Var> varItemList;

    public UserOrderItemListAdapter(Context context, ArrayList<Var> varItemList) {
        this.varItemList=varItemList;
        this.context = context;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_item_row_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        viewHolder.ItemWeight.setText(varItemList.get(i).getVarType());
        viewHolder.ItemRate.setText(varItemList.get(i).getVarActualAmt());
        viewHolder.ItemQTY.setText(varItemList.get(i).getVar_qty());
        viewHolder.ItemAmount.setText(String.valueOf(Integer.parseInt(varItemList.get(i).getVarActualAmt())*Integer.parseInt(varItemList.get(i).getVar_qty())));
        viewHolder.ItemDis.setText(String.valueOf(Integer.parseInt(varItemList.get(i).getVarDisAmt())*Integer.parseInt(varItemList.get(i).getVar_qty())));

    }


    @Override
    public int getItemCount() {
        return varItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName,ItemWeight,ItemRate,ItemQTY,ItemAmount,ItemDis;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
            ItemWeight = itemView.findViewById(R.id.ItemWeight);
            ItemRate = itemView.findViewById(R.id.ItemRate);
            ItemQTY = itemView.findViewById(R.id.ItemQty);
            ItemAmount = itemView.findViewById(R.id.ItemAmount);
            ItemDis = itemView.findViewById(R.id.distv);

        }
    }

}
