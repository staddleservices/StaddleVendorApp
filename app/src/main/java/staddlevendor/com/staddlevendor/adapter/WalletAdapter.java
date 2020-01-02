package staddlevendor.com.staddlevendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.bean.WalletModel;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<WalletModel> walletModelArrayList;

    public WalletAdapter(Context mContext, ArrayList<WalletModel> walletModelArrayList) {
        this.mContext = mContext;
        this.walletModelArrayList = walletModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_wallet, viewGroup, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int position) {
        try {
            final WalletModel walletModel = walletModelArrayList.get(position);
            myViewHolder.txtUser.setText(walletModel.getUser_name());
            float tot = Float.parseFloat(walletModel.getOrder_price());
            float comm = Float.parseFloat(walletModel.getDiscount());
            float totalPrice = tot - comm;
            if (tot > comm)
                myViewHolder.txtAmount.setText("Rs. " + totalPrice);
            else
                myViewHolder.txtAmount.setText("Rs. " + tot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return walletModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtUser, txtAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtAmount = itemView.findViewById(R.id.txtAmount);
        }
    }

}
