package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.PendingListResponse;
import staddlevendor.com.staddlevendor.adapter.PendingListAdapter;
import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class PendingListActivity extends AppCompatActivity {

    ArrayList<PendingListModel> pendingListModelArrayList;
    PendingListAdapter pendingListAdapter;
    String vid;
    ImageView iv_back;
    ApiInterface apiInterface;
    RecyclerView rvPendingList;
    RelativeLayout rl_no_offers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list);
        AppConstants.ChangeStatusBarColor(PendingListActivity.this);

        find_All_IDs();

        vid = AppPreferences.loadPreferences(PendingListActivity.this, "USER_ID");

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
        if (CheckNetwork.isNetworkAvailable(PendingListActivity.this)) {
            pendingListDetails(vid);
        } else {
            Toast.makeText(PendingListActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void find_All_IDs() {
        pendingListModelArrayList = new ArrayList<>();
        rvPendingList = findViewById(R.id.rvPendingList);
        rvPendingList.setLayoutManager(new LinearLayoutManager(PendingListActivity.this));
        iv_back = findViewById(R.id.iv_back);
        rl_no_offers = findViewById(R.id.rl_no_offers);
    }

    public void pendingListDetails(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PendingListResponse> call = apiInterface.getPendingList(vid);

        call.enqueue(new Callback<PendingListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PendingListResponse> call, @NonNull Response<PendingListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        pendingListModelArrayList.clear();
                        Log.e("PENDING_ORDERS",response.message());
                        PendingListResponse pendingListResponse = response.body();
                        if (pendingListResponse != null) {
                            String status = pendingListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                pendingListModelArrayList = pendingListResponse.getInfo();

                                if (pendingListModelArrayList != null) {
                                    pendingListAdapter = new PendingListAdapter(PendingListActivity.this, pendingListModelArrayList,"Pending");
                                    rvPendingList.setAdapter(pendingListAdapter);
                                    rvPendingList.setHasFixedSize(true);
                                    pendingListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(PendingListActivity.this, "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvPendingList.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(PendingListActivity.this, pendingListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rvPendingList.setVisibility(View.GONE);
                                rl_no_offers.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(PendingListActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PendingListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PendingListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
