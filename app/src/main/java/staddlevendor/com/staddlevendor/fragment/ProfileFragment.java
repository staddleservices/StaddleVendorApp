package staddlevendor.com.staddlevendor.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorInfoResponse;
import staddlevendor.com.staddlevendor.activity.AddMenuActivity;
import staddlevendor.com.staddlevendor.activity.AddPromoCodeActivity;
import staddlevendor.com.staddlevendor.activity.AddVendorSubCategoryActivity;
import staddlevendor.com.staddlevendor.activity.ChangeImageActivity;
import staddlevendor.com.staddlevendor.activity.ManageSubCategoryListActivity;
import staddlevendor.com.staddlevendor.activity.MenuListActivity;
import staddlevendor.com.staddlevendor.activity.ProfileActivity;
import staddlevendor.com.staddlevendor.activity.TotalSalesActivity;
import staddlevendor.com.staddlevendor.bean.GetVendorInfoModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

public class ProfileFragment extends Fragment {


    TextView vendor_business_name;
    TextView vendor_business_address;
    TextView vendor_mobile_number;
    TextView vendor_email;
    TextView vendor_business_opening_time;
    TextView vendor_business_closing_time;


    ImageView vendor_business_cover_picture;
    ImageView vendor_time_edit_opening;
    ImageView vendor_time_edit_closing;


    String vendor_business_name_str;
    String vendor_business_address_str;
    String vendor_mobile_number_str;
    String vendor_email_str;
    String vendor_id;
    String vendor_business_cover_picture_str;
    String vendor_adhar;
    String vendor_gst;
    String vendor_openingtime;
    String vendor_closingtime;


    ApiInterface apiInterface;
    private ProgressDialog pd;


    RelativeLayout manage_promo_code;
    RelativeLayout manage_images;
    RelativeLayout add_services;
    RelativeLayout services_list;
    RelativeLayout add_subcategory;
    RelativeLayout manage_subcetgory;
    RelativeLayout ttl_sales;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        AppConstants.ChangeStatusBarColor(getActivity());

        init(view);
        vendor_id = AppPreferences.loadPreferences(getContext(), "USER_ID");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (CheckNetwork.isNetworkAvailable(getContext())) {
            getUserInfo(vendor_id);
            getVenderDetails(vendor_id);
        } else {
            Toast.makeText(getContext(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

        manage_promo_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPromoCodeActivity.class);
                startActivity(intent);
            }
        });

        vendor_time_edit_opening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                openTime = getTime(hourOfDay + ":" + minute);
                                vendor_openingtime = hourOfDay + ":" + minute;
                                if (CheckNetwork.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                                    updateTime(AppPreferences.loadPreferences(getActivity(), "USER_ID"), vendor_openingtime, vendor_closingtime);
                                } else {
                                    Toast.makeText(getActivity(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        vendor_time_edit_closing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                closeTime = getTime(hourOfDay + ":" + minute);
                                vendor_closingtime = hourOfDay + ":" + minute;
                                if (CheckNetwork.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
                                    updateTime(AppPreferences.loadPreferences(getActivity(), "USER_ID"), vendor_openingtime, vendor_closingtime);
                                } else {
                                    Toast.makeText(getActivity(), "Check your internet connection !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        manage_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChangeImageActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        add_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMenuActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        services_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        add_subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddVendorSubCategoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        manage_subcetgory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ManageSubCategoryListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });

        ttl_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TotalSalesActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });


        return view;
    }

    private void init(View view) {

        vendor_business_address = view.findViewById(R.id.vendor_business_address);
        vendor_business_name = view.findViewById(R.id.vendor_business_name);
        vendor_email = view.findViewById(R.id.vendor_email_address);
        vendor_mobile_number = view.findViewById(R.id.vendor_mobile_number);
        vendor_business_cover_picture = view.findViewById(R.id.iv_profile);
        vendor_business_opening_time = view.findViewById(R.id.vendor_business_opening_time);
        vendor_business_closing_time = view.findViewById(R.id.vendor_business_closing_time);
        vendor_time_edit_opening = view.findViewById(R.id.vendor_timing_edit_opening);
        vendor_time_edit_closing = view.findViewById(R.id.vendor_timing_edit_closing);
        manage_promo_code = view.findViewById(R.id.manage_promo_code);
        manage_images = view.findViewById(R.id.manage_images);
        add_services = view.findViewById(R.id.add_services);
        services_list = view.findViewById(R.id.services_list);
        add_subcategory = view.findViewById(R.id.add_subcategory);
        manage_subcetgory = view.findViewById(R.id.manage_subcetgory);
        ttl_sales = view.findViewById(R.id.ttl_sales);



    }


    private void getUserInfo(String vendorId) {
        pd = new ProgressDialog(getContext());
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<GetVendorInfoResponse> call = apiInterface.getVendorInfo(vendorId);

        call.enqueue(new Callback<GetVendorInfoResponse>() {
            @Override
            public void onResponse(Call<GetVendorInfoResponse> call, Response<GetVendorInfoResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                // Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        GetVendorInfoResponse getVendorInfoResponse = response.body();

                        if (getVendorInfoResponse != null) {
                            ArrayList<GetVendorInfoModel> vendorInfoModelArrayList = getVendorInfoResponse.getInfo();

                            String status = getVendorInfoResponse.getStatus();
                            String message = getVendorInfoResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {

                                for (GetVendorInfoModel vendorInfoModel : vendorInfoModelArrayList) {
                                    // vendorId = vendorInfoModel.getVid();
                                    //vendor_business_name_str = vendorInfoModel.getVenderfname();
                                    vendor_email_str = vendorInfoModel.getEmail();
                                    vendor_mobile_number_str = vendorInfoModel.getMobile();
                                    vendor_business_cover_picture_str = vendorInfoModel.getImage();
                                    vendor_adhar = vendorInfoModel.getAdharcard_no();
                                    vendor_gst = vendorInfoModel.getGst_no();
                                    vendor_business_address_str = vendorInfoModel.getLocation();
                                }
                                AppPreferences.savePreferences(getContext(), "VENDOR_IMAGE", vendor_business_cover_picture_str);
                                //Toast.makeText(getContext(), vendor_business_name_str, Toast.LENGTH_LONG).show();
                                vendor_business_name.setText(AppPreferences.loadPreferences(getContext(), "USER_NAME"));
                                vendor_email.setText(vendor_email_str);
                                vendor_mobile_number.setText(vendor_mobile_number_str);
                                vendor_business_address.setText(vendor_business_address_str);
                                //edt_adhar_card.setText(adharcard_no);
                                //edt_gst_no.setText(gst_no);
                                if (vendor_business_cover_picture_str.equalsIgnoreCase("") || vendor_business_cover_picture_str.equalsIgnoreCase("null")) {
                                    Picasso.get()
                                            .load(R.drawable.user_profile)
                                            .error(R.drawable.user_profile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(vendor_business_cover_picture);

                                } else if (vendor_business_cover_picture_str.contains(".png")) {
                                    Picasso.get()
                                            .load(vendor_business_cover_picture_str)
                                            .error(R.drawable.user_profile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(vendor_business_cover_picture);

                                } else {
                                    Picasso.get()
                                            .load(vendor_business_cover_picture_str)
                                            .error(R.drawable.user_profile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(vendor_business_cover_picture);
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetVendorInfoResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getContext(), "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });

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
                    vendor_openingtime = info.getJSONObject(0).getString("opening_time");
                    vendor_closingtime = info.getJSONObject(0).getString("closing_time");
                    vendor_business_opening_time.setText("Opening time : " + vendor_openingtime);
                    vendor_business_closing_time.setText("Closing time : " + vendor_closingtime);
//                    tv_offer_list.setText("Order History\n" + "(" + info.getJSONObject(0).getString("totalOrderCount") + ")");
//                    tv_pending_list.setText("New Order\n" + "(" + info.getJSONObject(0).getString("totalOrderPendingCount") + ")");
//                    tv_offer_accepted.setText("Accepted \n" + "(" + info.getJSONObject(0).getString("totalOrderAcceptCount") + ")");
//                    tv_offer_rejected.setText("Rejected \n" + "(" + info.getJSONObject(0).getString("totalOrderRejectCount") + ")");
//                    tv_offer_completed.setText("Completed \n" + "(" + info.getJSONObject(0).getString("totalOrderCompleteCount") + ")");
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
