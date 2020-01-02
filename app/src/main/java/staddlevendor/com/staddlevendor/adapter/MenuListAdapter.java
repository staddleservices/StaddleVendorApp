package staddlevendor.com.staddlevendor.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import staddlevendor.com.staddlevendor.HomeActivity;
import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.activity.MenuListActivity;
import staddlevendor.com.staddlevendor.bean.MenuListModule;
import staddlevendor.com.staddlevendor.retrofitApi.ApiClient;
import staddlevendor.com.staddlevendor.retrofitApi.ApiInterface;
import staddlevendor.com.staddlevendor.utils.CheckNetwork;


public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MenuListModule> menuListModuleArrayList;
    private OnItemClickListener listener;

    public MenuListAdapter(Context mContext, ArrayList<MenuListModule> menuListModuleArrayList) {
        this.mContext = mContext;
        this.menuListModuleArrayList = menuListModuleArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_menu_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MenuListModule menuListModule = menuListModuleArrayList.get(position);
        holder.bind(menuListModule, listener);
        final String name = menuListModule.getName();
        String price = menuListModule.getPrice();

        holder.txt_menu_name.setText(name);
        holder.txt_service_price.setText(price);
        holder.txt__category_name.setText("Category Name: "+menuListModule.getSubCatName());

        if (menuListModule.getStatus().equalsIgnoreCase("A")) {
            holder.btn_switch.setChecked(true);
        } else {
            holder.btn_switch.setChecked(false);
        }

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuListActivity) mContext).editMenuDialog(menuListModuleArrayList.get(position).getId(), menuListModuleArrayList.get(position).getName(), menuListModuleArrayList.get(position).getPrice());
            }
        });

        holder.imdDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MenuListActivity) mContext).deleteMenu(menuListModuleArrayList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuListModuleArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEdit, imdDelete;
        TextView txt_menu_name, txt_service_price,txt__category_name;
        SwitchCompat btn_switch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imdDelete = itemView.findViewById(R.id.imdDelete);
            txt_menu_name = itemView.findViewById(R.id.txt_service_name);
            txt_service_price = itemView.findViewById(R.id.txt_service_price);
            btn_switch = itemView.findViewById(R.id.btn_switch_old);
            txt__category_name = itemView.findViewById(R.id.txt__category_name);
        }

        void bind(final MenuListModule menuListModule, final OnItemClickListener listener) {
            btn_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menuListModule.getStatus().equalsIgnoreCase("A"))
                        listener.onItemClick(menuListModule, "D");
                    else
                        listener.onItemClick(menuListModule, "A");
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(MenuListModule menuListModule, String isStatus);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

