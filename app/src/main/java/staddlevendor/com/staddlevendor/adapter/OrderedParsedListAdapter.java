package staddlevendor.com.staddlevendor.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.activity.OrderAddressActivity;
import staddlevendor.com.staddlevendor.bean.OrderParsedListModel;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;

public class OrderedParsedListAdapter extends RecyclerView.Adapter<OrderedParsedListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OrderParsedListModel> orderParderListModelArrayList;
    private ApiInterface apiInterface;
    private ProgressDialog pd;

    public OrderedParsedListAdapter(Context mContext, ArrayList<OrderParsedListModel> orderParderListModelArrayList) {
        this.mContext = mContext;
        this.orderParderListModelArrayList = orderParderListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_order_parsed, viewGroup, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            final OrderParsedListModel parsedListModel = orderParderListModelArrayList.get(i);
            myViewHolder.txt_quantity.setText(parsedListModel.getCount());
            myViewHolder.txt_menu_name.setText(parsedListModel.getMenu_name());
            myViewHolder.txt_price.setText(parsedListModel.getMenu_price());

            float a = Float.parseFloat(parsedListModel.getCount());
            float b = Float.parseFloat(parsedListModel.getMenu_price());
            int count = (int) a;
            int price = (int) b;
            int total = count * price;
            myViewHolder.txt_total_amount.setText("" + total);

            myViewHolder.img_rejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateProductStatusRejected(parsedListModel.getId());
                }
            });
            myViewHolder.img_accepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateProductStatusAccepted(parsedListModel.getId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderParderListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_price, txt_menu_name, txt_count, txt_total_amount, txt_quantity;
        ImageView img_rejected, img_accepted;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_menu_name = itemView.findViewById(R.id.txt_menu_name);
            txt_total_amount = itemView.findViewById(R.id.txt_total_amount);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            img_rejected = itemView.findViewById(R.id.img_rejected);
            img_accepted = itemView.findViewById(R.id.img_accepted);
            txt_count = itemView.findViewById(R.id.txt_count);
        }
    }

    private void updateProductStatusAccepted(String productId) {
        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String vname = AppPreferences.loadPreferences(mContext, "USER_NAME");
        Call<JsonElement> call = apiInterface.updateProductSatusAccepted(productId,vname,"","");

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, final Response<JsonElement> response) {
                pd.dismiss();
                //   Log.d("" + TAG, "" + response);
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    //    Log.e("" + TAG, "Response >>>>" + str_response);

                    String status = str_response.getString("status");
                    String message = str_response.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductStatusRejected(String productId) {
        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Loading Please Wait...");
        pd.show();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonElement> call = apiInterface.updateProductSatusRejected(productId);

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, final Response<JsonElement> response) {
                pd.dismiss();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    String status = str_response.getString("status");
                    String message = str_response.getString("message");

                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
