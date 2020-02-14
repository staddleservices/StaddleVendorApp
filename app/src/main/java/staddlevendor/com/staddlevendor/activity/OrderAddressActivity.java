package staddlevendor.com.staddlevendor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.ncorti.slidetoact.SlideToActView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.OrderMenuAdapter;
import staddlevendor.com.staddlevendor.bean.OrderParsedListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class OrderAddressActivity extends AppCompatActivity {

    RecyclerView rvShopping;
    ImageView iv_back;
    TextView order_id_action,create_date,name_customer,contact_info,date_booking,booking_time,booking_address,tv_item_total,txt_percentage,txt_percentagename,txt_overallTotalprice,promocode,promocutoff;
    Button btn_complete;

    private OrderMenuAdapter orderMenuAdapter;
    private ArrayList<OrderParsedListModel> orderList;
    LinearLayout promocode_layout;
    SlideToActView confirm_order_btn ;
    private ApiInterface apiInterface;
    String user_number;

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



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);

        orderList = new ArrayList<>();
        setUpView();

        try {
            if (getIntent().getStringExtra("TAG").equals("Accept"))
                btn_complete.setVisibility(View.VISIBLE);
            else
                btn_complete.setVisibility(View.GONE);

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


            //tv_item_total.setText("₹ " + getIntent().getStringExtra("TOTAL_PRICE"));
            //txt_percentage.setText("₹ " + getIntent().getStringExtra("DISCOUNT_PRICE"));
            //txt_overallTotalprice.setText("₹ " + getIntent().getStringExtra("TOTAl"));
            name_customer.setText( getIntent().getStringExtra("NAME"));
            contact_info.setText("Contact : " + getIntent().getStringExtra("CONTACT")+" | "+ getIntent().getStringExtra("CONTACT_EMAIL"));
            user_number=getIntent().getStringExtra("CONTACT");
            booking_address.setText("Address : " + getIntent().getStringExtra("ADDRESS"));
            create_date.setText(getIntent().getStringExtra("CREATE_DATE"));
            date_booking.setText("DATE : "+getIntent().getStringExtra("DATE"));
            booking_time.setText("Time : "+getIntent().getStringExtra("TIME"));
            //Toast.makeText(this, getIntent().getStringExtra("DISCOUNT"), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, getIntent().getStringExtra("TOTAL"), Toast.LENGTH_LONG).show();
            order_id_action.setText("#"+getIntent().getStringExtra("ORDER_ID"));
            confirm_order_btn.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideToActView slideToActView) {
                    updateProductStatusAccepted(getIntent().getStringExtra("ID"),getIntent().getStringExtra("UID"));
                    Toast.makeText(OrderAddressActivity.this, "OrderAccepted", Toast.LENGTH_SHORT).show();
                }
            });


//            txt_percentage.setText(getIntent().getStringExtra("DISCOUNT"));
//            txt_overallTotalprice.setText(getIntent().getStringExtra("TOTAL"));
//            if(getIntent().getStringExtra("PROMONAME")!=null){
//                promocode_layout.setVisibility(View.VISIBLE);
//                promocode.setText(getIntent().getStringExtra("PROMONAME"));
//                promocutoff.setText(getIntent().getStringExtra("PROMOCUTOFF"));
//            }





            orderMenuAdapter = new OrderMenuAdapter(OrderAddressActivity.this, orderList);
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

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetwork.isNetworkAvailable(OrderAddressActivity.this)) {
                    //completeOrder(getIntent().getStringExtra("ID"));
                } else {
                    Toast.makeText(OrderAddressActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpView() {
        rvShopping = findViewById(R.id.rvShopping);
        btn_complete = findViewById(R.id.btn_complete);
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
        confirm_order_btn = findViewById(R.id.confirm_order_btn);
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
        rvShopping.setLayoutManager(new LinearLayoutManager(OrderAddressActivity.this));
        rvShopping.setHasFixedSize(true);
    }

//    private void completeOrder(String id) {
//        final ProgressDialog pd = new ProgressDialog(this);
//        pd.setCancelable(false);
//        pd.setMessage("Loading Please Wait...");
//        pd.show();
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<JsonElement> call = apiInterface.completeOrder(id,"","","");
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
//                pd.dismiss();
//                try {
//                    assert response.body() != null;
//                    JSONObject object = new JSONObject(response.body().toString());
//                    Toast.makeText(OrderAddressActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//                    btn_complete.setVisibility(View.GONE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
//                pd.dismiss();
//                Toast.makeText(OrderAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void updateProductStatusAccepted(String productId, String uid) {
        final ProgressDialog pd = new ProgressDialog(OrderAddressActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String vname = AppPreferences.loadPreferences(OrderAddressActivity.this, "USER_NAME");
        String VID = AppPreferences.loadPreferences(OrderAddressActivity.this, "USER_ID");
//        Log.e("INFO",vname);
//        Log.e("INFO",VID);
//        Log.e("INFO",uid);
//        Log.e("INFO",productId);
        Call<JsonElement> call = apiInterface.updateProductSatusAccepted(productId,vname,VID,uid);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull final Response<JsonElement> response) {
                pd.dismiss();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    String status = str_response.getString("status");
                    String message = str_response.getString("message");
                    Log.e("ORDER_STATUS",message+" | "+status);
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(OrderAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
