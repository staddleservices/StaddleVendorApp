package staddlevendor.com.staddlevendor.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.Objects;

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

public class ChangePasswordActivity extends AppCompatActivity {
    LinearLayout ll_btn_submit;
    EditText edt_old_password, edt_new_password, edt_conform_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        AppConstants.ChangeStatusBarColor(ChangePasswordActivity.this);

        find_all_Ids();

        ll_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edt_old_password.getText().toString().trim())) {
                    edt_old_password.setError("Enter Password");
                    edt_old_password.requestFocus();
                } else if (edt_old_password.getText().toString().trim().length() < 5) {
                    edt_old_password.setError("Password is too short !! ");
                    edt_old_password.requestFocus();
                } else if (TextUtils.isEmpty(edt_new_password.getText().toString().trim())) {
                    edt_new_password.setError("Enter New Password");
                    edt_new_password.requestFocus();
                } else if (edt_new_password.getText().toString().trim().length() < 5) {
                    edt_new_password.setError("Password is too short !! ");
                    edt_new_password.requestFocus();
                } else if (TextUtils.isEmpty(edt_conform_password.getText().toString().trim())) {
                    edt_conform_password.setError("Enter Confirm Password");
                    edt_conform_password.requestFocus();
                } else if (edt_conform_password.getText().toString().trim().length() < 5) {
                    edt_conform_password.setError("Password is too short !! ");
                    edt_conform_password.requestFocus();
                } else if (!edt_new_password.getText().toString().trim().equals(edt_conform_password.getText().toString().trim())) {
                    edt_conform_password.setError("Please check new and confirm password");
                    edt_conform_password.requestFocus();
                } else {
                    if (CheckNetwork.isNetworkAvailable(ChangePasswordActivity.this)) {
                        updatePassword(AppPreferences.loadPreferences(ChangePasswordActivity.this, "USER_ID"),
                                edt_old_password.getText().toString().trim(), edt_new_password.getText().toString().trim());
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void find_all_Ids() {
        ll_btn_submit = findViewById(R.id.ll_btn_submit);
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_conform_password = findViewById(R.id.edt_conform_password);
    }

    private void updatePassword(String vid, String password, String newpassword) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.updatePassword(vid, password, newpassword);

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    if (object.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(ChangePasswordActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
