package staddlevendor.com.staddlevendor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.WalletAdapter;
import staddlevendor.com.staddlevendor.bean.WalletModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class WalletAmtActivity extends AppCompatActivity {

    private TextView txtPrice, txtTotal;
    private RecyclerView recyclerView;
    private WalletAdapter walletAdapter;
    private ArrayList<WalletModel> walletModelArrayList;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_amt);

        AppConstants.ChangeStatusBarColor(this);

        tag = getIntent().getStringExtra("TAG");

        setUpView();

        if (CheckNetwork.isNetworkAvailable(this)) {
            getWallet(AppPreferences.loadPreferences(this, "USER_ID"));
        } else {
            Toast.makeText(this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpView() {
        walletModelArrayList = new ArrayList<>();
        txtPrice = findViewById(R.id.txtPrice);
        txtTotal = findViewById(R.id.txtTotal);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        walletAdapter = new WalletAdapter(WalletAmtActivity.this, walletModelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(walletAdapter);
        walletAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void getWallet(String vid) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call;
        if (tag.equals("Cod"))
            call = apiInterface.getCodAmounts(vid);
        else
            call = apiInterface.getOnlineAmounts(vid);

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
                            WalletModel walletModel = new WalletModel();
                            JSONObject jsonObject = info.getJSONObject(i);
                            float tot = Float.parseFloat(jsonObject.getString("order_price"));
                            float comm = Float.parseFloat(jsonObject.getString("discount"));
                            totalAmount += tot - comm;
                            walletModel.setUser_name(jsonObject.getString("user_name"));
                            walletModel.setDiscount(jsonObject.getString("discount"));
                            walletModel.setOrder_price(jsonObject.getString("order_price"));
                            walletModelArrayList.add(walletModel);
                        }
                        txtPrice.setText("Rs. " + totalAmount);
                        txtTotal.setText("Rs. " + totalAmount);
                        recyclerView.setAdapter(walletAdapter);
                        walletAdapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(WalletAmtActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(WalletAmtActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
