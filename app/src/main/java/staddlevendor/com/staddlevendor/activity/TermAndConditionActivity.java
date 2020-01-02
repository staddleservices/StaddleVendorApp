package staddlevendor.com.staddlevendor.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class TermAndConditionActivity extends AppCompatActivity {

    LinearLayout ll_continue;
    CheckBox checkbox;
    TextView tv_accept_terms;
    WebView web_term_condition;

    public String postUrl = "http://staddle.in/mobileapp/api/wsGetcmsDetails.php?id=4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        AppConstants.ChangeStatusBarColor(TermAndConditionActivity.this);

        find_All_Ids();

        web_term_condition.setWebViewClient(new WebViewClient());
        web_term_condition.getSettings().setJavaScriptEnabled(true);
        web_term_condition.loadUrl(postUrl);
        web_term_condition.setHorizontalScrollBarEnabled(false);

        tv_accept_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    checkbox.setChecked(false);
                } else {
                    checkbox.setChecked(true);
                }
            }
        });//==========================================================

        ll_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkbox.isChecked()) {
                    Toast.makeText(TermAndConditionActivity.this, "Select terms and condition", Toast.LENGTH_LONG).show();
                    checkbox.requestFocus();
                } else {
                    Intent intent = new Intent(TermAndConditionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                }
            }
        });

    }

    private void find_All_Ids() {
        ll_continue = findViewById(R.id.ll_continue);
        checkbox = findViewById(R.id.checkbox);
        tv_accept_terms = findViewById(R.id.tv_accept_terms);
         web_term_condition = findViewById(R.id.web_term_condition);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
