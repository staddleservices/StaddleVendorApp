package staddlevendor.com.staddlevendor.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class PolicyActivity extends AppCompatActivity {

    ImageView iv_back;
    public String postUrl = "http://staddle.in/mobileapp/api/wsGetcmsDetails.php?id=2";
    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        AppConstants.ChangeStatusBarColor(PolicyActivity.this);

        find_all_Ids();

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



    }

    private void find_all_Ids() {
        iv_back = findViewById(R.id.iv_back);
        webView = (WebView) findViewById(R.id.webView);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}
