package staddlevendor.com.staddlevendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorInfoResponse;
import staddlevendor.com.staddlevendor.activity.AboutActivity;
import staddlevendor.com.staddlevendor.activity.AddMenuActivity;
import staddlevendor.com.staddlevendor.activity.AddOfferActivity;
import staddlevendor.com.staddlevendor.activity.AddVendorSubCategoryActivity;
import staddlevendor.com.staddlevendor.activity.ChangeImageActivity;
import staddlevendor.com.staddlevendor.activity.ChangePasswordActivity;
import staddlevendor.com.staddlevendor.activity.ContactUsActivity;
import staddlevendor.com.staddlevendor.activity.EnterPasswordActivity;
import staddlevendor.com.staddlevendor.activity.ManageSubCategoryListActivity;
import staddlevendor.com.staddlevendor.activity.MenuListActivity;
import staddlevendor.com.staddlevendor.activity.PendingListActivity;
import staddlevendor.com.staddlevendor.activity.PolicyActivity;

import staddlevendor.com.staddlevendor.bean.GetVendorInfoModel;
import staddlevendor.com.staddlevendor.fragment.AcceptedOrderFragment;
import staddlevendor.com.staddlevendor.fragment.CompleteOrderFragment;
import staddlevendor.com.staddlevendor.fragment.HomeFragment;
import staddlevendor.com.staddlevendor.activity.LoginActivity;
import staddlevendor.com.staddlevendor.fragment.MenuFragment;
import staddlevendor.com.staddlevendor.fragment.NewOrderAkaPendingFragment;
import staddlevendor.com.staddlevendor.fragment.NotificationFragment;
import staddlevendor.com.staddlevendor.activity.ProfileActivity;
import staddlevendor.com.staddlevendor.fragment.ProfileFragment;
import staddlevendor.com.staddlevendor.fragment.WalletFragment;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

import static staddlevendor.com.staddlevendor.sheardPref.AppPreferences.PREFS_NAME;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ImageView  iv_user_profile_pic;
    DrawerLayout drawer;
    ImageView open_nav_ic;

    FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView navigation;

    RelativeLayout  ll_nav_wallet,
            ll_nav_notification, ll_nav_contact_us,
            ll_nav_policy, ll_nav_about, ll_nav_logout, ll_nav_settings;

    String userName, vendorId,catId;
    TextView tv_user_name, tv_email, txt_shop_name,city_and_cate;

    ApiInterface apiInterface;
    String adharcard_no, gst_no, vendorName, vendorEmail, vendorMobile, post_picture, location;


    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppConstants.ChangeStatusBarColor(HomeActivity.this);

        userName = AppPreferences.loadPreferences(HomeActivity.this, "USER_NAME");
        vendorId = AppPreferences.loadPreferences(HomeActivity.this, "USER_ID");
        catId = AppPreferences.loadPreferences(HomeActivity.this, "USER_CID");


        find_All_IDs();
        open_nav_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (CheckNetwork.isNetworkAvailable(HomeActivity.this)) {
            getUserInfo(vendorId);
        } else {
            Toast.makeText(HomeActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

        if (userName.equalsIgnoreCase("")) {
            txt_shop_name.setText("Not mentioned");
        } else {
            txt_shop_name.setText(userName);
        }

        switch (catId){
            case "1":{
                city_and_cate.setText("Beauty Salon");

                break;
            }
            case "2":{
                city_and_cate.setText("House Keeping");
                break;
            }
            case "3":{
                city_and_cate.setText("Security");
                break;
            }
            case "4":{
                city_and_cate.setText("Spa");
                break;
            }


        }

        replaceFragment(new NewOrderAkaPendingFragment());

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.new_orders_frag);


        //ll_nav_wallet.setOnClickListener(this);
        //ll_nav_notification.setOnClickListener(this);
        ll_nav_settings.setOnClickListener(this);
        ll_nav_contact_us.setOnClickListener(this);
        ll_nav_about.setOnClickListener(this);
        ll_nav_policy.setOnClickListener(this);
        ll_nav_logout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            for (Fragment fragment : fragmentManager.getFragments()) {
                String fragmentName = fragment.getClass().getSimpleName();
                if (fragmentName.equals(HomeFragment.class.getSimpleName())) {
                    if (doubleBackToExitPressedOnce) {
                        finishAffinity();
                        return;
                    }
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click back again to exit !!", Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(mRunnable, 1000);
                } else if (fragmentName.equals(MenuFragment.class.getSimpleName())
                        || fragmentName.equals(NotificationFragment.class.getSimpleName())
                        || fragmentName.equals(WalletFragment.class.getSimpleName())) {
                    navigation.setSelectedItemId(R.id.new_orders_frag);
                    replaceFragment(new NewOrderAkaPendingFragment());
                }
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
      if (id == R.id.ll_nav_add_offer) {
            Intent intent = new Intent(HomeActivity.this, AddOfferActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }else if (id == R.id.ll_nav_settings) {
            Intent intent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }
//        else if (id == R.id.ll_nav_notification) {
//            navigation.setSelectedItemId(R.id.navigation_notification);
//            replaceFragment(new NotificationFragment());
//            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//        }
         else if (id == R.id.ll_nav_contact_us) {
            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        } else if (id == R.id.ll_nav_policy) {
            Intent intent = new Intent(HomeActivity.this, PolicyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        } else if (id == R.id.ll_nav_about) {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        } else if (id == R.id.ll_nav_logout) {
            logOut();
        } else if (id == R.id.iv_edit) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_log_out_box, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);
        alertDialog.getWindow().setGravity(Gravity.CENTER);

        TextView btn_not_now = dialogView.findViewById(R.id.btn_not_now);
        TextView btn_yes = dialogView.findViewById(R.id.btn_yes);

        btn_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();*/
                AppPreferences.savePreferences(HomeActivity.this, "LOGIN_STATUS","0");

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
                finish();

                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                alertDialog.dismiss();
            }
        });

    }

    private void find_All_IDs() {
        drawer = findViewById(R.id.drawer_layout);



        ll_nav_notification = findViewById(R.id.ll_nav_notification);
        ll_nav_settings = findViewById(R.id.ll_nav_settings);
        ll_nav_contact_us = findViewById(R.id.ll_nav_contact_us);
        ll_nav_about = findViewById(R.id.ll_nav_about);
        ll_nav_policy = findViewById(R.id.ll_nav_policy);
        ll_nav_logout = findViewById(R.id.ll_nav_logout);
        open_nav_ic = findViewById(R.id.open_nav_ic);

        tv_user_name = findViewById(R.id.tv_user_name);
        iv_user_profile_pic = findViewById(R.id.iv_user_profile_pic);
        txt_shop_name = findViewById(R.id.txt_shop_name);
        tv_email = findViewById(R.id.tv_email);
        city_and_cate = findViewById(R.id.city_and_category);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        try {
            switch (item.getItemId()) {
                case R.id.accepted_orders_frag:
                    //drawer.openDrawer(Gravity.START);
                    fragment = new AcceptedOrderFragment();
                    break;
                case R.id.delivered_orders_frag:
                    fragment = new CompleteOrderFragment();
                    break;
                case R.id.managment_vendor_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.new_orders_frag:
                    fragment = new NewOrderAkaPendingFragment();
                    break;
            }
            replaceFragment(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    private void getUserInfo(String vendorId) {
        Call<GetVendorInfoResponse> call = apiInterface.getVendorInfo(vendorId);

        call.enqueue(new Callback<GetVendorInfoResponse>() {
            @Override
            public void onResponse(Call<GetVendorInfoResponse> call, Response<GetVendorInfoResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        GetVendorInfoResponse getVendorInfoResponse = response.body();
                        if (getVendorInfoResponse != null) {
                            ArrayList<GetVendorInfoModel> vendorInfoModelArrayList = getVendorInfoResponse.getInfo();
                            String status = getVendorInfoResponse.getStatus();
                            if (status.equalsIgnoreCase("1")) {
                                for (GetVendorInfoModel vendorInfoModel : vendorInfoModelArrayList) {
                                    vendorName = vendorInfoModel.getVenderfname();
                                    vendorEmail = vendorInfoModel.getEmail();
                                    vendorMobile = vendorInfoModel.getMobile();
                                    post_picture = vendorInfoModel.getImage();
                                    adharcard_no = vendorInfoModel.getAdharcard_no();
                                    gst_no = vendorInfoModel.getGst_no();
                                    location = vendorInfoModel.getLocation();
                                }

                                tv_user_name.setText(vendorName);
                                tv_email.setText(vendorEmail);

                                if (post_picture.equalsIgnoreCase("") || post_picture.equalsIgnoreCase("null")) {
                                    Picasso.get()
                                            .load(R.drawable.user_profile)
                                            .into(iv_user_profile_pic);

                                } else if (post_picture.contains(".png")) {
                                    Picasso.get()
                                            .load(post_picture)
                                            .into(iv_user_profile_pic);

                                } else {
                                    Picasso.get()
                                            .load(post_picture)
                                            .into(iv_user_profile_pic);
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
                Toast.makeText(HomeActivity.this, "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
