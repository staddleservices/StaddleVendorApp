package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class ForgotPasswordActivity extends AppCompatActivity {

    ProgressDialog pd;
    ApiInterface apiInterface;

    Button ll_btn_submit;
    EditText edt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        AppConstants.ChangeStatusBarColor(ForgotPasswordActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        find_All_IDs();

        ll_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doValidation();
            }
        });

    }

    private void find_All_IDs() {
        ll_btn_submit = findViewById(R.id.btn_proceed);
        edt_email = findViewById(R.id.edt_forgot_password);
    }

    private void doValidation() {
        String mobile = edt_email.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            edt_email.setError("Enter Mobile Number");
            edt_email.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(ForgotPasswordActivity.this)) {
                forgotPassword(mobile);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forgotPassword(String mobile) {
        pd = new ProgressDialog(ForgotPasswordActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<JsonElement> call = apiInterface.forgotPassword(mobile);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                pd.dismiss();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    String status = str_response.getString("status");
                    String message = str_response.getString("message");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(ForgotPasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                pd.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}