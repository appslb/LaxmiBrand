package com.e.laxmibrand.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.e.laxmibrand.R;
import com.e.laxmibrand.admin.category.CategoryList;
import com.e.laxmibrand.admin.product.ProductList;

public class MainActivity extends AppCompatActivity {
        ImageView orders,products,notifications,offers,category;
        CardView card1_order,card2_products,card3_category,card4_aboutus,card5_contactus,card6_faqs,card7_promo,card8_market;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orders= (ImageView) findViewById(R.id.order);
        category= (ImageView) findViewById(R.id.category);
        products = (ImageView) findViewById(R.id.product);
        card1_order = (CardView) findViewById(R.id.card1);
        card2_products = (CardView) findViewById(R.id.card2);
        card3_category = (CardView) findViewById(R.id.card3);
        card4_aboutus = (CardView) findViewById(R.id.card4);
        card5_contactus = (CardView) findViewById(R.id.card5);
        card6_faqs = (CardView) findViewById(R.id.card6);
        card7_promo = (CardView) findViewById(R.id.card7);
        card8_market = (CardView) findViewById(R.id.card8);

        card1_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminOrderFragment.class));
            }
        });

        card7_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminPromotional.class));
            }
        });

        card8_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminMarketing.class));
            }
        });

        card5_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
            }
        });

        card4_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminAboutUsActivity.class));
            }
        });

        card3_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryList.class));
            }
        });

        card6_faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminFAQs.class));
            }
        });
        card2_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProductList.class));

            }
        });
    }
}