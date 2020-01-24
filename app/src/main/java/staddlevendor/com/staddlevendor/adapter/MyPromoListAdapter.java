package staddlevendor.com.staddlevendor.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.activity.AddPromoCodeActivity;
import staddlevendor.com.staddlevendor.bean.MyPromoListModel;
import staddlevendor.com.staddlevendor.bean.MySingleton;
import staddlevendor.com.staddlevendor.retrofitApi.BaseApi;
import staddlevendor.com.staddlevendor.retrofitApi.EndApi;

public class MyPromoListAdapter extends RecyclerView.Adapter<MyPromoListAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyPromoListModel> myPromoListModels;
    private Dialog quantAlert;
    ProgressDialog progressDialog;

    public MyPromoListAdapter(Context context, ArrayList<MyPromoListModel> myPromoListModels) {
        this.context = context;
        this.myPromoListModels = myPromoListModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.promolistitem, viewGroup, false);
        return new MyPromoListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.promoname.setText(myPromoListModels.get(i).getPromo_name());
        myViewHolder.promo_value.setText("Percentage : "+myPromoListModels.get(i).getPromo_value());
        myViewHolder.description.setText("Description : "+myPromoListModels.get(i).getDescription());
        myViewHolder.minimum_price.setText("Applicable amount : "+myPromoListModels.get(i).getMinimum_price());
        myViewHolder.create_date.setText("Added on "+myPromoListModels.get(i).getCreate_date());
        myViewHolder.promo_type.setText("Type : "+myPromoListModels.get(i).getPromo_type());
        myViewHolder.delete_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_password_box(myPromoListModels.get(i).getVid(),myPromoListModels.get(i).getPromo_name(),i);
            }
        });

    }

    private void delete_promoCode(final String vid, final String promo_name,final String password,final int i) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BaseApi.BASE_URL+EndApi.DELETE_PROMO_VENDOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.e("DELETEPROMO",response);
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String ans = jsonObject.getString("status");
                        Toast.makeText(context, ans, Toast.LENGTH_SHORT).show();
                        quantAlert.dismiss();
                        myPromoListModels.remove(i);
                        notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("vendor_id",vid);
                map.put("promo_name",promo_name);
                map.put("password",password);
                return map;
            }
        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(context).addTorequestque(stringRequest);
    }

    @Override
    public int getItemCount() {
        return myPromoListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView promoname;
        TextView promo_value;
        TextView promo_percentage;
        TextView description;
        TextView minimum_price;
        TextView create_date;
        TextView promo_type;
        TextView delete_promo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            promoname = itemView.findViewById(R.id.promonamecode);
            promo_value = itemView.findViewById(R.id.promo_minimum_price);
            description = itemView.findViewById(R.id.promocontent);
            promo_percentage = itemView.findViewById(R.id.promo_percentage);
            promo_type = itemView.findViewById(R.id.promo_type);
            minimum_price = itemView.findViewById(R.id.promo_minimum_price);
            create_date = itemView.findViewById(R.id.create_date);
            delete_promo = itemView.findViewById(R.id.delete_promo);



        }
    }


    private void create_password_box(final String vid, final String promoname,final int i) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.delete_promo_dialog,
                null, false);

        final Button add=(Button) formElementsView.findViewById(R.id.delete_promo_btn);
        final EditText vendor_password = (EditText) formElementsView.findViewById(R.id.vendor_password_delete_promo);
        final TextView cancel_promo_btn = formElementsView.findViewById(R.id.promo_cancel_btn);


        cancel_promo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantAlert.dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(vendor_password.getText().toString().equals("")){
                    Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    String password = vendor_password.getText().toString();
                    delete_promoCode(vid,promoname,password,i);
                }
            }
        });



        quantAlert=new AlertDialog.Builder(context).setView(formElementsView)
                .setCancelable(false)
                .show();
        quantAlert.getWindow().getAttributes().windowAnimations = R.anim.zoom_out;
        quantAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
