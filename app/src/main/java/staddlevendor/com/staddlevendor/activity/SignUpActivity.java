package staddlevendor.com.staddlevendor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout ll_btn_sign_up;
    EditText edt_name, edt_phone_number, edt_email_id, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AppConstants.ChangeStatusBarColor(SignUpActivity.this);

        find_All_IDs();
// ========================= AsteriskPasswordTransformationMethod ========================
        edt_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        ll_btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidation();
            }
        });
    }

    private void find_All_IDs() {
        ll_btn_sign_up = findViewById(R.id.ll_btn_sign_up);
        edt_name = findViewById(R.id.edt_name);
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_password = findViewById(R.id.edt_password);
    }

    private void doValidation() {
        String username = edt_name.getText().toString().trim();
        String mobile = edt_phone_number.getText().toString().trim();
        String email = edt_email_id.getText().toString().trim();
        String password = edt_password.getText().toString().trim();

        AppPreferences.savePreferences(SignUpActivity.this, "EMAIL", email);
        AppPreferences.savePreferences(SignUpActivity.this, "PASSWORD", password);
        AppPreferences.savePreferences(SignUpActivity.this, "MOBILE", mobile);

        if (username.equalsIgnoreCase("")) {
            edt_name.setError("Enter Name");
            edt_name.requestFocus();
        } else if (mobile.equalsIgnoreCase("")) {
            edt_phone_number.setError("Enter Mobile Number");
            edt_phone_number.requestFocus();
        } else if (mobile.length() < 10) {
            edt_phone_number.setError("Please Enter 10 Digit Mobile Number");
            edt_phone_number.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            edt_email_id.setError("Please Enter Email");
            edt_email_id.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email_id.getText().toString()).matches()) {
            edt_email_id.setError("Invalid Email !!");
            edt_email_id.requestFocus();
        } else if (password.equalsIgnoreCase("")) {
            edt_password.setError("Enter Password");
            edt_password.requestFocus();
        } else if (password.length() < 5) {
            edt_password.setError("Password is too short !! ");
            edt_password.requestFocus();
        } else {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

    }

    //=================== AsteriskPasswordTransformationMethod Class =====================
    private class AsteriskPasswordTransformationMethod  extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
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
