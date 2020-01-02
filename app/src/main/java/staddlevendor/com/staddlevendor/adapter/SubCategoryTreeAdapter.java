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
import staddlevendor.com.staddlevendor.bean.GetSubCategoryListModule;
import staddlevendor.com.staddlevendor.bean.GetSubCategoryTreeListModule;

public class SubCategoryTreeAdapter extends RecyclerView.Adapter<SubCategoryTreeAdapter.MyViewHolder> {

    private Context context;
    private List<GetSubCategoryTreeListModule> getSubCategoryTreeArrayList;

    public SubCategoryTreeAdapter(Context context, List<GetSubCategoryTreeListModule> getSubCategoryTreeArrayList) {
        this.context = context;
        this.getSubCategoryTreeArrayList = getSubCategoryTreeArrayList;
    }

    @NonNull
    @Override
    public SubCategoryTreeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_sub_category_list_new, parent, false);
        return new SubCategoryTreeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryTreeAdapter.MyViewHolder holder, final int position) {
        final GetSubCategoryTreeListModule SubCategoryListModule = getSubCategoryTreeArrayList.get(position);

        holder.tvCategory_name.setText(SubCategoryListModule.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendeesInterface.recycleAdapterOnClick(SubCategoryListModule, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getSubCategoryTreeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory_name = itemView.findViewById(R.id.tvCategory_name);
        }
    }

    public interface ListRecycleDataInterface {
        void recycleAdapterOnClick(GetSubCategoryTreeListModule spnIndCategoryTreeListModule, int position);
    }

    // Interface object
    private SubCategoryTreeAdapter.ListRecycleDataInterface attendeesInterface;

    // Interface method
    public void setAdapterOnClick(SubCategoryTreeAdapter.ListRecycleDataInterface attendeesInterface) {
        this.attendeesInterface = attendeesInterface;
    }
}

