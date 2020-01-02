package staddlevendor.com.staddlevendor.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import staddlevendor.com.staddlevendor.R;
import staddlevendor.com.staddlevendor.activity.PendingListActivity;

public class NotificationFragment extends Fragment {

    Context mContext;
    ImageView iv_back;
    TextView tv_welcome1;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mContext = getActivity();

        find_All_IDs(view);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        tv_welcome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PendingListActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        return view;
    }

    private void find_All_IDs(View view) {
        iv_back = view.findViewById(R.id.iv_back);
        tv_welcome1 = view.findViewById(R.id.tv_welcome1);
    }
}
