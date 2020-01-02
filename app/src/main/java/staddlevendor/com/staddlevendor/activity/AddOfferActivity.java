package staddlevendor.com.staddlevendor.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;

import staddlevendor.com.staddlevendor.ResponseClasses.GetSubCategoryListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.GetSubCategoryTreeListResponse;
import staddlevendor.com.staddlevendor.ResponseClasses.UploadImageResponse;
import staddlevendor.com.staddlevendor.adapter.CategoryListAdapter;
import staddlevendor.com.staddlevendor.adapter.SubCategoryListAdapter;
import staddlevendor.com.staddlevendor.adapter.SubCategoryTreeAdapter;
import staddlevendor.com.staddlevendor.bean.GetCategoryListModule;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryListModule;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryTreeListModule;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

import static android.widget.Toast.LENGTH_LONG;

public class AddOfferActivity extends AppCompatActivity {
    ImageView iv_back;
    RelativeLayout rl_add_offer, rel_category, rel_sub_category, rel_sub_categorytree;
    EditText edt_offer_name, edt_detail, edt_current_price, edt_offer_price;
    RadioButton radioToDate, radiofromDate;
    ImageView iv_category_arrow, iv_sub_category_arrow, iv_sub_category_tree_arrow, iv_profile, iv_camera;
    TextView tv_category, tv_sub_category, tv_sub_category_tree;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    Context context;
    private RecyclerView rv_category, rv_sub_category, rv_sub_category_tree;

    private CategoryListAdapter adapter;
    private SubCategoryListAdapter subadapter;
    private SubCategoryTreeAdapter subTreeAdapter;
    private GetSubCategoryListModule subCategoryListModule;
    private GetSubCategoryTreeListModule subCategoryTreeListModule;
    private ArrayList<GetCategoryListModule> getCategoryListModuleArrayList = new ArrayList<>();
    private ArrayList<GetSubCategoryListModule> getSubCategoryListModuleArrayList = new ArrayList<>();
    private ArrayList<GetSubCategoryTreeListModule> getSubCategoryTreeListModuleArrayList = new ArrayList<>();
    String categoryName;

    String offer_start_date, offer_end_date;
    String cid, sid, current_price, offer_price, offer_name, product_details, image, post_picture;
    ;
    ProgressDialog progressDialog;
    // Spinner spinner;
    String TAG = getClass().getSimpleName();
    ApiInterface apiInterface;//********
    boolean isCategoryVisbile = false;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 2;
    Bitmap bitmap;
    Uri fileUri;
    Uri imageUrl;
    String vid = "", photo, imageprofile;//===== End =======

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        AppConstants.ChangeStatusBarColor(AddOfferActivity.this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        find_all_Ids();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(AddOfferActivity.this);
        rv_sub_category.setLayoutManager(linearLayoutManager1);
        rv_sub_category.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(AddOfferActivity.this);
        rv_sub_category_tree.setLayoutManager(linearLayoutManager2);
        rv_sub_category_tree.setHasFixedSize(true);

        //=============== Api Call =========================

        cid = AppPreferences.loadPreferences(AddOfferActivity.this, "USER_CID");
        vid = AppPreferences.loadPreferences(AddOfferActivity.this, "USER_ID");

        if (CheckNetwork.isNetworkAvailable(AddOfferActivity.this)) {
            //=========== Api Call ===============
            subCategoryCall(cid);
        } else {
            Toast.makeText(AddOfferActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //============ upload photo method==================
                showPictureDialog();

            }
        });

        rel_sub_category.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isCategoryVisbile) {
                    isCategoryVisbile = true;
                    rv_sub_category.setVisibility(View.VISIBLE);
                    iv_sub_category_arrow.setImageResource(R.drawable.ic_vector_circle_down_arrow);
                } else {
                    isCategoryVisbile = false;
                    rv_sub_category.setVisibility(View.GONE);
                    iv_sub_category_arrow.setImageResource(R.drawable.ic_vector_circle_left_arrow);
                }
            }
        });
        rel_sub_categorytree.setVisibility(View.GONE);

        rel_sub_categorytree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCategoryVisbile) {
                    isCategoryVisbile = true;
                    rv_sub_category_tree.setVisibility(View.VISIBLE);
                    iv_sub_category_tree_arrow.setImageResource(R.drawable.ic_vector_circle_down_arrow);
                } else {
                    isCategoryVisbile = false;
                    rv_sub_category_tree.setVisibility(View.GONE);
                    iv_sub_category_tree_arrow.setImageResource(R.drawable.ic_vector_circle_left_arrow);
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);

                updateLabel();
            }

            private void updateLabel() {
                //  String myFormat = "yyyy-MM-dd"; //In which you need put here
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Calendar todayy = Calendar.getInstance();

                radiofromDate.setText("");

                if (myCalendar.before(todayy)) {
                    Toast.makeText(AddOfferActivity.this, "please enter valid from date", Toast.LENGTH_SHORT).show();
                } else {
                    radiofromDate.setText(sdf.format(myCalendar.getTime()));
                }
                offer_start_date = radiofromDate.getText().toString().trim();
            }
        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, month);
                myCalendar1.set(Calendar.DAY_OF_MONTH, day);

                updateLabel1();
            }

            private void updateLabel1() {
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                radioToDate.setText("");

                if (!myCalendar1.before(myCalendar)) {
                    radioToDate.setText(sdf.format(myCalendar1.getTime()));
                } else {
                    Toast.makeText(AddOfferActivity.this, "Please enter to date after from date", Toast.LENGTH_SHORT).show();
                }
                // offer.setText(sdf.format(myCalendar1.getTime()));
                offer_end_date = radioToDate.getText().toString().trim();
            }
        };

        radiofromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(AddOfferActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        radioToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddOfferActivity.this, date2, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rl_add_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidation();
            }
        });
    }// =============== End Of onCreate() ==============


    private void find_all_Ids() {
        iv_back = findViewById(R.id.iv_back);
        rl_add_offer = findViewById(R.id.rl_add_offer);
        edt_offer_name = findViewById(R.id.edt_offer_name);
        edt_detail = findViewById(R.id.tv_detail);
        radiofromDate = findViewById(R.id.radiofromDate);
        radioToDate = findViewById(R.id.radioToDate);
        edt_current_price = findViewById(R.id.edt_current_price);
        edt_offer_price = findViewById(R.id.edt_offer_price);

        rel_sub_category = findViewById(R.id.rel_sub_category);
        rel_sub_categorytree = findViewById(R.id.rel_sub_categorytree);

        iv_sub_category_arrow = findViewById(R.id.iv_sub_category_arrow);
        iv_sub_category_tree_arrow = findViewById(R.id.iv_sub_category_tree_arrow);

        iv_profile = findViewById(R.id.iv_profile);
        iv_camera = findViewById(R.id.iv_camera);

        tv_sub_category = findViewById(R.id.tv_sub_category);
        tv_sub_category_tree = findViewById(R.id.tv_sub_category_tree);
        rv_sub_category = findViewById(R.id.rv_sub_category);
        rv_sub_category_tree = findViewById(R.id.rv_sub_category_tree);
    }

    private void subCategoryCall(final String cid) {
        showProgressDialog();
        Call<GetSubCategoryListResponse> call = apiInterface.getSubCategoryList(cid);

        call.enqueue(new Callback<GetSubCategoryListResponse>() {
            @Override
            public void onResponse(Call<GetSubCategoryListResponse> call, Response<GetSubCategoryListResponse> response) {
                hideProgressDialog();
                try {
                    String str_response = new Gson().toJson(response.body());
                    String serverCode = String.valueOf(response.code());
                    Log.e("" + TAG, "Response >>>>" + str_response);

                    if (response.isSuccessful()) {
                        GetSubCategoryListResponse categoryListResponse = response.body();

                        if (categoryListResponse != null) {
                            String status = categoryListResponse.getStatus();
                            String message = categoryListResponse.getMessage();

                            if (status.equalsIgnoreCase("1")) {
                                getSubCategoryListModuleArrayList = categoryListResponse.getInfo();

                                for (int i = 0; i < getSubCategoryListModuleArrayList.size(); i++) {
                                    subCategoryListModule = getSubCategoryListModuleArrayList.get(i);
                                    String id = subCategoryListModule.getId();
                                    String cid = subCategoryListModule.getCid();
                                    String categoryName = subCategoryListModule.getSub_name();
                                }

                                subadapter = new SubCategoryListAdapter(AddOfferActivity.this, getSubCategoryListModuleArrayList);
                                rv_sub_category.setAdapter(subadapter);
                                subadapter.notifyDataSetChanged();

                                //====== Recycler view onClick ======
                                onClickRecycleView1();

                            } else {
                                Toast.makeText(AddOfferActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(AddOfferActivity.this, "Response Getting Null !!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddOfferActivity.this, serverCode + "Server Error !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetSubCategoryListResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddOfferActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickRecycleView1() {
        subadapter.setAdapterOnClick(new SubCategoryListAdapter.ListRecycleDataInterface() {
            @Override
            public void recycleAdapterOnClick(GetSubCategoryListModule spnIndCategoryListModule, int position) {
                categoryName = spnIndCategoryListModule.getSub_name();
                sid = spnIndCategoryListModule.getId();
                tv_sub_category.setText(categoryName);

                subCategoryTreeCall(sid);
            }

            private void subCategoryTreeCall(final String sid) {

                showProgressDialog();

                Call<GetSubCategoryTreeListResponse> call = apiInterface.getSubCategoryTreeList(sid);

                call.enqueue(new Callback<GetSubCategoryTreeListResponse>() {
                    @Override
                    public void onResponse(Call<GetSubCategoryTreeListResponse> call, Response<GetSubCategoryTreeListResponse> response) {
                        hideProgressDialog();
                        try {
                            String str_response = new Gson().toJson(response.body());
                            String serverCode = String.valueOf(response.code());
                            Log.e("" + TAG, "Response >>>>" + str_response);

                            if (response.isSuccessful()) {
                                GetSubCategoryTreeListResponse categoryListResponse = response.body();

                                if (categoryListResponse != null) {
                                    String status = categoryListResponse.getStatus();
                                    String message = categoryListResponse.getMessage();

                                    if (status.equalsIgnoreCase("1")) {

                                        getSubCategoryTreeListModuleArrayList = categoryListResponse.getInfo();

                                        for (GetSubCategoryTreeListModule categoryListTreeModule : getSubCategoryTreeListModuleArrayList) {
                                            String id = categoryListTreeModule.getId();
                                            String sid = categoryListTreeModule.getSid();
                                            String categoryName = categoryListTreeModule.getName();
                                            String categoryDetails = categoryListTreeModule.getDetails();
                                        }
                                        subTreeAdapter = new SubCategoryTreeAdapter(AddOfferActivity.this, getSubCategoryTreeListModuleArrayList);
                                        rv_sub_category_tree.setAdapter(subTreeAdapter);
                                        subTreeAdapter.notifyDataSetChanged();
                                        Toast.makeText(AddOfferActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                                        rel_sub_categorytree.setVisibility(View.VISIBLE);

                                        //====== Recycler view onClick ======
                                        onClickRecycleViewSubTree();
                                    } else {
                                        rel_sub_categorytree.setVisibility(View.GONE);
                                        Toast.makeText(AddOfferActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(AddOfferActivity.this, "Response Getting Null !!", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(AddOfferActivity.this, serverCode + "Server Error !!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetSubCategoryTreeListResponse> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(AddOfferActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void onClickRecycleViewSubTree() {
        subTreeAdapter.setAdapterOnClick(new SubCategoryTreeAdapter.ListRecycleDataInterface() {
            @Override
            public void recycleAdapterOnClick(GetSubCategoryTreeListModule spnIndCategoryTreeListModule, int position) {
                categoryName = spnIndCategoryTreeListModule.getName();
                sid = spnIndCategoryTreeListModule.getSid();
                tv_sub_category_tree.setText(categoryName);
            }
        });
    }

    private void showPictureDialog() {
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(AddOfferActivity.this);
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
            Toast.makeText(AddOfferActivity.this, "Camera not supported", LENGTH_LONG).show();
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
       /* try {
            imageUrl = data.getData();
            if (requestCode == 100 && resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(RegistrationActivity.this.getContentResolver(), imageUrl);
            }
            // ==== User Defined Method ======
            convertToBase64(bitmap); //converting image to base64 string

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        //---------------------------------------bitmap------------------------------------
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

                    Toast.makeText(AddOfferActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddOfferActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == MY_CAMERA_REQUEST_CODE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            assert bitmap != null;
            convertToBase64(bitmap);
            iv_profile.setImageBitmap(bitmap);

        }//------------------ End ----------------------------------------
    }

    private void convertToBase64(final Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        if (CheckNetwork.isNetworkAvailable(AddOfferActivity.this)) {
            //=========== Api Call ===============
            uploadLogoImage(image);
        } else {
            Toast.makeText(AddOfferActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }
    }// ===========*** End Image Upload Data ***=============

    private void uploadLogoImage(final String image) {
        showProgressDialog();
        Call<UploadImageResponse> call = apiInterface.uploadPicture(image);

        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                hideProgressDialog();
                String str_response = new Gson().toJson(response.body());
                Log.e("" + TAG, "Response >>>>" + str_response);
                UploadImageResponse uploadImageResponse = response.body();

                if (uploadImageResponse != null) {
                    String message = uploadImageResponse.getMessage();
                    String status = uploadImageResponse.getStatus();
                    String info = uploadImageResponse.getInfo();

                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(AddOfferActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        post_picture = info;
                    } else {
                        Toast.makeText(AddOfferActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddOfferActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void doValidation() {
        offer_name = edt_offer_name.getText().toString().trim();
        product_details = edt_detail.getText().toString().trim();
        current_price = edt_current_price.getText().toString().trim();
        offer_price = edt_offer_price.getText().toString().trim();
        String userProfilePic = post_picture;
        if (offer_name.equalsIgnoreCase("")) {
            edt_offer_name.setError("Enter Offer Name");
            edt_offer_name.requestFocus();
        } else if (product_details.equalsIgnoreCase("")) {
            edt_detail.setError("Enter Offer Details");
            edt_detail.requestFocus();
        } else if (current_price.equalsIgnoreCase("")) {
            edt_current_price.setError("Enter Current Price");
            edt_current_price.requestFocus();
        } else if (offer_price.equalsIgnoreCase("")) {
            edt_offer_price.setError("Enter Offer Price");
            edt_offer_price.requestFocus();
        } else {
            if (CheckNetwork.isNetworkAvailable(AddOfferActivity.this)) {
                doAddOffer(cid, sid, current_price, offer_price, product_details, offer_start_date, offer_end_date, userProfilePic, offer_name, vid);
            } else {
                Toast.makeText(AddOfferActivity.this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //============================== Add Offer Call ===============================================
    private void doAddOffer(String cid, String sid, String current_price, String offer_price, String product_details, String offer_start_date, String offer_end_date, String userProfilePic, String offer_name, String vid) {
        showProgressDialog();
        Call<JsonElement> call = apiInterface.AddOffer(cid, sid, current_price, offer_price, product_details, offer_start_date, offer_end_date, userProfilePic, offer_name, vid);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                hideProgressDialog();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    Log.e("" + TAG, "Response >>>>" + str_response);

                    String status = str_response.getString("status");
                    String message = str_response.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(AddOfferActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddOfferActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        finish();
                    } else {
                        Toast.makeText(AddOfferActivity.this, message + " ", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(AddOfferActivity.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(AddOfferActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Please Wait...");
        progressDialog.show();
    }

    @NonNull
    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
