package staddlevendor.com.staddlevendor.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.activity.OrderAddressActivity;
import staddlevendor.com.staddlevendor.activity.ProductListDetailsActivity;
import staddlevendor.com.staddlevendor.bean.PendingListModel;
import staddlevendor.com.staddlevendor.sheardPref.AppPreferences;

public class RejectedListAdapter extends RecyclerView.Adapter<RejectedListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<PendingListModel> pendingListModelArrayList;
    private String tag = "";

    public RejectedListAdapter(Context mContext, ArrayList<PendingListModel> pendingListModelArrayList, String tag) {
        this.mContext = mContext;
        this.pendingListModelArrayList = pendingListModelArrayList;
        this.tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_reject, viewGroup, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {
        try {
            PendingListModel pendingListModel = pendingListModelArrayList.get(position);
            final String image = pendingListModel.getVimage();
            String user_name = pendingListModel.getUser_name();

            myViewHolder.txtPayment.setText(pendingListModel.getPayment());
            myViewHolder.txtName.setText(user_name);

//            if (!pendingListModel.getHouse_number().equals(""))
//                myViewHolder.txtDetails.setText(pendingListModel.getHouse_number() + ", "
//                        + pendingListModel.getLandmark() + ", " + pendingListModel.getCity());
//            else
//                myViewHolder.txtDetails.setText("");
//
//            if (AppPreferences.loadPreferences(mContext, "USER_CID").equals("1")) {
//                myViewHolder.txtTag.setVisibility(View.VISIBLE);
//                if (!pendingListModel.getHouse_number().equals(""))
//                    myViewHolder.txtTag.setText("At Home"); //At Salon
//                else
//                    myViewHolder.txtTag.setText("At Salon");
//            } else
//                myViewHolder.txtTag.setVisibility(View.GONE);

            float tot = Float.parseFloat(pendingListModel.getOrder_price());
            float comm = Float.parseFloat(pendingListModel.getDiscount());
            float totalPrice = tot - comm;
            myViewHolder.tv_current_price.setText("" + totalPrice);

            myViewHolder.txtBookDate.setText(pendingListModel.getBooked_date());
            myViewHolder.txtBookTime.setText(pendingListModel.getBooking_slot());
            if (image == null || image.equalsIgnoreCase("")) {
                myViewHolder.iv_offer_image.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                        .error(R.drawable.saloon1)
                        .into(myViewHolder.iv_offer_image);
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OrderAddressActivity.class);
                    intent.putParcelableArrayListExtra("DATALIST", pendingListModelArrayList.get(position).getData());
                    intent.putExtra("TAG", tag);
                    intent.putExtra("ID", pendingListModelArrayList.get(position).getId());
                    intent.putExtra("TOTAL", pendingListModelArrayList.get(position).getOrder_price());
                    intent.putExtra("DISCOUNT_PRICE", pendingListModelArrayList.get(position).getDiscount());
                    intent.putExtra("PRICE", pendingListModelArrayList.get(position).getDiscount_price());
                    intent.putExtra("NAME", pendingListModelArrayList.get(position).getUser_name());

//                    if (!pendingListModelArrayList.get(position).getState().equals(""))
//                        intent.putExtra("Mobile", pendingListModelArrayList.get(position).getState());
//                    else
//                        intent.putExtra("Mobile", pendingListModelArrayList.get(position).getUser_mobile());
//
//
//                    if (!pendingListModelArrayList.get(position).getHouse_number().equals(""))
//                        intent.putExtra("ADDRESS", pendingListModelArrayList.get(position).getHouse_number() + ", "
//                                + pendingListModelArrayList.get(position).getLandmark() + ", " + pendingListModelArrayList.get(position).getCity());
//                    else
//                        intent.putExtra("ADDRESS", "");

                    intent.putExtra("TIME", pendingListModelArrayList.get(position).getBooking_slot());
                    intent.putExtra("DATE", pendingListModelArrayList.get(position).getBooked_date());
                    mContext.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pendingListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtTag, txtPayment, txtDetails, txtBookDate, txtBookTime, tv_current_price, tv_offerprice;
        ImageView iv_offer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTag = itemView.findViewById(R.id.txtTag);
            txtBookTime = itemView.findViewById(R.id.txtBookTime);
            txtPayment = itemView.findViewById(R.id.txtPayment);
            txtBookDate = itemView.findViewById(R.id.txtBookDate);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetails = itemView.findViewById(R.id.txt_details);
            tv_current_price = itemView.findViewById(R.id.tv_current_price);
            //  tv_offerprice = itemView.findViewById(R.id.tv_offerprice);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
        }
    }
}
