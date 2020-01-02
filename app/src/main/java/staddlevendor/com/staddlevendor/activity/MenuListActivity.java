package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.MenuListResponse;
import staddlevendor.com.staddlevendor.adapter.MenuListAdapter;
import staddlevendor.com.staddlevendor.bean.MenuListModule;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

import static android.graphics.Color.TRANSPARENT;
import static staddlevendor.com.staddlevendor.sheardPref.AppPreferences.PREFS_NAME;

public class MenuListActivity extends AppCompatActivity {
    RecyclerView rvPopUp;
    ImageView iv_back;
    ApiInterface apiInterface;
    private ArrayList<MenuListModule> menuListModuleArrayList;
    MenuListAdapter menuListAdapter;
    String vendorId = "";
    ProgressDialog progressDialog;
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        AppConstants.ChangeStatusBarColor(MenuListActivity.this);

        setUpView();

        vendorId = AppPreferences.loadPreferences(MenuListActivity.this, "USER_ID");

        if (CheckNetwork.isNetworkAvailable(MenuListActivity.this)) {
            getVendorMenuList(vendorId);
        } else {
            Toast.makeText(MenuListActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUpView() {
        menuListModuleArrayList = new ArrayList<>();
        rvPopUp = findViewById(R.id.rvPopUp);
        iv_back = findViewById(R.id.iv_back);
        setAdapter();
    }

    private void setAdapter() {
        rvPopUp.setLayoutManager(new LinearLayoutManager(MenuListActivity.this));
        rvPopUp.setHasFixedSize(true);
        menuListAdapter = new MenuListAdapter(MenuListActivity.this, menuListModuleArrayList);
        rvPopUp.setAdapter(menuListAdapter);
        menuListAdapter.notifyDataSetChanged();

        menuListAdapter.setOnItemClickListener(new MenuListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuListModule menuListModule, String isStatus) {
                doChangeStatus(isStatus, menuListModule.getId());
            }
        });
    }

    private void getVendorMenuList(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<MenuListResponse> call = apiInterface.getVendermenulist(vid);

        call.enqueue(new Callback<MenuListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MenuListResponse> call, @NonNull Response<MenuListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        menuListModuleArrayList.clear();
                        MenuListResponse menuListResponse = response.body();
                        if (menuListResponse != null) {
                            String status = menuListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                menuListModuleArrayList = menuListResponse.getInfo();
                                setAdapter();
                            } else {
                                Toast.makeText(MenuListActivity.this, menuListResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(MenuListActivity.this, "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MenuListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MenuListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doChangeStatus(String statuss, String id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait..."); // set message
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonElement> call = apiInterface.ChangeMenuStatus(statuss, id);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                progressDialog.dismiss();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    Log.e("" + TAG, "Response >>>>" + str_response);
                    String status = str_response.getString("status");
                    String message = str_response.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(MenuListActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                        getVendorMenuList(vendorId);
                    } else {
                        Toast.makeText(MenuListActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MenuListActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void editMenuDialog(final String vid, String name, String price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_menu, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();

        final EditText edt_menu_name = dialogView.findViewById(R.id.edt_menu_name);
        final EditText edt_price = dialogView.findViewById(R.id.edt_price);
        TextView txtCancel = dialogView.findViewById(R.id.txtCancel);
        TextView txtOk = dialogView.findViewById(R.id.txtOk);

        edt_menu_name.setText(name);
        edt_price.setText(price);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuName = edt_menu_name.getText().toString().trim();
                String price = edt_price.getText().toString().trim();

                if (menuName.equalsIgnoreCase("")) {
                    edt_menu_name.setError("Please Enter Service Name");
                    edt_menu_name.requestFocus();
                } else if (price.equalsIgnoreCase("")) {
                    edt_price.setError("Please Enter Price");
                    edt_price.requestFocus();
                } else {
                    if (CheckNetwork.isNetworkAvailable(MenuListActivity.this)) {
                        editMenu(vid, menuName, price);
                    } else {
                        Toast.makeText(MenuListActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
                    }
                }
                alertDialog.dismiss();
            }
        });
    }

    private void editMenu(String vid, String name, String price) {
        showProgressDialog();
        Call<JsonElement> call = apiInterface.editMenu(vid, name, price);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                hideProgressDialog();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    String status = str_response.getString("status");
                    String message = str_response.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(MenuListActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                        getVendorMenuList(vendorId);
                    } else {
                        Toast.makeText(MenuListActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                hideProgressDialog();
                Toast.makeText(MenuListActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteMenu(String vid) {
        showProgressDialog();
        Call<JsonElement> call = apiInterface.deleteMenu(vid);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                hideProgressDialog();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    String status = str_response.getString("status");
                    String message = str_response.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(MenuListActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                        getVendorMenuList(vendorId);
                    } else {
                        Toast.makeText(MenuListActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                hideProgressDialog();
                Toast.makeText(MenuListActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(MenuListActivity.this);
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
