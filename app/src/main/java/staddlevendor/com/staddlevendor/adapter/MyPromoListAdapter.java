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
import staddlevendor.com.staddlevendor.bean.MyPromoListModel;

public class MyPromoListAdapter extends RecyclerView.Adapter<MyPromoListAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyPromoListModel> myPromoListModels;

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
                delete_promoCode(myPromoListModels.get(i).getVid(),myPromoListModels.get(i).getPromo_name());
            }
        });

    }

    private void delete_promoCode(String vid, String promo_name) {
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
}
