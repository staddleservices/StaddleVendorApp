package staddlevendor.com.staddlevendor.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.retrofitApi.BaseApi;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;
import staddlevendor.com.staddlevendor.utils.AppConstants;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;

import static android.widget.Toast.LENGTH_LONG;

public class ChangeImageActivity extends AppCompatActivity {

    ImageView iv_back;
    ImageView imgOne, imgTwo, imgThree, imgFour, imgFive, imgGst, imgPanCard, imgWork, imgAadhar;
    ImageView imgCamera1, imgCamera2, imgCamera3, imgCamera4, imgCamera5, imgCamera7, imgCamera8, imgCamera9, imgCamera10;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 2;
    int imageCode;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        AppConstants.ChangeStatusBarColor(ChangeImageActivity.this);

        setUpView();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 1;
                showPictureDialog();
            }
        });

        imgCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 2;
                showPictureDialog();
            }
        });

        imgCamera3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 3;
                showPictureDialog();
            }
        });

        imgCamera4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 4;
                showPictureDialog();
            }
        });

        imgCamera5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 5;
                showPictureDialog();
            }
        });


        imgCamera7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 7;
                showPictureDialog();
            }
        });

        imgCamera8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 8;
                showPictureDialog();
            }
        });

        imgCamera9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 9;
                showPictureDialog();
            }
        });


        imgCamera10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null) {
                    bitmap = null;
                }
                imageCode = 10;
                showPictureDialog();
            }
        });

        if (CheckNetwork.isNetworkAvailable(this)) {
            getVenderDetails(AppPreferences.loadPreferences(this, "USER_ID"));
        } else {
            Toast.makeText(this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
        }

    }

    private void setUpView() {
        iv_back = findViewById(R.id.iv_back);
        imgOne = findViewById(R.id.imgOne);
        imgTwo = findViewById(R.id.imgTwo);
        imgThree = findViewById(R.id.imgThree);
        imgFour = findViewById(R.id.imgFour);
        imgFive = findViewById(R.id.imgFive);
        imgGst = findViewById(R.id.imgGst);
        imgPanCard = findViewById(R.id.imgPanCard);
        imgWork = findViewById(R.id.imgWork);
        imgAadhar = findViewById(R.id.imgAadhar);
        imgCamera1 = findViewById(R.id.imgCamera1);
        imgCamera2 = findViewById(R.id.imgCamera2);
        imgCamera3 = findViewById(R.id.imgCamera3);
        imgCamera4 = findViewById(R.id.imgCamera4);
        imgCamera5 = findViewById(R.id.imgCamera5);
        imgCamera7 = findViewById(R.id.imgCamera7);
        imgCamera8 = findViewById(R.id.imgCamera8);
        imgCamera9 = findViewById(R.id.imgCamera9);
        imgCamera10 = findViewById(R.id.imgCamera10);
    }

    private void showPictureDialog() {
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ChangeImageActivity.this);
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
        } else {
            // no camera on this device
            Toast.makeText(ChangeImageActivity.this, "Camera not supported", LENGTH_LONG).show();
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
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == MY_GALLERY_REQUEST_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String imageString = convertToBase64(bitmap);
                    if (imageCode == 1) {
                        imgOne.setImageBitmap(bitmap);
                    } else if (imageCode == 2) {
                        imgTwo.setImageBitmap(bitmap);
                    } else if (imageCode == 3) {
                        imgThree.setImageBitmap(bitmap);
                    } else if (imageCode == 4) {
                        imgFour.setImageBitmap(bitmap);
                    } else if (imageCode == 5) {
                        imgFive.setImageBitmap(bitmap);
                    } else if (imageCode == 7) {
                        imgGst.setImageBitmap(bitmap);
                    } else if (imageCode == 8) {
                        imgPanCard.setImageBitmap(bitmap);
                    } else if (imageCode == 9) {
                        imgWork.setImageBitmap(bitmap);
                    } else if (imageCode == 10) {
                        imgAadhar.setImageBitmap(bitmap);
                    }

                    if (CheckNetwork.isNetworkAvailable(this)) {
                        updateVenderImages(imageCode, imageString);
                    } else {
                        Toast.makeText(this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ChangeImageActivity.this, "Cancelled Image Capture", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == MY_CAMERA_REQUEST_CODE) {
            try {
                bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                String imageString = convertToBase64(bitmap);
                if (imageCode == 1) {
                    imgOne.setImageBitmap(bitmap);
                } else if (imageCode == 2) {
                    imgTwo.setImageBitmap(bitmap);
                } else if (imageCode == 3) {
                    imgThree.setImageBitmap(bitmap);
                } else if (imageCode == 4) {
                    imgFour.setImageBitmap(bitmap);
                } else if (imageCode == 5) {
                    imgFive.setImageBitmap(bitmap);
                } else if (imageCode == 7) {
                    imgGst.setImageBitmap(bitmap);
                } else if (imageCode == 8) {
                    imgPanCard.setImageBitmap(bitmap);
                } else if (imageCode == 9) {
                    imgWork.setImageBitmap(bitmap);
                } else if (imageCode == 10) {
                    imgAadhar.setImageBitmap(bitmap);
                }

                if (CheckNetwork.isNetworkAvailable(this)) {
                    updateVenderImages(imageCode, imageString);
                } else {
                    Toast.makeText(this, "Check your internet connection !", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String convertToBase64(Bitmap bitmap) {
        bitmap = compressImage(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

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

    private void getVenderDetails(String vid) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.getVenderImageDetails(vid);

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    if (object.getString("status").equals("1")) {
                        JSONArray info = object.getJSONArray("info");

                        if (info.getJSONObject(0).getString("image1").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("image1"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgOne);

                        } else if (!info.getJSONObject(0).getString("image1").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("image1"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgOne);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgOne);
                        }


                        if (info.getJSONObject(0).getString("image2").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("image2"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgTwo);

                        } else if (!info.getJSONObject(0).getString("image2").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("image2"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgTwo);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgTwo);
                        }


                        if (info.getJSONObject(0).getString("image3").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("image3"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgThree);

                        } else if (!info.getJSONObject(0).getString("image3").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("image3"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgThree);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgThree);
                        }


                        if (info.getJSONObject(0).getString("image4").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("image4"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgFour);

                        } else if (!info.getJSONObject(0).getString("image4").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("image4"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgFour);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgFour);
                        }


                        if (info.getJSONObject(0).getString("image5").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("image5"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgFive);

                        } else if (!info.getJSONObject(0).getString("image5").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("image5"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgFive);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgFive);
                        }


                        if (info.getJSONObject(0).getString("gst_no").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("gst_no"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgGst);

                        } else if (!info.getJSONObject(0).getString("gst_no").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("gst_no"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgGst);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgGst);
                        }

                        if (info.getJSONObject(0).getString("pan_cardimg").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("pan_cardimg"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgPanCard);

                        } else if (!info.getJSONObject(0).getString("pan_cardimg").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("pan_cardimg"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgPanCard);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgPanCard);
                        }


                        if (info.getJSONObject(0).getString("work_certifaction").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("work_certifaction"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgWork);

                        } else if (!info.getJSONObject(0).getString("work_certifaction").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("work_certifaction"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgWork);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgWork);
                        }


                        if (info.getJSONObject(0).getString("adharcard_no").contains(BaseApi.IMAGE_BASE_POST_URL)) {
                            Picasso.get()
                                    .load(info.getJSONObject(0).getString("adharcard_no"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgAadhar);

                        } else if (!info.getJSONObject(0).getString("adharcard_no").equals("")) {
                            Picasso.get()
                                    .load(BaseApi.IMAGE_BASE_POST_URL + info.getJSONObject(0).getString("adharcard_no"))
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgAadhar);
                        } else {
                            Picasso.get()
                                    .load(R.color.color_change_pass)
                                    .placeholder(R.color.color_change_pass)
                                    .error(R.color.color_change_pass)
                                    .into(imgAadhar);
                        }


                    } else {
                        Toast.makeText(ChangeImageActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(ChangeImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVenderImages(int imageCode, String value) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = null;

        if (imageCode == 1) {
            call = apiInterface.updateVenderImages1(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 2) {
            call = apiInterface.updateVenderImages2(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 3) {
            call = apiInterface.updateVenderImages3(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 4) {
            call = apiInterface.updateVenderImages4(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 5) {
            call = apiInterface.updateVenderImages5(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 7) {
            call = apiInterface.updateVenderImages6(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 8) {
            call = apiInterface.updateVenderImages7(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 9) {
            call = apiInterface.updateVenderImages8(AppPreferences.loadPreferences(this, "USER_ID"), value);
        } else if (imageCode == 10) {
            call = apiInterface.updateVenderImages9(AppPreferences.loadPreferences(this, "USER_ID"), value);
        }

        call.enqueue(new Callback<JsonElement>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                pd.dismiss();
                try {
                    assert response.body() != null;
                    JSONObject object = new JSONObject(response.body().toString());
                    if (object.getString("status").equals("1")) {
                        Toast.makeText(ChangeImageActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();
                        getVenderDetails(AppPreferences.loadPreferences(ChangeImageActivity.this, "USER_ID"));
                    } else
                        Toast.makeText(ChangeImageActivity.this, "" + object.getString("message"), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                pd.dismiss();
                Toast.makeText(ChangeImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
