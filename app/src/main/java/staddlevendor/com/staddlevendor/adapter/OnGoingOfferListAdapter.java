package staddlevendor.com.staddlevendor.adapter;

import android.content.Context;
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
import staddlevendor.com.staddlevendor.bean.OnGoingOfferListModel;

import static staddlevendor.com.staddlevendor.retrofitApi.BaseApi.IMAGE_BASE_POST_URL;

public class OnGoingOfferListAdapter extends RecyclerView.Adapter<OnGoingOfferListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<OnGoingOfferListModel> onGoingOfferListModelArrayList;


    public OnGoingOfferListAdapter(Context mContext, ArrayList<OnGoingOfferListModel> onGoingOfferListModelArrayList) {
        this.mContext = mContext;
        this.onGoingOfferListModelArrayList = onGoingOfferListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_ongoing_offer, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        try {
            OnGoingOfferListModel onGoingOfferListModel = onGoingOfferListModelArrayList.get(position);
            String product_details = onGoingOfferListModel.getProduct_details();
            String current_price = onGoingOfferListModel.getCurrent_price();
            String offer_price = onGoingOfferListModel.getOffer_price();
            String image = onGoingOfferListModel.getImage();
            String product_name = onGoingOfferListModel.getProduct_name();

            //String offer_price=productListModel.getOffer_price();
            myViewHolder.txtDetails.setText(product_details);
            myViewHolder.txtName.setText(product_name);
            myViewHolder.tv_current_price.setText(current_price);
            myViewHolder.tv_offerprice.setText(offer_price);
            //  String image_url=IMAGE_BASE_POST_URL+image;

            if (image == null || image.equalsIgnoreCase("")) {
                myViewHolder.iv_offer_image.setImageResource(R.drawable.ic_launcher_background);
            } else {
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.ic_launcher_background)// Place holder image from drawable folder
                        .error(R.drawable.saloon1)
                        .into(myViewHolder.iv_offer_image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return onGoingOfferListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDetails, tv_current_price, tv_offerprice;
        ImageView iv_offer_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDetails = itemView.findViewById(R.id.txt_details);
            tv_current_price = itemView.findViewById(R.id.tv_current_price);
            tv_offerprice = itemView.findViewById(R.id.tv_offerprice);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
        }
    }
}
