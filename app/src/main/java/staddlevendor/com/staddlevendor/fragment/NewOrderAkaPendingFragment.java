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
import staddlevendor.com.staddlevendor.ResponseClasses.PendingListResponse;
import staddlevendor.com.staddlevendor.activity.PendingListActivity;
import staddlevendor.com.staddlevendor.adapter.PendingListAdapter;
import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class NewOrderAkaPendingFragment extends Fragment {

    ArrayList<PendingListModel> pendingListModelArrayList;
    PendingListAdapter pendingListAdapter;
    String vid;
    ImageView iv_back;
    ApiInterface apiInterface;
    RecyclerView rvPendingList;
    RelativeLayout rl_no_offers;
    public NewOrderAkaPendingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neworders, container, false);
        find_All_IDs(view);

        vid = AppPreferences.loadPreferences(getContext(), "USER_ID");
        pendingListDetails(vid);
        return view;
    }


    private void find_All_IDs(View view) {
        pendingListModelArrayList = new ArrayList<>();
        rvPendingList = view.findViewById(R.id.rvPendingListn);
        rvPendingList.setLayoutManager(new LinearLayoutManager(getContext()));
        iv_back = view.findViewById(R.id.iv_back);
        rl_no_offers = view.findViewById(R.id.rl_no_offers);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(getContext())) {
            pendingListDetails(vid);
        } else {
            Toast.makeText(getContext(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void pendingListDetails(String vid) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<PendingListResponse> call = apiInterface.getPendingList(vid);

        call.enqueue(new Callback<PendingListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PendingListResponse> call, @NonNull Response<PendingListResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        pendingListModelArrayList.clear();
                        Log.e("PENDING_ORDERS",response.message());
                        PendingListResponse pendingListResponse = response.body();
                        if (pendingListResponse != null) {
                            String status = pendingListResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                pendingListModelArrayList = pendingListResponse.getInfo();

                                if (pendingListModelArrayList != null) {
                                    pendingListAdapter = new PendingListAdapter(getContext(), pendingListModelArrayList,"Pending");
                                    rvPendingList.setAdapter(pendingListAdapter);
                                    rvPendingList.setHasFixedSize(true);
                                    pendingListAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getContext(), "null ", Toast.LENGTH_SHORT).show();
                                }
                                rvPendingList.setVisibility(View.VISIBLE);
                                rl_no_offers.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getContext(), pendingListResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<PendingListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
