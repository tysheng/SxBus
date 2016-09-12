package tysheng.sxbus;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.ui.MoreFragment;
import tysheng.sxbus.ui.SearchFragment;
import tysheng.sxbus.ui.StarFragment;
import tysheng.sxbus.utils.SnackBarUtil;


public class MainActivity extends BaseActivity implements OnTabSelectListener {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    private FragmentManager mManager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mManager = getSupportFragmentManager();
        mBottomBar.setOnTabSelectListener(this);
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            SnackBarUtil.show(mCoordinatorLayout, "没有这些权限可能会出现问题:(", Snackbar.LENGTH_LONG);
                        }
                    }
                });
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.menu_search:
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, Fragment.instantiate(MainActivity.this, SearchFragment.class.getName()))
                        .commit();
                break;
            case R.id.menu_star:
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, Fragment.instantiate(MainActivity.this, StarFragment.class.getName()))
                        .commit();
                break;
            case R.id.menu_more:
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, Fragment.instantiate(MainActivity.this, MoreFragment.class.getName()))
                        .commit();
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBottomBar.getCurrentTabPosition() == 0) {
            super.onBackPressed();
        } else {
            mBottomBar.selectTabAtPosition(0);
        }

    }

}
