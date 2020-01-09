package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class AddVendorSubCategoryActivity extends AppCompatActivity {
    Button rl_add_subcat;
    ApiInterface apiInterface;
    String TAG = getClass().getSimpleName();
    ProgressDialog progressDialog;
    String vendorId = "", subcatName = "";
    EditText edt_subcat_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor_subcat);
        AppConstants.ChangeStatusBarColor(AddVendorSubCategoryActivity.this);

        findAllIDs();
        vendorId = AppPreferences.loadPreferences(AddVendorSubCategoryActivity.this, "USER_ID");
        rl_add_subcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidation();
            }
        });

    }

    void findAllIDs() {
        rl_add_subcat = findViewById(R.id.rl_add_subcat);
        edt_subcat_name = findViewById(R.id.edt_subcat_name);
    }

    private void doValidation() {
        subcatName = edt_subcat_name.getText().toString().trim();

        if (subcatName.equalsIgnoreCase("")) {
            edt_subcat_name.setError("Please Enter SubCategory");
            edt_subcat_name.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(AddVendorSubCategoryActivity.this)) {
                addSubCat(vendorId, subcatName);
            } else {
                Toast.makeText(AddVendorSubCategoryActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //============================== Add Offer Call ===============================================
    private void addSubCat(String vid, String subCategory) {
        showProgressDialog();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonElement> call = apiInterface.addSubCategory(vid, subCategory);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                //  hideProgressDialog();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    Log.e("" + TAG, "Response >>>>" + str_response);

                    String status = str_response.getString("status");
                    String message = str_response.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(AddVendorSubCategoryActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddVendorSubCategoryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        Toast.makeText(AddVendorSubCategoryActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddVendorSubCategoryActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(AddVendorSubCategoryActivity.this);
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

