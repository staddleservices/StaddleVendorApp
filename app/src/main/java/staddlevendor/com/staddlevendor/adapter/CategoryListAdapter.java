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

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Context context;
    private List<GetCategoryListModule> getCategoryArrayList;

    public CategoryListAdapter(Context context, List<GetCategoryListModule> getCategoryArrayList) {
        this.context=context;
        this.getCategoryArrayList=getCategoryArrayList;
    }
    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_category_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, final int position) {

        final GetCategoryListModule CategoryListModule = getCategoryArrayList.get(position);

        holder.tvCategory_name.setText(CategoryListModule.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendeesInterface.recycleAdapterOnClick(CategoryListModule, position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return getCategoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory_name=itemView.findViewById(R.id.tvCategory_name);
        }
    }

    // ======================================= Adapter Click Interface ================================//
    /*__________________________________________________________________________________________________*/

    // Interface
    public interface ListRecycleDataInterface {
        void recycleAdapterOnClick(GetCategoryListModule spnIndCategoryListModule, int position);
    }

    // Interface object
    private CategoryListAdapter.ListRecycleDataInterface attendeesInterface;

    // Interface method
    public void setAdapterOnClick(CategoryListAdapter.ListRecycleDataInterface attendeesInterface) {
        this.attendeesInterface = attendeesInterface;
    }

}
