package com.e.laxmibrand.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.MainActivity;

public class OrderPlacedScreen extends Fragment {
    LinearLayout orderPlacedLayout;
    TextView orderIdText;
    String orderid;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.place_order, container, false);
    orderPlacedLayout = root.findViewById(R.id.orderPlaceLayout);
    orderIdText = root.findViewById(R.id.orderNoText);
    orderid=getArguments().getString("orderid","1");
    orderIdText.setText(Html.fromHtml("<u>Order No : " + orderid+"</u>"));
    orderPlacedLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent orderDetailActivity = new Intent(getActivity(),UserOrderDetails.class);
            orderDetailActivity.putExtra("orderid",orderid);
            getActivity().startActivity(orderDetailActivity);
        }
    });
    return root;
    }


}
