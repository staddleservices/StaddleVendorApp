package staddlevendor.com.staddlevendor.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.goodiebag.pinview.Pinview;
import com.google.gson.JsonElement;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.OrderMenuAdapter;
import staddlevendor.com.staddlevendor.bean.MySingleton;
import staddlevendor.com.staddlevendor.bean.OrderParsedListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.retrofitApi.EndApi;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

import static staddlevendor.com.staddlevendor.retrofitApi.EndApi.VERIFY_ORDERS;

public class CompletedOrderActivity extends AppCompatActivity {
    RecyclerView rvShopping;
    ImageView iv_back;
    TextView order_id_action,create_date,name_customer,contact_info,date_booking,booking_time,booking_address,tv_item_total,txt_percentage,txt_percentagename,txt_overallTotalprice,promocode,promocutoff;
    private OrderMenuAdapter orderMenuAdapter;
    private ArrayList<OrderParsedListModel> orderList;
    LinearLayout promocode_layout;
    SlideToActView complete_order_btn;
    private ApiInterface apiInterface;
    private AlertDialog quantAlert;
    String user_contact;
    String user_id;
    RelativeLayout layout_Totalprice;
    RelativeLayout layout_VendorDeiscount;
    RelativeLayout layout_PromoCode;
    RelativeLayout layout_Topay;

    TextView layout_Totalprice_txt;
    TextView layout_VendorDeiscount_txt;
    TextView layout_PromoCode_txt;
    TextView layout_Topay_txt;
    TextView layout_Promo_name_txt;
    TextView layout_vendor_discount_per_txt;
    TextView layout_topay_amount_methods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_order_view_activity);
        orderList = new ArrayList<>();
        setUpView();

        try {


            orderList = getIntent().getParcelableArrayListExtra("DATALIST");
            Log.e("DATALIST",orderList.get(0).getMenu_name());

//            if (getIntent().getStringExtra("TAG").equals("Accept"))
//                btn_complete.setVisibility(View.VISIBLE);
//            else
//                btn_complete.setVisibility(View.GONE);

            orderList = getIntent().getParcelableArrayListExtra("DATALIST");
            Log.e("DATALIST",orderList.get(0).getMenu_name());

            if(getIntent().getStringExtra("DISCOUNT_PRICE").equals("0") || getIntent().getStringExtra("DISCOUNT_PRICE").equals("0.0")){
                layout_VendorDeiscount.setVisibility(View.GONE);

            }else{
                layout_VendorDeiscount.setVisibility(View.VISIBLE);
                layout_VendorDeiscount_txt.setText("₹ "+getIntent().getStringExtra("DISCOUNT_PRICE"));
                layout_vendor_discount_per_txt.setText(getIntent().getStringExtra("DISCOUNT")+" % Off");
            }
            if(getIntent().getStringExtra("PROMONAME").equals("")){
                layout_PromoCode.setVisibility(View.GONE);
            }else{
                layout_PromoCode.setVisibility(View.VISIBLE);
                layout_PromoCode_txt.setText("₹ "+getIntent().getStringExtra("PROMOCUTOFF"));
                layout_Promo_name_txt.setText(getIntent().getStringExtra("PROMONAME"));
            }
            if(getIntent().getStringExtra("PAYMENT").equals("cash")){
                layout_topay_amount_methods.setText("Due ");
            }else{
                layout_topay_amount_methods.setText("Paid Online");
            }
            layout_Totalprice_txt.setText("₹ "+getIntent().getStringExtra("TOTAL_PRICE"));
            layout_Topay_txt.setText("₹ "+getIntent().getStringExtra("TOTAL"));
            name_customer.setText( getIntent().getStringExtra("NAME"));
            contact_info.setText("Contact : " + getIntent().getStringExtra("CONTACT")+" | "+ getIntent().getStringExtra("CONTACT_EMAIL"));
            user_contact=getIntent().getStringExtra("CONTACT");
            booking_address.setText("Address : " + getIntent().getStringExtra("ADDRESS"));
            create_date.setText(getIntent().getStringExtra("CREATE_DATE"));
            date_booking.setText("DATE : "+getIntent().getStringExtra("DATE"));
            booking_time.setText("Time : "+getIntent().getStringExtra("TIME"));
            //Toast.makeText(this, getIntent().getStringExtra("DISCOUNT"), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, getIntent().getStringExtra("TOTAL"), Toast.LENGTH_LONG).show();
            order_id_action.setText("#"+getIntent().getStringExtra("ORDER_ID"));
            user_id=getIntent().getStringExtra("UID");



//            txt_percentage.setText(getIntent().getStringExtra("DISCOUNT"));
//            txt_overallTotalprice.setText(getIntent().getStringExtra("TOTAL"));
//            if(getIntent().getStringExtra("PROMONAME")!=null){
//                promocode_layout.setVisibility(View.VISIBLE);
//                promocode.setText(getIntent().getStringExtra("PROMONAME"));
//                promocutoff.setText(getIntent().getStringExtra("PROMOCUTOFF"));
//            }




            orderMenuAdapter = new OrderMenuAdapter(CompletedOrderActivity.this, orderList);
            rvShopping.setAdapter(orderMenuAdapter);
            orderMenuAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



    }





    private void setUpView() {
        rvShopping = findViewById(R.id.rvShopping);
        iv_back = findViewById(R.id.iv_back);
        order_id_action = findViewById(R.id.order_id_action);
        create_date = findViewById(R.id.create_date);
        name_customer = findViewById(R.id.name_customer);
        contact_info = findViewById(R.id.contact_info);
        date_booking = findViewById(R.id.date_booking);
        booking_time = findViewById(R.id.booking_time);
        booking_address = findViewById(R.id.booking_address);
//        tv_item_total = findViewById(R.id.tv_item_total);
//        txt_percentagename = findViewById(R.id.txt_percentagename);
//        txt_overallTotalprice = findViewById(R.id.txt_overallTotalprice);
//        promocode = findViewById(R.id.promoname);
//        promocutoff = findViewById(R.id.promo_cutoff);
//        promocode_layout = findViewById(R.id.promocode_layout);
        complete_order_btn = findViewById(R.id.complete_order_btn);
        layout_Totalprice = findViewById(R.id.layout_totalprice);
        layout_VendorDeiscount = findViewById(R.id.layout_vendorDiscount);
        layout_PromoCode = findViewById(R.id.layout_Promocode);
        layout_Topay = findViewById(R.id.layout_topay_amount);
        layout_Totalprice_txt = findViewById(R.id.layout_totalprice_txt);
        layout_VendorDeiscount_txt = findViewById(R.id.layout_vendorDiscount_txt);
        layout_PromoCode_txt = findViewById(R.id.layout_Promocode_txt);
        layout_Topay_txt = findViewById(R.id.layout_topay_amount_txt);
        layout_vendor_discount_per_txt = findViewById(R.id.layout_vendorDiscount_per_txt);
        layout_Promo_name_txt = findViewById(R.id.layout_Promocode_name_txt);
        layout_topay_amount_methods = findViewById(R.id.layout_topay_amount_methods);
        rvShopping.setHasFixedSize(true);
        rvShopping.setLayoutManager(new LinearLayoutManager(CompletedOrderActivity.this));
    }






}
