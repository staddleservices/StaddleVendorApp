package staddlevendor.com.staddlevendor.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class ContactUsActivity extends AppCompatActivity {

    public String postUrl = "http://staddle.in/mobileapp/api/wsGetcmsDetails.php?id=1";
    public WebView webView;
    ImageView iv_back;
    TextView txtEmail, txtContact1, txtContact2;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        AppConstants.ChangeStatusBarColor(ContactUsActivity.this);

        setUpView();

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(postUrl);
        webView.setHorizontalScrollBarEnabled(false);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Conact us.");
//                    intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                    intent.setData(Uri.parse("mailto:vendorsupport@staddle.in"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtContact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+91-8233936618"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtContact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+91-7709465211"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpView() {
        iv_back = findViewById(R.id.iv_back);
        webView = (WebView) findViewById(R.id.webView);
        txtEmail = findViewById(R.id.txtEmail);
        txtContact1 = findViewById(R.id.txtContact1);
        txtContact2 = findViewById(R.id.txtContact2);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
