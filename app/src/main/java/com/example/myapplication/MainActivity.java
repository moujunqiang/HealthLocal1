package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.adapter.BaseFragmentAdapter;
import com.example.myapplication.fragment.HistoryFragment;
import com.example.myapplication.fragment.NewsFragment;
import com.example.myapplication.utils.DoubleClickHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ViewPager mViewPager;
    BottomNavigationView mBottomNavigationView;
    private BaseFragmentAdapter<Fragment> mPagerAdapter;
    private Fragment fragment;
    private HistoryFragment fragment1;
    private NewsFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    protected void initView() {
        mBottomNavigationView = findViewById(R.id.bv_home_navigation);
        mViewPager = findViewById(R.id.vp_home_pager);
        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        fragment1 = HistoryFragment.newInstance();
        mPagerAdapter.addFragment(fragment1);
        fragment2 = NewsFragment.newInstance();
        mPagerAdapter.addFragment(fragment2);

        mViewPager.setAdapter(mPagerAdapter);

        // 限制页面数量
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
    }

    /**
     * {@link BottomNavigationView.OnNavigationItemSelectedListener}
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mPagerAdapter.setCurrentItem(HistoryFragment.class);
                fragment = fragment1;
                return true;
            case R.id.home_me:
                mPagerAdapter.setCurrentItem(NewsFragment.class);
                fragment = fragment2;
                return true;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (fragment instanceof NewsFragment) {
            fragment2.goBack();
        }else {
            if (DoubleClickHelper.isOnDoubleClick()) {
                // 移动到上一个任务栈，避免侧滑引起的不良反应
                moveTaskToBack(false);
                System.exit(0);

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}