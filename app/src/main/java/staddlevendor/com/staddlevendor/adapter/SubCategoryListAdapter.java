package staddlevendor.com.staddlevendor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.bean.GetCategoryListModule;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryListModule;

public class SubCategoryListAdapter extends RecyclerView.Adapter<SubCategoryListAdapter.MyViewHolder> {

    private Context context;
    private List<GetSubCategoryListModule> getSubCategoryArrayList;

    public SubCategoryListAdapter(Context context, List<GetSubCategoryListModule> getSubCategoryArrayList) {
        this.context = context;
        this.getSubCategoryArrayList = getSubCategoryArrayList;
    }

    @NonNull
    @Override
    public SubCategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_sub_category_list_new, parent, false);
        return new SubCategoryListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryListAdapter.MyViewHolder holder, final int position) {
        final GetSubCategoryListModule SubCategoryListModule = getSubCategoryArrayList.get(position);

        holder.tvCategory_name.setText(SubCategoryListModule.getSub_name());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  attendeesInterface.recycleAdapterOnClick(SubCategoryListModule, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return getSubCategoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory_name = itemView.findViewById(R.id.tvCategory_name);
        }
    }

    public interface ListRecycleDataInterface {
        void recycleAdapterOnClick(GetSubCategoryListModule spnIndCategoryListModule, int position);
    }

    // Interface object
    private SubCategoryListAdapter.ListRecycleDataInterface attendeesInterface;

    // Interface method
    public void setAdapterOnClick(SubCategoryListAdapter.ListRecycleDataInterface attendeesInterface) {
        this.attendeesInterface = attendeesInterface;
    }
}
