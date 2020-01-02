package staddlevendor.com.staddlevendor.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;

import staddlevendor.com.staddlevendor.ResponseClasses.AddOfferResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetVendorInfoResponse;
import staddlevendor.com.staddlevendor.bean.GetVendorInfoModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

import static android.widget.Toast.LENGTH_LONG;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout ll_btn_save;
    ImageView iv_back, iv_profile, iv_camera;
    EditText edt_name, edt_address, edt_phone, edt_email, edt_adhar_card, edt_gst_no;

    // =========== Upload image ================
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 2;
    Bitmap bitmap;
    Uri fileUri;
    Uri imageUrl;
    String photo, imageprofile;//===== End =======

    ProgressDialog pd;
    ApiInterface apiInterface;
    String vid, adharcard_no, gst_no, vendorName, vendorEmail, vendorMobile, post_picture, location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AppConstants.ChangeStatusBarColor(ProfileActivity.this);

        find_All_IDs();
        vid = AppPreferences.loadPreferences(ProfileActivity.this, "USER_ID");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (CheckNetwork.isNetworkAvailable(ProfileActivity.this)) {
            getUserInfo(vid);
        } else {
            Toast.makeText(ProfileActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }


        //========================= OnClick Listener =================================
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ll_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidation();

            }
        });
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();//====================upload photo method=================
            }
        });
    }// =============== End Of onCreate() ==============

    private void showPictureDialog() {
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ProfileActivity.this);
        pictureDialog.setTitle("Select Action");
        pictureDialog.setIcon(R.drawable.ic_camera);
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera", "Cancel"};
        pictureDialog.setCancelable(false);

        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takeFromGallery();
                        break;
                    case 1:
                        takeFromCamera();
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    private void takeFromCamera() {
        // Check if this device has a camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    // Open default camera
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);             // start the image capture Intent
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);        //100
        } else {
            // no camera on this device
            Toast.makeText(ProfileActivity.this, "Camera not supported", LENGTH_LONG).show();
        }
    }

    private void takeFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MY_GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == MY_GALLERY_REQUEST_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    convertToBase64(bitmap);
                    iv_profile.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == MY_CAMERA_REQUEST_CODE) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            convertToBase64(thumbnail);
            iv_profile.setImageBitmap(thumbnail);
        }//------------------ End ----------------------------------------

    }

    private void convertToBase64( Bitmap bitmap) {
        bitmap = compressImage(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        photo = Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }// ===========*** End Image Upload Data ***=============

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > 400) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        assert bitmap != null;
        return bitmap;
    }

    private void doValidation() {
        String username = edt_name.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String phone = edt_phone.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String adhar_no = edt_adhar_card.getText().toString().trim();
        String gst_no = edt_gst_no.getText().toString().trim();
        String vendor_image = photo;

        if (username.equalsIgnoreCase("")) {
            edt_name.setError("Enter Name");
            edt_name.requestFocus();
        } else if (address.equalsIgnoreCase("")) {
            edt_address.setError("Enter Address");
            edt_address.requestFocus();
        } else if (phone.equalsIgnoreCase("")) {
            edt_phone.setError("Enter Mobile Number");
            edt_phone.requestFocus();
        } else if (phone.length() < 10) {
            edt_phone.setError("Please Enter 10 Digit Mobile Number");
            edt_phone.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            edt_email.setError("Please Enter Email");
            edt_email.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString()).matches()) {
            edt_email.setError("Invalid Email !!");
            edt_email.requestFocus();
        } else if (adhar_no.equalsIgnoreCase("")) {
            edt_adhar_card.setError("Enter Adhar Number");
            edt_adhar_card.requestFocus();
        } else if (adhar_no.length() < 12) {
            edt_adhar_card.setError("Enter 12 Digit Adhar Number !! ");
            edt_adhar_card.requestFocus();
        } else if (gst_no.equalsIgnoreCase("")) {
            edt_gst_no.setError("Enter GST Number");
            edt_gst_no.requestFocus();
        } else if (gst_no.length() < 15) {
            edt_gst_no.setError("Enter 15 Digit GST Number !! ");
            edt_gst_no.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(ProfileActivity.this)) {
                vendorName=username;
                // ======== Api Call =============
                editUserInfo(vid, vendorName, vendorEmail, location, phone, adhar_no, gst_no, vendor_image);
            } else {
                Toast.makeText(ProfileActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void find_All_IDs() {
        ll_btn_save = findViewById(R.id.ll_btn_save);
        iv_back = findViewById(R.id.iv_back);
        edt_name = findViewById(R.id.edt_name);
        edt_address = findViewById(R.id.edt_address);
        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_adhar_card = findViewById(R.id.edt_adhar_card);
        edt_gst_no = findViewById(R.id.edt_gst_no);
        iv_profile = findViewById(R.id.iv_profile);
        iv_camera = findViewById(R.id.iv_camera);
    }

    private void getUserInfo(String vendorId) {
        pd = new ProgressDialog(ProfileActivity.this);
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
                                    vendorName = vendorInfoModel.getVenderfname();
                                    vendorEmail = vendorInfoModel.getEmail();
                                    vendorMobile = vendorInfoModel.getMobile();
                                    post_picture = vendorInfoModel.getImage();
                                    adharcard_no = vendorInfoModel.getAdharcard_no();
                                    gst_no = vendorInfoModel.getGst_no();
                                    location = vendorInfoModel.getLocation();
                                }
                                AppPreferences.savePreferences(ProfileActivity.this, "VENDOR_IMAGE", post_picture);
                                edt_name.setText(vendorName);
                                edt_email.setText(vendorEmail);
                                edt_phone.setText(vendorMobile);
                                edt_address.setText(location);
                                edt_adhar_card.setText(adharcard_no);
                                edt_gst_no.setText(gst_no);
                                if (post_picture.equalsIgnoreCase("") || post_picture.equalsIgnoreCase("null")) {
                                    Picasso.get()
                                            .load(R.drawable.user_profile)
                                            .error(R.drawable.user_profile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(iv_profile);

                                } else if (post_picture.contains(".png")) {
                                    Picasso.get()
                                            .load(post_picture)
                                            .error(R.drawable.user_profile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(iv_profile);

                                } else {
                                    Picasso.get()
                                            .load(post_picture)
                                            .error(R.drawable.user_profile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(iv_profile);
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
                Toast.makeText(ProfileActivity.this, "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void editUserInfo(String vendorId, String fName, String email, String location, String mobile, String adharcard_no, String gst_no, String image) {
        pd = new ProgressDialog(ProfileActivity.this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();

        Call<AddOfferResponse> call = apiInterface.editProfile(vendorId, fName, email, location, mobile, adharcard_no, gst_no, image);

        call.enqueue(new Callback<AddOfferResponse>() {
            @Override
            public void onResponse(Call<AddOfferResponse> call, Response<AddOfferResponse> response) {
                pd.dismiss();
                String str_response = new Gson().toJson(response.body());
                //  Log.e("" + TAG, "Response >>>>" + str_response);
                try {
                    if (response.isSuccessful()) {
                        AddOfferResponse editProfileResponse = response.body();

                        if (editProfileResponse != null) {
                            String status = editProfileResponse.getStatus();
                            String message = editProfileResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddOfferResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, "Server Error :" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }
}