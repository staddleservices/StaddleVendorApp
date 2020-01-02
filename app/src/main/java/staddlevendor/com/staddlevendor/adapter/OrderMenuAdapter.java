package staddlevendor.com.staddlevendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.bean.OrderParsedListModel;

public class OrderMenuAdapter extends RecyclerView.Adapter<OrderMenuAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OrderParsedListModel> orderList;

    public OrderMenuAdapter(Context mContext, ArrayList<OrderParsedListModel> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_order_details, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(orderList.get(position).getMenu_name());
        holder.tv_price.setText("" + orderList.get(position).getMenu_price() + " ");
        holder.tv_count.setText("" + orderList.get(position).getCount());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_price, tv_count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}