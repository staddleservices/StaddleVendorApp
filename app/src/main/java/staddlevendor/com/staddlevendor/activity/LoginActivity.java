package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class LoginActivity extends AppCompatActivity {

    EditText mobileeditetxt;
    Button proceedbtnmobile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppConstants.ChangeStatusBarColor(LoginActivity.this);

        find_All_IDs();



        proceedbtnmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String  mobileNumber=mobileeditetxt.getText().toString();
                if(mobileNumber.length()==10){
                    makeIntent(mobileNumber);
                }else if(mobileNumber.length()==0){
                    Toast.makeText(getApplicationContext(),"Please enter a proper mobile number",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter a proper mobile number",Toast.LENGTH_LONG).show();
                }
            }
        });

//        tv_btn_forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//            }
//
//        });


    }

    private void find_All_IDs() {
        proceedbtnmobile = findViewById(R.id.proceedbtnmobile);
        mobileeditetxt = findViewById(R.id.mobileeditetxt);
    }
    private void makeIntent(String mobileNumber){
        Intent intent=new Intent(LoginActivity.this,MobileOtpActivity.class);
        intent.putExtra("mobile_number",mobileNumber);
        startActivity(intent);
    }


}
