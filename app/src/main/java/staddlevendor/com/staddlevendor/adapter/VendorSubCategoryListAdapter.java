package staddlevendor.com.staddlevendor.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.bean.GetVendorSubCategoryListModule;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;

public class VendorSubCategoryListAdapter extends RecyclerView.Adapter<VendorSubCategoryListAdapter.MyViewHolder> {

    private Context context;
    private List<GetVendorSubCategoryListModule> getVendorSubCategoryListModuleList;
    String isManagetype;
    ApiInterface apiInterface;
    ProgressDialog progressDialog;
    String vid = "", subCatName = "", id = "", Name = "";

    public VendorSubCategoryListAdapter(Context context, List<GetVendorSubCategoryListModule> getVendorSubCategoryListModuleList, String isManagetype) {
        this.context = context;
        this.getVendorSubCategoryListModuleList = getVendorSubCategoryListModuleList;
        this.isManagetype = isManagetype;
    }

    @NonNull
    @Override
    public VendorSubCategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_sub_category_list, parent, false);
        return new VendorSubCategoryListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorSubCategoryListAdapter.MyViewHolder holder, final int position) {
        final GetVendorSubCategoryListModule vendorSubCategoryListModule = getVendorSubCategoryListModuleList.get(position);
        vid = vendorSubCategoryListModule.getVid();
        //  id = vendorSubCategoryListModule.getId();
        //   Name=vendorSubCategoryListModule.getName();
        if (isManagetype.equalsIgnoreCase("")) {
            holder.iv_edit.setVisibility(View.GONE);
            holder.tvCategory_name.setText(vendorSubCategoryListModule.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attendeesInterface.recycleAdapterOnClick(vendorSubCategoryListModule, position);
                }
            });
        } else {
            holder.tvCategory_name.setText(vendorSubCategoryListModule.getName());
            // holder.iv_edit.setPadding(5, 5, 5, 5);
            holder.rl_for_padding.setPadding(10, 15, 10, 10);
            holder.iv_edit.setVisibility(View.VISIBLE);
            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Name = vendorSubCategoryListModule.getName();
                    id = vendorSubCategoryListModule.getId();
                    showDialogDate(context);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return getVendorSubCategoryListModuleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory_name;
        RelativeLayout rl_for_padding;
        ImageView iv_edit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory_name = itemView.findViewById(R.id.tvCategory_name);
            iv_edit = itemView.findViewById(R.id.iv_edit);
            rl_for_padding = itemView.findViewById(R.id.rl_for_padding);
        }
    }

    public interface ListRecycleDataInterface {
        void recycleAdapterOnClick(GetVendorSubCategoryListModule spnIndVendorSubCategoryListModule, int position);
    }

    // Interface object
    private VendorSubCategoryListAdapter.ListRecycleDataInterface attendeesInterface;

    // Interface method
    public void setAdapterOnClick(VendorSubCategoryListAdapter.ListRecycleDataInterface attendeesInterface) {
        this.attendeesInterface = attendeesInterface;
    }

    public void showDialogDate(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = mInflater.inflate(R.layout.dialog_edit_sub_category, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        alertDialog.getWindow().setWindowAnimations(R.style.DialogTheme);

        TextView txt_submit = dialogView.findViewById(R.id.txt_submit);
        final EditText edt_subcat_name = dialogView.findViewById(R.id.edt_subcat_name);
        edt_subcat_name.setText(Name);
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCatName = edt_subcat_name.getText().toString().trim();
                editSubCategory(id, subCatName);
            }
        });
        alertDialog.show();
    }

    private void editSubCategory(String vid, String name) {
        showProgressDialog();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<JsonElement> call = apiInterface.editSubCategory(vid, name);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                hideProgressDialog();
                try {
                    JSONObject str_response = new JSONObject(String.valueOf(response.body()));
                    //    Log.e("" + TAG, "Response >>>>" + str_response);

                    String status = str_response.getString("status");
                    String message = str_response.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(context, message + " ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, HomeActivity.class);
                        context.startActivity(intent);
                        //  overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

                    } else {
                        Toast.makeText(context, message + " ", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(context, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
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

}

