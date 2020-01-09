package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.LoginResponse;
import staddlevendor.com.staddlevendor.bean.LoginInfoModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class EnterPasswordActivity extends AppCompatActivity {


    ImageView back_btn;
    TextView showMob_number;
    EditText edt_pswd;
    Button proceed_btn;
    String mobile_number="";
    TextView forgot_password_text;

    ProgressDialog pd;
    String TAG = getClass().getSimpleName();
    ApiInterface apiInterface;//********
    String cId, fName, lName, userEmail, userMobile, userName, id,subCatName,commission,gender;
    String DEVICE_TOKEN, device_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        init();
        AppConstants.ChangeStatusBarColor(EnterPasswordActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        DEVICE_TOKEN = AppPreferences.loadPreferences(EnterPasswordActivity.this, "DEVICE_TOKEN");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String pswd = edt_pswd.getText().toString();
                 if(pswd.equals("") || pswd.equals(null)){

                 }else{
                     doValidation(mobile_number,pswd,DEVICE_TOKEN);
                 }
            }
        });

        forgot_password_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EnterPasswordActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        mobile_number = intent.getStringExtra("mobile_number");
        back_btn = findViewById(R.id.bakbtnpswd);
        showMob_number = findViewById(R.id.mobile_show_pswd);
        edt_pswd = findViewById(R.id.pswdedttxt);
        proceed_btn = findViewById(R.id.btn_proceed);
        showMob_number.setText(mobile_number);
        forgot_password_text = findViewById(R.id.forgot_password_text);

    }

    private void doValidation(String mobile_number, String pswd, String DEVICE_TOKEN) {


        AppPreferences.savePreferences(EnterPasswordActivity.this, "MOBILE", mobile_number);
        AppPreferences.savePreferences(EnterPasswordActivity.this, "PASSWORD", pswd);

       doLogin(mobile_number,pswd,DEVICE_TOKEN,"A");
    }

    private void doLogin(String mobile_number, String password, String device_id, String device_type) {
        pd = new ProgressDialog(EnterPasswordActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        Call<LoginResponse> call = apiInterface.UserLogin(mobile_number, password,device_id,device_type);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();

                        if (loginResponse != null) {

                            String status = loginResponse.getStatus();
                            String message = loginResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {
                                LoginInfoModel loginInfoModel = response.body().getInfo();

                                cId = loginInfoModel.getCid();
                                fName = loginInfoModel.getFname();
                                lName = loginInfoModel.getLname();
                                userEmail = loginInfoModel.getEmail();
                                userMobile = loginInfoModel.getMobile();
                                id = loginInfoModel.getId();
                                subCatName = loginInfoModel.getSubcatName();
                                gender = loginInfoModel.getGender();
                                commission = loginInfoModel.getCommission();
                                id = loginInfoModel.getId();

                                userName = fName + " " + lName;

                                AppPreferences.savePreferences(EnterPasswordActivity.this, "LOGIN_STATUS", "1");
                                AppPreferences.savePreferences(EnterPasswordActivity.this, "USER_NAME", userName);
                                AppPreferences.savePreferences(EnterPasswordActivity.this, "USER_CID", cId);
                                AppPreferences.savePreferences(EnterPasswordActivity.this, "SUBCAT_NAME", subCatName);
                                AppPreferences.savePreferences(EnterPasswordActivity.this, "GENDER", gender);
                                AppPreferences.savePreferences(EnterPasswordActivity.this, "USER_ID", id);
                                AppPreferences.savePreferences(EnterPasswordActivity.this, "USER_EMAIL", userEmail);

                                Toast.makeText(EnterPasswordActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EnterPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                finish();

                            } else if (status.equalsIgnoreCase("2")) {
                                Toast.makeText(EnterPasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EnterPasswordActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog_active_user_login, null);
                                builder.setView(dialogView);
                                builder.setCancelable(true);

                                final AlertDialog alertDialog1 = builder.create();
                                alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog1.show();

                                TextView btn_ok1 = dialogView.findViewById(R.id.btn_ok1);
                                TextView tv_msg_body = dialogView.findViewById(R.id.tv_msg_body);
                                tv_msg_body.setText(message);

                                btn_ok1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog1.dismiss();
                                    }
                                });
                            }
                        }
                    } else {
                        Toast.makeText(EnterPasswordActivity.this, response.code() + "Server Code Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(EnterPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}
