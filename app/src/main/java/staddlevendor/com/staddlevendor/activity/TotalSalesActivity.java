package staddlevendor.com.staddlevendor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class TotalSalesActivity extends AppCompatActivity {


    TextView tv_amt1, tv_amt2;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_sales);


        find_All_IDs();

        tv_amt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TotalSalesActivity.this, WalletAmtActivity.class);
                intent.putExtra("TAG", "Cod");
                startActivity(intent);
            }
        });

        tv_amt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TotalSalesActivity.this, WalletAmtActivity.class);
                intent.putExtra("TAG", "Online");
                startActivity(intent);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(TotalSalesActivity.this).onBackPressed();
            }
        });

        if (CheckNetwork.isNetworkAvailable(Objects.requireNonNull(TotalSalesActivity.this))) {
            getCodAmounts(AppPreferences.loadPreferences(TotalSalesActivity.this, "USER_ID"));
        } else {
            Toast.makeText(TotalSalesActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void find_All_IDs() {
        tv_amt1 = findViewById(R.id.tv_amt1);
        tv_amt2 = findViewById(R.id.tv_amt2);
        iv_back = findViewById(R.id.iv_back);
    }

    private void getCodAmounts(String vid) {
        final ProgressDialog pd = new ProgressDialog(TotalSalesActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.getCodAmounts(vid);

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    String status = object.getString("status");
                    if (status.equals("1")) {
                        JSONArray info = object.getJSONArray("info");
                        float totalAmount = 0;
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject jsonObject = info.getJSONObject(i);
                            float tot = Float.parseFloat(jsonObject.getString("order_price"));
                            float comm = Float.parseFloat(jsonObject.getString("discount"));
                            totalAmount += tot - comm;
                        }
                        tv_amt1.setText("Rs. " + totalAmount);
                    } else
                        tv_amt1.setText("Rs. " + 0);

                    getOnlineAmounts(AppPreferences.loadPreferences(Objects.requireNonNull(TotalSalesActivity.this), "USER_ID"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(TotalSalesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOnlineAmounts(String vid) {
        final ProgressDialog pd = new ProgressDialog(TotalSalesActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.getOnlineAmounts(vid);

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    String status = object.getString("status");
                    if (status.equals("1")) {
                        JSONArray info = object.getJSONArray("info");
                        float totalAmount = 0;
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject jsonObject = info.getJSONObject(i);
                            float tot = Float.parseFloat(jsonObject.getString("order_price"));
                            float comm = Float.parseFloat(jsonObject.getString("discount"));
                            totalAmount += tot - comm;
                        }
                        tv_amt2.setText("Rs. " + totalAmount);
                    } else
                        tv_amt2.setText("Rs. " + 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(TotalSalesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
