package tysheng.sxbus.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import rx.functions.Action1;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.utils.SnackBarUtil;


public class MainActivity extends BaseActivity implements OnTabSelectListener {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    private Fragment mCurrent;
    private Fragment mSearch;
    private Fragment mStar;
    private Fragment mMore;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mBottomBar.setOnTabSelectListener(this);
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            case R.id.menu_star:
                if (!(mCurrent instanceof StarFragment)) {
                    if (mStar == null)
                        mStar = Fragment.instantiate(this, StarFragment.class.getName());
                    jumpFragment(mCurrent, mStar, "0");
                    mCurrent = mStar;
                }
                break;
            case R.id.menu_search:
                if (!(mCurrent instanceof SearchFragment)) {
                    if (mSearch == null)
                        mSearch = Fragment.instantiate(this, SearchFragment.class.getName());
                    jumpFragment(mCurrent, mSearch, "1");
                    mCurrent = mSearch;
                }
                break;
            case R.id.menu_more:
                if (!(mCurrent instanceof MoreFragment)) {
                    if (mMore == null)
                        mMore = Fragment.instantiate(this, MoreFragment.class.getName());
                    jumpFragment(mCurrent, mMore, "2");
                    mCurrent = mMore;
                }
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
