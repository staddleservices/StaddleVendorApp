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
import staddlevendor.com.staddlevendor.ResponseClasses.PendingListResponse;
import staddlevendor.com.staddlevendor.adapter.PendingListAdapter;
import staddlevendor.com.staddlevendor.adapter.RejectedListAdapter;
import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class RejectedListActivity extends AppCompatActivity {
    ArrayList<PendingListModel> pendingListModelArrayList;
    String vid;
    ImageView iv_back;
    ApiInterface apiInterface;
    RecyclerView rvRejectedList;
    RelativeLayout rl_no_offers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_list);
        AppConstants.ChangeStatusBarColor(RejectedListActivity.this);
        find_All_IDs();
        pendingListModelArrayList = new ArrayList<>();

        vid = AppPreferences.loadPreferences(RejectedListActivity.this, "USER_ID");

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
        if (CheckNetwork.isNetworkAvailable(RejectedListActivity.this)) {
            rejectedListDetails(vid);
        } else {
            Toast.makeText(RejectedListActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void find_All_IDs() {
        rvRejectedList = findViewById(R.id.rvRejectedList);
        rvRejectedList.setLayoutManager(new LinearLayoutManager(RejectedListActivity.this));
        iv_back = findViewById(R.id.iv_back);
        rl_no_offers = findViewById(R.id.rl_no_offers);
    }

    private void rejectedListDetails(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PendingListResponse> call = apiInterface.getRejectedList(vid);

        call.enqueue(new Callback<PendingListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PendingListResponse> call, @NonNull Response<PendingListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        pendingListModelArrayList.clear();
                        PendingListResponse pendingListResponse = response.body();
                        if (pendingListResponse != null) {
                            String status = pendingListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                pendingListModelArrayList = pendingListResponse.getInfo();

                                if (pendingListModelArrayList != null) {
                                    RejectedListAdapter rejectedListAdapter = new RejectedListAdapter(RejectedListActivity.this, pendingListModelArrayList,"Reject");
                                    rvRejectedList.setAdapter(rejectedListAdapter);
                                    rvRejectedList.setHasFixedSize(true);
                                    rejectedListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(RejectedListActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvRejectedList.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(RejectedListActivity.this, pendingListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rvRejectedList.setVisibility(View.GONE);
                                rl_no_offers.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(RejectedListActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PendingListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RejectedListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
