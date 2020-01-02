package staddlevendor.com.staddlevendor.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;


import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.ServiceListAdapter;
import staddlevendor.com.staddlevendor.bean.ServiceListModule;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class ServicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ServiceListModule> serviceListModuleArrayList = new ArrayList<>();
    ServiceListAdapter adapter;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        AppConstants.ChangeStatusBarColor(ServicesActivity.this);

        find_All_Ids();

        setUpRecyclerView();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(ServicesActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager1);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
    }

    private void setUpRecyclerView() {

        ServiceListModule serviceListModule = new ServiceListModule();

        for (int i = 0; i < 2; i++) {
            serviceListModule.setService_name("Service Name :");
            serviceListModule.setTotal_amount("Total Amount :");
            serviceListModule.setOffer("Offer :");
            serviceListModuleArrayList.add(serviceListModule);
        }

        adapter = new ServiceListAdapter(ServicesActivity.this, serviceListModuleArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void find_All_Ids() {
        recyclerView = findViewById(R.id.recyclerView_service_list);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
