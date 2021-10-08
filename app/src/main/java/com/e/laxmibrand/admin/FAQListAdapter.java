package com.e.laxmibrand.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.beans.AdminCategory;
import com.e.laxmibrand.beans.AdminFAQ;

import java.util.ArrayList;

public class FAQListAdapter extends RecyclerView.Adapter<FAQListAdapter.FAQItemViewHolder> {

    Context context;
    ArrayList<AdminFAQ> faqList;
    public FAQListAdapter(Context context,     ArrayList<AdminFAQ> faqList) {
        this.context = context;
        this.faqList = faqList;

    }
    @NonNull
    @Override
    public FAQItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.faq_list_item, viewGroup, false);
        return new FAQItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final FAQItemViewHolder viewHolder, final int i) {

        viewHolder.faqQues.setText(faqList.get(i).getFaqQues());
        viewHolder.faqAns.setText(faqList.get(i).getFaqAns());

    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }


    public class FAQItemViewHolder extends RecyclerView.ViewHolder {
       TextView faqAns,faqQues;

        public FAQItemViewHolder(@NonNull View itemView) {
            super(itemView);
            faqAns = itemView.findViewById(R.id.faqAns);
            faqQues = itemView.findViewById(R.id.faqQues);
        }


    }

}
