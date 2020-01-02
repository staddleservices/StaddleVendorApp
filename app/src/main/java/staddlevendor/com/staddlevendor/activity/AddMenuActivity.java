package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.GetSubCategoryTreeListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorSubCategoryListResponse;
import staddlevendor.com.staddlevendor.adapter.SubCategoryTreeAdapter;
import staddlevendor.com.staddlevendor.adapter.VendorSubCategoryListAdapter;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryTreeListModule;
import staddlevendor.com.staddlevendor.bean.GetVendorSubCategoryListModule;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class AddMenuActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    EditText edt_menu_name, edt_price;
    RelativeLayout rl_add_menu;
    SwitchCompat btn_switch;
    String menuName = "", price = "", vendorId = "";
    ImageView iv_back;
    ProgressDialog progressDialog;
    String TAG = getClass().getSimpleName();
    String statuss = "D";
    //
    ImageView iv_sub_category_tree_arrow;
    RecyclerView rv_sub_category_tree;
    RelativeLayout rel_sub_categorytree;
    TextView tv_sub_category_tree;
    private ArrayList<GetVendorSubCategoryListModule> getVendorSubCategoryListResponseArrayList;
    boolean isCategoryVisbile = false;
    VendorSubCategoryListAdapter vendorSubCategoryListAdapter;
    String sid, categoryName, id, vid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);
        AppConstants.ChangeStatusBarColor(AddMenuActivity.this);
        find_all_Ids();

        vendorId = AppPreferences.loadPreferences(AddMenuActivity.this, "USER_ID");

        getVendorSubCategoryListResponseArrayList = new ArrayList<>();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (CheckNetwork.isNetworkAvailable(AddMenuActivity.this)) {
            getVendorSubCategoryListApi(vendorId);
        } else {
            Toast.makeText(AddMenuActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

        rl_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidation();
            }
        });

        rel_sub_categorytree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCategoryVisbile) {
                    isCategoryVisbile = true;
                    rv_sub_category_tree.setVisibility(View.VISIBLE);
                    iv_sub_category_tree_arrow.setImageResource(R.drawable.ic_vector_circle_down_arrow);
                } else {
                    isCategoryVisbile = false;
                    rv_sub_category_tree.setVisibility(View.GONE);
                    iv_sub_category_tree_arrow.setImageResource(R.drawable.ic_vector_circle_left_arrow);
                }
            }
        });
    }

    private void find_all_Ids() {
        edt_menu_name = findViewById(R.id.edt_menu_name);
        edt_price = findViewById(R.id.edt_price);
        iv_back = findViewById(R.id.iv_back);
        rl_add_menu = findViewById(R.id.rl_add_menu);
        btn_switch = findViewById(R.id.btn_switch);

        iv_sub_category_tree_arrow = findViewById(R.id.iv_sub_category_tree_arrow);
        rv_sub_category_tree = findViewById(R.id.rv_sub_category_tree);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(AddMenuActivity.this);
        rv_sub_category_tree.setLayoutManager(linearLayoutManager2);
        rv_sub_category_tree.setHasFixedSize(true);

        rel_sub_categorytree = findViewById(R.id.rel_sub_categorytree);
        tv_sub_category_tree = findViewById(R.id.tv_sub_category_tree);
    }

    private void doValidation() {
        menuName = edt_menu_name.getText().toString().trim();
        price = edt_price.getText().toString().trim();

        if (menuName.equalsIgnoreCase("")) {
            edt_menu_name.setError("Please Enter Service Name");
            edt_menu_name.requestFocus();
        } else if (price.equalsIgnoreCase("")) {
            edt_price.setError("Please Enter Price");
            edt_price.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(AddMenuActivity.this)) {
                doAddMenu(vendorId, menuName, price, id);
            } else {
                Toast.makeText(AddMenuActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onClickRecycleViewSubTree() {
        vendorSubCategoryListAdapter.setAdapterOnClick(new VendorSubCategoryListAdapter.ListRecycleDataInterface() {
            @Override
            public void recycleAdapterOnClick(GetVendorSubCategoryListModule spnIndSubCategoryListModule, int position) {
                categoryName = spnIndSubCategoryListModule.getName();
                id = spnIndSubCategoryListModule.getId();
                vid = spnIndSubCategoryListModule.getVid();
                tv_sub_category_tree.setText(categoryName);
            }
        });
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
                                vendorSubCategoryListAdapter = new VendorSubCategoryListAdapter(AddMenuActivity.this, getVendorSubCategoryListResponseArrayList,"");
                                rv_sub_category_tree.setAdapter(vendorSubCategoryListAdapter);
                                vendorSubCategoryListAdapter.notifyDataSetChanged();
                                Toast.makeText(AddMenuActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                                rel_sub_categorytree.setVisibility(View.VISIBLE);

                                //====== Recycler view onClick ======
                                onClickRecycleViewSubTree();
                            } else {
                                rel_sub_categorytree.setVisibility(View.GONE);
                                Toast.makeText(AddMenuActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AddMenuActivity.this, "Response Getting Null !!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddMenuActivity.this, serverCode + "Server Error !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetVendorSubCategoryListResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddMenuActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //============================== Add menu Call ===============================================
    private void doAddMenu(String vid, String name, String price, String sid) {
        showProgressDialog();
        Call<JsonElement> call = apiInterface.AddMenu(vid, name, price, sid);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                hideProgressDialog();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    Log.e("" + TAG, "Response >>>>" + str_response);

                    String status = str_response.getString("status");
                    String message = str_response.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(AddMenuActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMenuActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        Toast.makeText(AddMenuActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddMenuActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(AddMenuActivity.this);
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
