package staddlevendor.com.staddlevendor.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import staddlevendor.com.staddlevendor.activity.AcceptedListActivity;
import staddlevendor.com.staddlevendor.activity.PendingListActivity;
import staddlevendor.com.staddlevendor.activity.ProductListActivity;
import staddlevendor.com.staddlevendor.bean.AcceptedListModel;
import staddlevendor.com.staddlevendor.bean.CompletedOrderList;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;

public class CompletedOrderAdapters extends RecyclerView.Adapter<CompletedOrderAdapters.MyViewHolder> {
    private Context mContext;
    private ArrayList<CompletedOrderList> completedOrderLists;
    private String tag = "";
    private ApiInterface apiInterface;

    public CompletedOrderAdapters(Context mContext, ArrayList<CompletedOrderList> pendingListModelArrayList, String tag) {
        this.mContext = mContext;
        this.completedOrderLists = pendingListModelArrayList;
        this.tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_pending, viewGroup, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {
        try {
            final CompletedOrderList pendingListModel = completedOrderLists.get(position);
            pendingListModel.getData();
            String image = pendingListModel.getVimage();
            String user_name = pendingListModel.getUser_name();

//            if (AppPreferences.loadPreferences(mContext, "USER_CID").equals("1")) {
//                //myViewHolder.txtTag.setVisibility(View.VISIBLE);
//                if (!pendingListModel.getHouse_number().equals("")){
//                    myViewHolder.txtTag.setText("At Home"); //At Salon
//                }
//                else {
//                    myViewHolder.txtTag.setText("At Salon");
//                }
//            } else{
//                myViewHolder.txtTag.setVisibility(View.GONE);
//            }


//            if (tag.equals("Pending")) {
//                myViewHolder.img_accepted.setVisibility(View.VISIBLE);
//                myViewHolder.img_rejected.setVisibility(View.VISIBLE);
//            } else {
//                if (pendingListModel.getStatus().equals("P")) {
//                    myViewHolder.img_accepted.setVisibility(View.VISIBLE);
//                    myViewHolder.img_rejected.setVisibility(View.VISIBLE);
//                } else {
//                    myViewHolder.img_accepted.setVisibility(View.GONE);
//                    myViewHolder.img_rejected.setVisibility(View.GONE);
//                }
//            }
//            if (!pendingListModel.getHouse_number().equals(""))
//                myViewHolder.txtDetails.setText(pendingListModel.getHouse_number() + ", "
//                        + pendingListModel.getLandmark() + ", " + pendingListModel.getCity());
//            else
//                myViewHolder.txtDetails.setText("");

            myViewHolder.payment_mode.setText("on " + pendingListModel.getPayment());
            myViewHolder.from_user.setText("from " + user_name);
            float tot = Float.parseFloat(pendingListModel.getOrder_price());
            float comm = Float.parseFloat(pendingListModel.getDiscount());
            float totalPrice = tot - comm;
            myViewHolder.order_total.setText("  ₹ " + totalPrice);
            myViewHolder.schedule_details.setText(pendingListModel.getBooked_date() + " | " + pendingListModel.getBooking_slot());
            myViewHolder.order_id.setText(pendingListModel.getId());

//            if (image == null || image.equalsIgnoreCase("")) {
//                myViewHolder.iv_offer_image.setImageResource(R.drawable.ic_launcher_background);
//            } else {
//                Picasso.get()
//                        .load(image)
//                        .placeholder(R.drawable.ic_launcher_background)
//                        .error(R.drawable.saloon1)
//                        .into(myViewHolder.iv_offer_image);
//            }

//            myViewHolder.img_rejected.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    updateProductStatusRejected(pendingListModelArrayList.get(position).getId());
//                }
//            });
//
//            myViewHolder.img_accepted.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    updateProductStatusAccepted(pendingListModelArrayList.get(position).getId());
//                }
//            });

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AcceptedListActivity.class);
                    intent.putParcelableArrayListExtra("DATALIST", completedOrderLists.get(position).getData());
                    intent.putExtra("TAG", tag);
                    intent.putExtra("ID", completedOrderLists.get(position).getId());
                    intent.putExtra("TOTAL", completedOrderLists.get(position).getOrder_price());
                    intent.putExtra("DISCOUNT_PRICE", completedOrderLists.get(position).getDiscount());
                    intent.putExtra("PRICE", completedOrderLists.get(position).getDiscount_price());
                    intent.putExtra("NAME", completedOrderLists.get(position).getUser_name());
                    intent.putExtra("ORDER_ID", completedOrderLists.get(position).getId());
                    intent.putExtra("CREATE_DATE", completedOrderLists.get(position).getCreate_date());
                    intent.putExtra("CONTACT", completedOrderLists.get(position).getUser_mobile());
                    intent.putExtra("CONTACT_EMAIL", completedOrderLists.get(position).getUser_email());
                    intent.putExtra("PROMONAME", completedOrderLists.get(position).getPromocode());
                    intent.putExtra("PROMOCUTOFF", completedOrderLists.get(position).getPromodiscount());
                    intent.putExtra("UID", completedOrderLists.get(position).getUid());
                    intent.putExtra("STATUS", completedOrderLists.get(position).getStatus());

//                    if (!pendingListModelArrayList.get(position).getState().equals(""))
//                        intent.putExtra("Mobile", pendingListModelArrayList.get(position).getState());
//                    else
//                        intent.putExtra("Mobile", pendingListModelArrayList.get(position).getUser_mobile());
//
//                    if (!pendingListModelArrayList.get(position).getHouse_number().equals(""))
//                        intent.putExtra("ADDRESS", pendingListModelArrayList.get(position).getHouse_number() + ", "
//                                + pendingListModelArrayList.get(position).getLandmark() + ", " + pendingListModelArrayList.get(position).getCity());
//                    else
//                        intent.putExtra("ADDRESS", "");
                    intent.putExtra("ADDRESS", completedOrderLists.get(position).getCompleteaddress());

                    intent.putExtra("TIME", completedOrderLists.get(position).getBooking_slot());
                    intent.putExtra("DATE", completedOrderLists.get(position).getBooked_date());
                    mContext.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return completedOrderLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order_id, from_user, itemsx, payment_mode, schedule_details, order_time, order_total, tv_offerprice;
        //ImageView iv_offer_image, img_rejected, img_accepted;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            from_user = itemView.findViewById(R.id.from_user);
            itemsx = itemView.findViewById(R.id.itemsx);
            payment_mode = itemView.findViewById(R.id.payment_mode);
            schedule_details = itemView.findViewById(R.id.schedule_details);
            order_time = itemView.findViewById(R.id.order_time);
            order_total = itemView.findViewById(R.id.order_total);
//            tv_offerprice = itemView.findViewById(R.id.tv_offerprice);
//            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
//            img_rejected = itemView.findViewById(R.id.img_rejected);
//            img_accepted = itemView.findViewById(R.id.img_accepted);
        }
    }


}
