package staddlevendor.com.staddlevendor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.utils.AppConstants;

public class ProductListDetailsActivity extends AppCompatActivity {
    ImageView iv_product_image, img_back, img_product_image;
    TextView txt_vendor_name, txt_offer_namee, tv_current_pricee, tv_offerpricee,
            tv_current_price, tv_offerprice, txt_vendor_mobile, txt_product_details,
            txt_product_detailss, txt_vendor_email, txt_vendor_location, txt_Favourite,
            txt_offer_start_date, txt_offer_end_date, txt_save;
    String productId, vimage, image, vmobile, offerstartdate, offerenddate, offerprice, currentprice, details, productname, vfname, vid, vlname, vlocation, vemail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_details);
        AppConstants.ChangeStatusBarColor(ProductListDetailsActivity.this);
        find_All_IDs();

        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productId");
            productname = intent.getStringExtra("name");
            details = intent.getStringExtra("details");
            currentprice = intent.getStringExtra("currentprice");
            offerprice = intent.getStringExtra("offerprice");
            offerstartdate = intent.getStringExtra("offerstartdate");
            offerenddate = intent.getStringExtra("offerenddate");
            image = intent.getStringExtra("image");

            vid = intent.getStringExtra("vid");
            vimage = intent.getStringExtra("vimage");
            vfname = intent.getStringExtra("vfname");
            vlname = intent.getStringExtra("vlname");
            vlocation = intent.getStringExtra("vlocation");
            vemail = intent.getStringExtra("vemail");
            vmobile = intent.getStringExtra("vmobile");

        }

        if (vimage == null) {
            iv_product_image.setImageResource(R.drawable.saloon1);
        } else if (vimage.equalsIgnoreCase("")) {
            iv_product_image.setImageResource(R.drawable.saloon1);
        } else {
            Picasso.get()
                    .load(vimage)
                    .placeholder(R.drawable.ic_camera)// Place holder image from drawable folder
                    .error(R.drawable.ic_envelope)
                    .into(iv_product_image);
        }

        if (image.equalsIgnoreCase("")) {
            img_product_image.setImageResource(R.drawable.saloon1);
        } else {
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_camera)// Place holder image from drawable folder
                    .error(R.drawable.ic_launcher_background)
                    .into(img_product_image);
        }


        if (vlocation == null) {
            txt_vendor_location.setText("Not Mentioned");
        } else if (vlocation.equalsIgnoreCase("")) {
            txt_vendor_location.setText("Not Mentioned");
        } else {
            txt_vendor_location.setText(vlocation);
        }

        if (vemail == null) {
            txt_vendor_email.setText("Not Mentioned");
        } else if (vemail.equalsIgnoreCase("")) {
            txt_vendor_email.setText("Not Mentioned");
        } else {
            txt_vendor_email.setText(vemail);
        }

        if (vmobile == null) {
            txt_vendor_mobile.setText("Not Mentioned");
        } else if (vmobile.equalsIgnoreCase("")) {
            txt_vendor_mobile.setText(" Not Mentioned");
        } else {
            txt_vendor_mobile.setText(vmobile);
        }

        if (vfname == null) {
            txt_vendor_name.setText("Not Mentioned");
        } else if (vfname.equalsIgnoreCase("")) {
            txt_vendor_name.setText(" Not Mentioned");
        } else {
            txt_vendor_name.setText(vfname);
        }


        // txt_vendor_name.setText(vfname + "  " + vlname);
        //  txt_vendor_email.setText(vemail);//---d
        txt_offer_start_date.setText(offerstartdate);
        txt_offer_end_date.setText(offerenddate);
        // txt_vendor_location.setText(vlocation);//---d
        txt_offer_namee.setText(productname);
        txt_product_detailss.setText(details);
        // txt_vendor_mobile.setText(vmobile);//---d
        tv_current_pricee.setText(currentprice);
        tv_offerpricee.setText(offerprice);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void find_All_IDs() {
        iv_product_image = findViewById(R.id.iv_product_image);
        txt_vendor_name = findViewById(R.id.txt_vendor_name);
        txt_product_detailss = findViewById(R.id.txt_product_detailss);
        txt_vendor_email = findViewById(R.id.txt_vendor_email);
        txt_vendor_location = findViewById(R.id.txt_vendor_location);

        txt_offer_namee = findViewById(R.id.txt_offer_namee);
        tv_current_pricee = findViewById(R.id.tv_current_pricee);
        tv_offerpricee = findViewById(R.id.tv_offerpricee);

        txt_offer_start_date = findViewById(R.id.txt_offer_start_datee);
        txt_vendor_mobile = findViewById(R.id.txt_vendor_mobile);
        txt_offer_end_date = findViewById(R.id.txt_offer_end_datee);
        img_back = findViewById(R.id.img_back);
        img_product_image = findViewById(R.id.img_product_image);
        txt_Favourite = findViewById(R.id.txt_Favourite);
        txt_save = findViewById(R.id.txt_save);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
