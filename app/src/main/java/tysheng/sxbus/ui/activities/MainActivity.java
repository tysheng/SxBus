package tysheng.sxbus.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.baidu.mapapi.SDKInitializer;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivityV2;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.databinding.ActivityMainBinding;
import tysheng.sxbus.presenter.impl.MainPresenterImpl;
import tysheng.sxbus.view.PositionBottomNavigationView;


public class MainActivity extends BaseActivityV2<MainPresenterImpl, ActivityMainBinding> implements MainView, PositionBottomNavigationView.onPositionSelectedListener, BaseFragment.FragmentCallback {
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenterImpl initPresenter() {
        return new MainPresenterImpl(this, getSupportFragmentManager());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Main);
        super.onCreate(savedInstanceState);
        // 初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.restorePosition(savedInstanceState);
        binding.bottom.registerIds(R.id.menu_star, R.id.menu_search, R.id.menu_more);
        binding.bottom.setOnPositionSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void setCurrentPosition(int pos) {
        binding.bottom.setCurrentPosition(pos);
    }

    @Override
    public void onPositionSelected(int position) {
        mPresenter.onPositionSelected(position);
    }

    @Override
    public void jumpFragment(Fragment from, Fragment to, String tag) {
        super.jumpFragment(from, to, tag);
    }

    @Override
    public void onPositionReselected(int position) {

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void handleCallbackNew(FragCallback callback) {
        mPresenter.preToCur(callback.what, callback);
    }
}
