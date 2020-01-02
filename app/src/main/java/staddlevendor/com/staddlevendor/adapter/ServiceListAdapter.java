package staddlevendor.com.staddlevendor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.bean.ServiceListModule;


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

     Context mContext;
    private ArrayList<ServiceListModule> serviceListModuleArrayList = new ArrayList<>();

    public ServiceListAdapter(Context mContext, ArrayList<ServiceListModule> serviceListModuleArrayList) {
        this.mContext = mContext;
        this.serviceListModuleArrayList = serviceListModuleArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_of_services, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view); // pass the view to View Holder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ServiceListModule serviceListModule = serviceListModuleArrayList.get(position);

        final String service_name = serviceListModule.getService_name();
        final String amount  = serviceListModule.getTotal_amount();
        final String offer = serviceListModule.getOffer();

        holder.tv_service_name.setText(service_name);
        holder.tv_total_amount.setText(amount);
        holder.tv_offer.setText(offer);

    }

    @Override
    public int getItemCount() {
        return serviceListModuleArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
      TextView tv_service_name,tv_total_amount,tv_offer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            tv_offer = itemView.findViewById(R.id.tv_offer);
        }
    }
}

