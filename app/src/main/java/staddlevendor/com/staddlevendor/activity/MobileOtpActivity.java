package staddlevendor.com.staddlevendor.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import staddlevendor.com.staddlevendor.R;


public class MobileOtpActivity extends AppCompatActivity {


    TextView showMobileNumber;
    String mobileNumber;
    Button proceedBtnWithOtp;
    ImageButton backArrowBtn;
    TextView timerText;
    CountDownTimer t;
    Pinview otpview;
    private boolean timerFinished;
    ProgressDialog progressDialog;

    //firebase
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    public static String VERIFICATION_TAG="false";

    //forword to home activity
    String username;
    String uid;
    String email;
    String profilepic;
    public String singupStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);
        init();
        Intent intent=getIntent();
        mobileNumber=intent.getStringExtra("mobile_number");
        String fmob="+91"+mobileNumber;
        sendVerificationCode(fmob);
        showMobileNumber.setText("+91"+mobileNumber);
        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        proceedBtnWithOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpview.getPinLength()==0){
                    Toast.makeText(getApplicationContext(),"Enter your OTP",Toast.LENGTH_LONG).show();



                }else {

                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    //Without this user can hide loader by tapping outside screen
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();


                    verifyVerificationCode(otpview.getValue());

                }
            }
        });



        //Timer Otp
        t = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long remainedSecs = millisUntilFinished / 1000;
                timerText.setText("" + (remainedSecs / 60) + ":" + (remainedSecs % 60));// manage it accordign to you
            }

            public void onFinish() {
                timerFinished=true;
                timerText.setText("Resend OTP");
                timerText.setClickable(true);
                //Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_SHORT).show();

                cancel();
            }
        }.start();
    }
    private void init(){
        showMobileNumber=findViewById(R.id.mobilenumbertext);
        backArrowBtn=findViewById(R.id.backbtnotp);
        proceedBtnWithOtp=findViewById(R.id.verifybtnotp);
        mAuth = FirebaseAuth.getInstance();
        timerText=findViewById(R.id.timerText);
        timerText.setClickable(false);
        otpview=findViewById(R.id.pinview);
        progressDialog=new ProgressDialog(MobileOtpActivity.this);
    }


    //Firebase mobile otp verification

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpview.setValue(code);
                verifyVerificationCode(code);
                //verifying the code

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MobileOtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("FirebaseException",e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MobileOtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Verified!",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            //verification successful we will start the profile activity
                            FirebaseUser user = task.getResult().getUser();
                            VERIFICATION_TAG="true";
                            Intent intent = new Intent(MobileOtpActivity.this,EnterPasswordActivity.class);
                            intent.putExtra("mobile_number",mobileNumber);
                            startActivity(intent);
                            finish();

                            //checkuserdb(mobileNumber);

                            //checkuserdb();


                        } else {
                            Toast.makeText(getApplicationContext(),"Verified!",Toast.LENGTH_LONG).show();


                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                        }
                    }
                });
    }


}
