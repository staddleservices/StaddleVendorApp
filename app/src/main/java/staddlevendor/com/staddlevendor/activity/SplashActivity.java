package staddlevendor.com.staddlevendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppConstants.ChangeStatusBarColor(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String login_status = AppPreferences.loadPreferences(SplashActivity.this, "LOGIN_STATUS");
                if (login_status.equalsIgnoreCase("1")) {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, TermAndConditionActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                    finish();
                }
            }
        }, 3000);

    }
}

