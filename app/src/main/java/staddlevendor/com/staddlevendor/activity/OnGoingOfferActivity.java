package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.OnGoingOfferListResponse;
import staddlevendor.com.staddlevendor.adapter.OnGoingOfferListAdapter;
import staddlevendor.com.staddlevendor.bean.OnGoingOfferListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class OnGoingOfferActivity extends AppCompatActivity {
    ArrayList<OnGoingOfferListModel> onGoingOfferListModelArrayList;
    String vid;
    ImageView iv_back;
    ApiInterface apiInterface;
    RecyclerView rvOnGoingList;
    RelativeLayout rl_no_offers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_offer);
        AppConstants.ChangeStatusBarColor(OnGoingOfferActivity.this);
        find_All_IDs();
        vid = AppPreferences.loadPreferences(OnGoingOfferActivity.this, "USER_ID");
        onGoingOfferListModelArrayList = new ArrayList<>();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void find_All_IDs() {
        rvOnGoingList = findViewById(R.id.rvOnGoingList);
        rvOnGoingList.setLayoutManager(new LinearLayoutManager(OnGoingOfferActivity.this));
        iv_back = findViewById(R.id.iv_back);
        rl_no_offers = findViewById(R.id.rl_no_offers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(OnGoingOfferActivity.this)) {
            onGoingOfferList(vid);
        } else {
            Toast.makeText(OnGoingOfferActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void onGoingOfferList(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<OnGoingOfferListResponse> call = apiInterface.getOnGoingOfferList(vid);

        call.enqueue(new Callback<OnGoingOfferListResponse>() {
            @Override
            public void onResponse(@NonNull Call<OnGoingOfferListResponse> call, @NonNull Response<OnGoingOfferListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        onGoingOfferListModelArrayList.clear();
                        OnGoingOfferListResponse onGoingOfferListResponse = response.body();
                        if (onGoingOfferListResponse != null) {
                            String status = onGoingOfferListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                onGoingOfferListModelArrayList = onGoingOfferListResponse.getInfo();

                                if (onGoingOfferListModelArrayList != null) {
                                    OnGoingOfferListAdapter onGoingOfferListAdapter = new OnGoingOfferListAdapter(OnGoingOfferActivity.this, onGoingOfferListModelArrayList);
                                    rvOnGoingList.setAdapter(onGoingOfferListAdapter);
                                    rvOnGoingList.setHasFixedSize(true);
                                    //     pendingListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(OnGoingOfferActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvOnGoingList.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(OnGoingOfferActivity.this, onGoingOfferListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rvOnGoingList.setVisibility(View.GONE);
                                rl_no_offers.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(OnGoingOfferActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<OnGoingOfferListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OnGoingOfferActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
