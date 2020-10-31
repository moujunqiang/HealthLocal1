package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.AddHealthInfo;
import com.example.myapplication.R;
import com.example.myapplication.adapter.HistoryAdapter;
import com.example.myapplication.bean.HealthHistoryBean;
import com.example.myapplication.db.HistoryDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryFragment extends Fragment {
    private View inflate;
    private RecyclerView rvHistory;
    private HistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history, container, false);
        initView();
        return inflate;
    }

    public static HistoryFragment newInstance() {

        Bundle args = new Bundle();
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    public void initView() {
        rvHistory = inflate.findViewById(R.id.rv_history);
        //跳转到添加
        inflate.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddHealthInfo.class));
            }
        });
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter();
        //数据库查询数据
        List<HealthHistoryBean> healthHistoryBeans = new HistoryDao(getContext()).queryTypesAll();
        adapter.setHealthHistoryBeans(healthHistoryBeans);
        rvHistory.setAdapter(adapter);

    }
}
