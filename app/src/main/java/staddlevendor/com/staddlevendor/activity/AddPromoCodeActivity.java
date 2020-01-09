package staddlevendor.com.staddlevendor.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.adapter.CustomSpinnerAdapter;
import staddlevendor.com.staddlevendor.adapter.MyPromoListAdapter;
import staddlevendor.com.staddlevendor.bean.MyPromoListModel;
import staddlevendor.com.staddlevendor.bean.MySingleton;
import staddlevendor.com.staddlevendor.retrofitApi.EndApi;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;

import static java.security.AccessController.getContext;
import static staddlevendor.com.staddlevendor.retrofitApi.EndApi.ADD_COUPONS;
import static staddlevendor.com.staddlevendor.retrofitApi.EndApi.FETCH_VENDOR_PROMOS;

public class AddPromoCodeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    RecyclerView my_promo_list;
    ArrayList<MyPromoListModel> myPromoListModels;
    ImageView add_promo;
    private AlertDialog quantAlert;
    List<String> methods;
    String promo_type_selected="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promo_code);
        init();
        add_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_add_promo_box();
            }
        });
        fetch_vendor_promos(AppPreferences.loadPreferences(AddPromoCodeActivity.this, "USER_ID"));
    }

    private void create_add_promo_box() {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.addpromo_dialog,
                null, false);
        final EditText promo_name=(EditText) formElementsView.findViewById(R.id.promo_name);
        promo_name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        final EditText promo_value=(EditText) formElementsView.findViewById(R.id.promo_value);
        final EditText mprice=(EditText) formElementsView.findViewById(R.id.mprice);
        final EditText description=(EditText) formElementsView.findViewById(R.id.promo_description);
        final Button add=(Button) formElementsView.findViewById(R.id.add_promo_btn);
        final EditText vendor_password = (EditText) formElementsView.findViewById(R.id.vendor_password);
        final TextView cancel_promo_btn = (TextView) formElementsView.findViewById(R.id.promo_cancel_btn);
        final Spinner spinner_types = (Spinner) formElementsView.findViewById(R.id.promo_type_spinner) ;
        spinner_types.setAdapter(new CustomSpinnerAdapter(AddPromoCodeActivity.this, R.layout.spinneritemlayout, methods));
        spinner_types.setOnItemSelectedListener(this);

        cancel_promo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantAlert.dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promonameentered=promo_name.getText().toString();
                String promovalueentered = promo_value.getText().toString();
                String ApplicableAmount = mprice.getText().toString();
                String descriptionentered = description.getText().toString();
                String password = vendor_password.getText().toString();
                if(password.equals("") || promonameentered.equals("") || promovalueentered.equals("") || ApplicableAmount.equals("") || descriptionentered.equals("") || promo_type_selected.equals("")){
                    Toast.makeText(AddPromoCodeActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }else{
                    addpromo_db(password,promonameentered,promovalueentered,ApplicableAmount,descriptionentered,promo_type_selected);
                }
            }
        });



        quantAlert=new AlertDialog.Builder(AddPromoCodeActivity.this).setView(formElementsView)
                .setCancelable(false)
                .show();
        quantAlert.getWindow().getAttributes().windowAnimations = R.anim.zoom_out;
        quantAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void addpromo_db(final String password, final String promonameentered, final String promovalueentered, final String applicableAmount, final String descriptionentered, final String promo_type_selected) {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, ADD_COUPONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.e("response",response);
                        quantAlert.dismiss();
                        myPromoListModels.clear();
                        fetch_vendor_promos(AppPreferences.loadPreferences(AddPromoCodeActivity.this,"USER_ID"));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("responce", error.toString() );
                new AlertDialog.Builder(AddPromoCodeActivity.this)
                        .setTitle("Connection failed!")
                        .setCancelable(false)
                        .setMessage("Please check your internet connection or restart the App!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("vendor_id",AppPreferences.loadPreferences(AddPromoCodeActivity.this,"USER_ID"));
                params.put("promo_name",promonameentered);
                params.put("description",descriptionentered);
                params.put("promo_value",promovalueentered);
                params.put("minimum_price",applicableAmount);
                params.put("promo_type",promo_type_selected);
                params.put("password",password);



                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    private void fetch_vendor_promos(final String user_id) {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, FETCH_VENDOR_PROMOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.e("response",response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String promoname = jsonObject.getString("promonames");
                                String promovalue = jsonObject.getString("promovalue");
                                String mprice = jsonObject.getString("mprice");
                                String description = jsonObject.getString("description");
                                String type = jsonObject.getString("type");
                                String create_date = jsonObject.getString("create_date");

                                myPromoListModels.add(new MyPromoListModel(AppPreferences.loadPreferences(AddPromoCodeActivity.this,"USER_ID"),promoname,description,
                                        promovalue,mprice,type,create_date));

                            }

                            MyPromoListAdapter myPromoListAdapter = new MyPromoListAdapter(AddPromoCodeActivity.this,myPromoListModels);
                            my_promo_list.setAdapter(myPromoListAdapter);
                            my_promo_list.hasFixedSize();
                            myPromoListAdapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("responce", error.toString() );
                new AlertDialog.Builder(AddPromoCodeActivity.this)
                        .setTitle("Connection failed!")
                        .setCancelable(false)
                        .setMessage("Please check your internet connection or restart the App!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("vid",user_id);



                return params;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(getApplicationContext()).addTorequestque(stringRequest);
    }

    private void init() {

        my_promo_list = findViewById(R.id.mypromolist);
        my_promo_list.setLayoutManager(new LinearLayoutManager(AddPromoCodeActivity.this));
        myPromoListModels = new ArrayList<>();
        add_promo = findViewById(R.id.add_promo_plus);
        methods = new ArrayList<>();
        methods.add("REGULAR");
        methods.add("REFERED");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        promo_type_selected = String.valueOf(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
