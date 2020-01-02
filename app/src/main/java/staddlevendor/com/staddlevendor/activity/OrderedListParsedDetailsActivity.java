package staddlevendor.com.staddlevendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.OrderedParsedListAdapter;
import staddlevendor.com.staddlevendor.bean.OrderParsedListModel;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class OrderedListParsedDetailsActivity extends AppCompatActivity {
    RecyclerView rvOrderList;
    ImageView iv_back;
    RelativeLayout rl_no_offers;
    OrderedParsedListAdapter orderedParsedListAdapter;
    ArrayList<OrderParsedListModel> orderInfoListArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_parsed_details);
        AppConstants.ChangeStatusBarColor(OrderedListParsedDetailsActivity.this);

        find_All_IDs();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {
            Intent intent = getIntent();
            orderInfoListArrayList = getIntent().getParcelableArrayListExtra("data");

            if (orderInfoListArrayList == null) {
                Toast.makeText(OrderedListParsedDetailsActivity.this, "Items not available", Toast.LENGTH_SHORT).show();
            } else {
                rvOrderList.setLayoutManager(new LinearLayoutManager(OrderedListParsedDetailsActivity.this));
                orderedParsedListAdapter = new OrderedParsedListAdapter(OrderedListParsedDetailsActivity.this, orderInfoListArrayList);
                rvOrderList.setAdapter(orderedParsedListAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void find_All_IDs() {
        rvOrderList = findViewById(R.id.rvOrderList);
        iv_back = findViewById(R.id.iv_back);
        rl_no_offers = findViewById(R.id.rl_no_offers);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
