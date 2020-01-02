package staddlevendor.com.staddlevendor.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.activity.OfferAcceptedActivity;
import staddlevendor.com.staddlevendor.activity.OfferCompletedListActivity;
import staddlevendor.com.staddlevendor.activity.OnGoingOfferActivity;
import staddlevendor.com.staddlevendor.activity.PendingListActivity;
import staddlevendor.com.staddlevendor.activity.ProductListActivity;
import staddlevendor.com.staddlevendor.activity.RejectedListActivity;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class HomeFragment extends Fragment {

    Context mContext;
    LinearLayout ll_organic_offers;
    RelativeLayout rlt_offer_rejected, rlt_offer_completed;
    TextView txtOpen, txtClose, tv_pending_list, tv_offer_accepted, tv_offer_rejected, tv_offer_completed, tv_offer_list;
    ImageView imgClose, imgOpen;
    String openTime = "", closeTime = "";

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_home, container, false);
        mContext = getActivity();

        setUpView(view);

        tv_offer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductListActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        tv_pending_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PendingListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        tv_offer_accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OfferAcceptedActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        rlt_offer_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RejectedListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        rlt_offer_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OfferCompletedListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        ll_organic_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OnGoingOfferActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        imgOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                openTime = getTime(hourOfDay + ":" + minute);
                                openTime = hourOfDay + ":" + minute;
                                if (CheckNetwork.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                                    updateTime(AppPreferences.loadPreferences(getActivity(), "USER_ID"), openTime, closeTime);
                                } else {
                                    Toast.makeText(getActivity(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                closeTime = getTime(hourOfDay + ":" + minute);
                                closeTime = hourOfDay + ":" + minute;
                                if (CheckNetwork.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                                    updateTime(AppPreferences.loadPreferences(getActivity(), "USER_ID"), openTime, closeTime);
                                } else {
                                    Toast.makeText(getActivity(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CheckNetwork.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            getVenderDetails(AppPreferences.loadPreferences(getActivity(), "USER_ID"));
        } else {
            Toast.makeText(getActivity(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getTime(String time) {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a");
        Date date1 = null;
        try {
            date1 = format.parse(format2.format(new Date()) + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format1.format(date1);
    }

    private void setUpView(View view) {
        imgOpen = view.findViewById(R.id.imgOpen);
        imgClose = view.findViewById(R.id.imgClose);
        txtOpen = view.findViewById(R.id.txtOpen);
        txtClose = view.findViewById(R.id.txtClose);
        ll_organic_offers = view.findViewById(R.id.ll_organic_offers);
        tv_offer_list = view.findViewById(R.id.tv_offer_list);
        tv_pending_list = view.findViewById(R.id.tv_pending_list);
        tv_offer_accepted = view.findViewById(R.id.tv_offer_accepted);
        tv_offer_completed = view.findViewById(R.id.tv_offer_completed);
        tv_offer_rejected = view.findViewById(R.id.tv_offer_rejected);
        rlt_offer_rejected = view.findViewById(R.id.rlt_offer_rejected);
        rlt_offer_completed = view.findViewById(R.id.rlt_offer_completed);
    }

    private void getVenderDetails(String vid) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.getVenderDetails(vid);

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    JSONArray info = object.getJSONArray("info");
                    openTime = info.getJSONObject(0).getString("opening_time");
                    closeTime = info.getJSONObject(0).getString("closing_time");
                    txtOpen.setText("Opening time : " + openTime);
                    txtClose.setText("Closing time : " + closeTime);
                    tv_offer_list.setText("Order History\n" + "(" + info.getJSONObject(0).getString("totalOrderCount") + ")");
                    tv_pending_list.setText("New Order\n" + "(" + info.getJSONObject(0).getString("totalOrderPendingCount") + ")");
                    tv_offer_accepted.setText("Accepted \n" + "(" + info.getJSONObject(0).getString("totalOrderAcceptCount") + ")");
                    tv_offer_rejected.setText("Rejected \n" + "(" + info.getJSONObject(0).getString("totalOrderRejectCount") + ")");
                    tv_offer_completed.setText("Completed \n" + "(" + info.getJSONObject(0).getString("totalOrderCompleteCount") + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTime(String vid, String openTime, String closeTime) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.updateTime(vid, openTime, closeTime);

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    if (object.getString("status").equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    getVenderDetails(AppPreferences.loadPreferences(Objects.requireNonNull(getActivity()), "USER_ID"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}