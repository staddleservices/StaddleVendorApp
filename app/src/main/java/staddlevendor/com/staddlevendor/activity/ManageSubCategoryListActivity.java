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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorSubCategoryListResponse;
import staddlevendor.com.staddlevendor.adapter.VendorSubCategoryListAdapter;
import staddlevendor.com.staddlevendor.bean.GetVendorSubCategoryListModule;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class ManageSubCategoryListActivity extends AppCompatActivity {
    RecyclerView rvMangeSubCategory;
    ImageView iv_back;
    ProgressDialog progressDialog;
    private ArrayList<GetVendorSubCategoryListModule> getVendorSubCategoryListResponseArrayList;
    VendorSubCategoryListAdapter vendorSubCategoryListAdapter;
    String vendorId = "";
    ApiInterface apiInterface;
    String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subcategory_list);
        AppConstants.ChangeStatusBarColor(ManageSubCategoryListActivity.this);
        find_All_IDs();
        vendorId = AppPreferences.loadPreferences(ManageSubCategoryListActivity.this, "USER_ID");

        getVendorSubCategoryListResponseArrayList = new ArrayList<>();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (CheckNetwork.isNetworkAvailable(ManageSubCategoryListActivity.this)) {
            getVendorSubCategoryListApi(vendorId);
        } else {
            Toast.makeText(ManageSubCategoryListActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void find_All_IDs() {
        rvMangeSubCategory = findViewById(R.id.rvMangeSubCategory);
        iv_back = findViewById(R.id.iv_back);
        rvMangeSubCategory.setLayoutManager(new LinearLayoutManager(ManageSubCategoryListActivity.this));
    }
    private void getVendorSubCategoryListApi(final String vid) {
        showProgressDialog();
        Call<GetVendorSubCategoryListResponse> call = apiInterface.getVenderSubCatgoryList(vid);

        call.enqueue(new Callback<GetVendorSubCategoryListResponse>() {
            @Override
            public void onResponse(Call<GetVendorSubCategoryListResponse> call, Response<GetVendorSubCategoryListResponse> response) {
                hideProgressDialog();
                try {
                    String str_response = new Gson().toJson(response.body());
                    String serverCode = String.valueOf(response.code());
                    Log.e("" + TAG, "Response >>>>" + str_response);

                    if (response.isSuccessful()) {
                        GetVendorSubCategoryListResponse categoryListResponse = response.body();

                        if (categoryListResponse != null) {
                            String status = categoryListResponse.getStatus();
                            String message = categoryListResponse.getMessage();
                            if (status.equalsIgnoreCase("1")) {
                                getVendorSubCategoryListResponseArrayList = categoryListResponse.getInfo();

                                for (GetVendorSubCategoryListModule categoryListTreeModule : getVendorSubCategoryListResponseArrayList) {
                                    String id = categoryListTreeModule.getId();
                                    String categoryName = categoryListTreeModule.getName();
                                    String vid = categoryListTreeModule.getVid();
                                }
                                vendorSubCategoryListAdapter = new VendorSubCategoryListAdapter(ManageSubCategoryListActivity.this, getVendorSubCategoryListResponseArrayList,"isManageType");
                                rvMangeSubCategory.setAdapter(vendorSubCategoryListAdapter);
                                vendorSubCategoryListAdapter.notifyDataSetChanged();
                                Toast.makeText(ManageSubCategoryListActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                                rvMangeSubCategory.setVisibility(View.VISIBLE);

                                //====== Recycler view onClick ======
                              //  onClickRecycleViewSubTree();
                            } else {
                                rvMangeSubCategory.setVisibility(View.GONE);
                                Toast.makeText(ManageSubCategoryListActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ManageSubCategoryListActivity.this, "Response Getting Null !!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageSubCategoryListActivity.this, serverCode + "Server Error !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetVendorSubCategoryListResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ManageSubCategoryListActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @NonNull
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(ManageSubCategoryListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.show();
    }

    @NonNull
    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
