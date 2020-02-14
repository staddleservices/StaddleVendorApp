package staddlevendor.com.staddlevendor.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.OrderMenuAdapter;
import staddlevendor.com.staddlevendor.bean.OrderParsedListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;

public class ViewOrderActivity extends AppCompatActivity {


    TextView name;
    TextView order_id_action;
    TextView name_customer;
    TextView create_date;
    TextView contact_info;
    TextView date_booking;
    TextView booking_time;
    TextView booking_address;

    RecyclerView rvShopping;

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

    private OrderMenuAdapter orderMenuAdapter;
    private ArrayList<OrderParsedListModel> orderList;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        init();
        orderList = new ArrayList<>();

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

        if(getIntent().getStringExtra("PAYMENT").equals("cash")){
            layout_topay_amount_methods.setText("Due ");
        }else{
            layout_topay_amount_methods.setText("Paid Online");
        }
        layout_Totalprice_txt.setText("₹ "+getIntent().getStringExtra("TOTAL_PRICE"));
        layout_Topay_txt.setText("₹ "+getIntent().getStringExtra("TOTAL"));
        order_id_action.setText("#"+getIntent().getStringExtra("ORDER_ID"));
        name_customer.setText(getIntent().getStringExtra("NAME"));
        create_date.setText(getIntent().getStringExtra("CREATE_DATE"));
        contact_info.setText("Contact :"+getIntent().getStringExtra("CONTACT")+" | "+getIntent().getStringExtra("CONTACT_EMAIL"));
        date_booking.setText("Date of Booking : "+getIntent().getStringExtra("DATE"));
        booking_time.setText("Timing : "+getIntent().getStringExtra("TIME"));
        booking_address.setText("Address : "+getIntent().getStringExtra("ADDRESS"));
        if(getIntent().getStringExtra("PROMOCUTOFF")==null){
            layout_PromoCode.setVisibility(View.GONE);
        }else{
            layout_PromoCode.setVisibility(View.VISIBLE);
            layout_PromoCode_txt.setText("₹ "+getIntent().getStringExtra("PROMOCUTOFF"));
            layout_Promo_name_txt.setText(getIntent().getStringExtra("PROMONAME"));
        }

        orderMenuAdapter = new OrderMenuAdapter(ViewOrderActivity.this, orderList);
        rvShopping.setAdapter(orderMenuAdapter);
        orderMenuAdapter.notifyDataSetChanged();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void init() {
        rvShopping =findViewById(R.id.rvShopping);
        order_id_action = findViewById(R.id.order_id_action);
        name_customer = findViewById(R.id.name_customer);
        create_date = findViewById(R.id.create_date);
        contact_info = findViewById(R.id.contact_info);
        iv_back = findViewById(R.id.iv_back);
        date_booking = findViewById(R.id.date_booking);
        booking_time = findViewById(R.id.booking_time);
        booking_address = findViewById(R.id.booking_address);
        rvShopping.setLayoutManager(new LinearLayoutManager(ViewOrderActivity.this));
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


    }
}
