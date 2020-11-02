package com.example.myapplication.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.AddHealthInfo;
import com.example.myapplication.MainActivity;
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
    List<HealthHistoryBean> healthHistoryBeans;

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
        healthHistoryBeans = new HistoryDao(getContext()).queryTypesAll();
        adapter.setHealthHistoryBeans(healthHistoryBeans);
        adapter.setOnItemClick(new HistoryAdapter.OnItemClick() {
            @Override
            public void onLongClick(int position) {
                showDeleteDialog(position);
            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), AddHealthInfo.class);
                intent.putExtra("item",healthHistoryBeans.get(position));
                startActivity(intent);
            }
        });
        rvHistory.setAdapter(adapter);

    }

    /**
     * 显示删除弹框
     */
    private void showDeleteDialog(int position) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getContext());
        normalDialog.setTitle("删除");
        normalDialog.setMessage("是否要删除这条记录?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new HistoryDao(getContext()).DeleteNote(healthHistoryBeans.get(position).getId());
                        healthHistoryBeans = new HistoryDao(getContext()).queryTypesAll();
                        adapter.setHealthHistoryBeans(healthHistoryBeans);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();
    }
}
