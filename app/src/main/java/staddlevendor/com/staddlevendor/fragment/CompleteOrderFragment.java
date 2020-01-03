package staddlevendor.com.staddlevendor.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.AcceptedResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.CompletedOrdersResponse;
import staddlevendor.com.staddlevendor.adapter.AcceptedOrderAdapter;
import staddlevendor.com.staddlevendor.adapter.CompletedOrderAdapters;
import staddlevendor.com.staddlevendor.bean.AcceptedListModel;
import staddlevendor.com.staddlevendor.bean.CompletedOrderList;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class CompleteOrderFragment extends Fragment {
    ArrayList<CompletedOrderList> completedOrderLists;
    CompletedOrderAdapters completedOrderAdapters;
    String vid;
    ImageView iv_back;
    ApiInterface apiInterface;
    RecyclerView completed_order_list;
    RelativeLayout rl_no_offers;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);
        find_All_IDs(view);

        vid = AppPreferences.loadPreferences(getContext(), "USER_ID");
        completed_orders(vid);
        return view;
    }

    private void find_All_IDs(View view) {
        completedOrderLists = new ArrayList<>();
        completed_order_list = view.findViewById(R.id.rv_completed_order);
        completed_order_list.setLayoutManager(new LinearLayoutManager(getContext()));
        iv_back = view.findViewById(R.id.iv_back);
        rl_no_offers = view.findViewById(R.id.rl_no_offers);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(getContext())) {
            completed_orders(vid);
        } else {
            Toast.makeText(getContext(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void completed_orders(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CompletedOrdersResponse> call = apiInterface.getCompletedList(vid);

        call.enqueue(new Callback<CompletedOrdersResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompletedOrdersResponse> call, @NonNull Response<CompletedOrdersResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        completedOrderLists.clear();
                        Log.e("PENDING_ORDERS",response.message());
                        CompletedOrdersResponse  completedOrdersResponse = response.body();
                        if (completedOrdersResponse != null) {
                            String status = completedOrdersResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                completedOrderLists = completedOrdersResponse.getInfo();

                                if (completedOrderLists != null) {
                                    completedOrderAdapters = new CompletedOrderAdapters(getContext(), completedOrderLists,"Pending");
                                    completed_order_list.setAdapter(completedOrderAdapters);
                                    completed_order_list.setHasFixedSize(true);
                                    completedOrderAdapters.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "null ", Toast.LENGTH_SHORT).show();
                                }
                                completed_order_list.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(), completedOrdersResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                completed_order_list.setVisibility(View.GONE);
                                rl_no_offers.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Response Fail !!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<CompletedOrdersResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
