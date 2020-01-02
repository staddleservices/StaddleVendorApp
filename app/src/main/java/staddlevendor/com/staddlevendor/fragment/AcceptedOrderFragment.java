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
import staddlevendor.com.staddlevendor.ResponseClasses.PendingListResponse;
import staddlevendor.com.staddlevendor.adapter.AcceptedOrderAdapter;
import staddlevendor.com.staddlevendor.adapter.PendingListAdapter;
import staddlevendor.com.staddlevendor.bean.AcceptedListModel;
import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class AcceptedOrderFragment extends Fragment {

    ArrayList<AcceptedListModel> acceptedListModels;
    AcceptedOrderAdapter acceptedOrderAdapter;
    String vid;
    ImageView iv_back;
    ApiInterface apiInterface;
    RecyclerView rvPendingList;
    RelativeLayout rl_no_offers;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accept_order_fragment, container, false);
        find_All_IDs(view);

        vid = AppPreferences.loadPreferences(getContext(), "USER_ID");
        AcceptedListDetails(vid);
        return view;
    }

    private void find_All_IDs(View view) {
        acceptedListModels = new ArrayList<>();
        rvPendingList = view.findViewById(R.id.rvPendingListn);
        rvPendingList.setLayoutManager(new LinearLayoutManager(getContext()));
        iv_back = view.findViewById(R.id.iv_back);
        rl_no_offers = view.findViewById(R.id.rl_no_offers);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(getContext())) {
            AcceptedListDetails(vid);
        } else {
            Toast.makeText(getContext(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void AcceptedListDetails(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<AcceptedResponse> call = apiInterface.getAcceptedtList(vid);

        call.enqueue(new Callback<AcceptedResponse>() {
            @Override
            public void onResponse(@NonNull Call<AcceptedResponse> call, @NonNull Response<AcceptedResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        acceptedListModels.clear();
                        Log.e("PENDING_ORDERS",response.message());
                        AcceptedResponse  acceptedResponse = response.body();
                        if (acceptedResponse != null) {
                            String status = acceptedResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                acceptedListModels = acceptedResponse.getInfo();

                                if (acceptedListModels != null) {
                                    acceptedOrderAdapter = new AcceptedOrderAdapter(getContext(), acceptedListModels,"Pending");
                                    rvPendingList.setAdapter(acceptedOrderAdapter);
                                    rvPendingList.setHasFixedSize(true);
                                    acceptedOrderAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvPendingList.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(), acceptedResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                rvPendingList.setVisibility(View.GONE);
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
            public void onFailure(Call<AcceptedResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
