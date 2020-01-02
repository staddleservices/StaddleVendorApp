package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.ProductListResponse;
import staddlevendor.com.staddlevendor.adapter.PendingListAdapter;
import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.bean.ProductListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView rvProductList;
    RelativeLayout rl_no_offers;
    ImageView iv_back;
    ApiInterface apiInterface;
    String vid;
    ArrayList<PendingListModel> productListModelArrayList;
    PendingListAdapter pendingListAdapter;

    //action mode
    public boolean is_in_action_mode;
    Toolbar toolbar;
    TextView counter_text_view;
    ArrayList<ProductListModel> selectionList = new ArrayList<>();
    int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        AppConstants.ChangeStatusBarColor(ProductListActivity.this);
        find_All_IDs();
        productListModelArrayList = new ArrayList<>();

        vid = AppPreferences.loadPreferences(ProductListActivity.this, "USER_ID");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(ProductListActivity.this)) {
            productListDetails(vid);
        } else {
            Toast.makeText(ProductListActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void find_All_IDs() {
        rvProductList = findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
        iv_back = findViewById(R.id.iv_back);
        rl_no_offers = findViewById(R.id.rl_no_offers);
    }

    public void productListDetails(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ProductListResponse> call = apiInterface.getProductList(vid);

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        productListModelArrayList.clear();
                        ProductListResponse productListResponse = response.body();
                        if (productListResponse != null) {
                            String status = productListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                productListModelArrayList = productListResponse.getInfo();

                                if (productListModelArrayList != null) {
                                    pendingListAdapter = new PendingListAdapter(ProductListActivity.this, productListModelArrayList, "Order");
                                    rvProductList.setAdapter(pendingListAdapter);
                                    rvProductList.setHasFixedSize(true);
                                    pendingListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(ProductListActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvProductList.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ProductListActivity.this, productListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rvProductList.setVisibility(View.GONE);
                                rl_no_offers.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(ProductListActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProductListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
