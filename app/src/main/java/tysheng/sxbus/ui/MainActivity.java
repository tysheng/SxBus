package tysheng.sxbus.ui;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.SnackBarUtil;


public class MainActivity extends BaseActivity implements OnTabSelectListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    private Fragment mCurrent, mSearch, mStar, mMore;
    private List<String> mList, mList0, mList1, mList2;

    public void addTag(int which, String tag) {
        switch (which) {
            case 0:
                mList0.add(0, tag);
                break;
            case 1:
                mList1.add(0, tag);
                break;
            case 2:
                mList2.add(0, tag);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG, mCurrent.getTag());
    }

    /**
     * restoreFragment for crash
     *
     * @param savedInstanceState savedInstanceState
     */
    private void restoreFragment(Bundle savedInstanceState) {
        mCurrent = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(TAG));
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        }
        mList = new ArrayList<>();
        mList0 = new ArrayList<>();
        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();
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
                    if (mStar == null) {
                        mStar = Fragment.instantiate(this, StarFragment.class.getName());
                        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mStar, "0").commitNow();

                    }
                    if (ListUtil.isEmpty(mList0))
                        mList0.add(0, "0");
                    jumpFragment(mList, mList0, "0");
                    mCurrent = mStar;
                    mList = mList0;
                }
                break;
            case R.id.menu_search:
                if (!(mCurrent instanceof SearchFragment)) {
                    if (mSearch == null) {
                        mSearch = Fragment.instantiate(this, SearchFragment.class.getName());
                        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mSearch, "1").commitNow();
                    }
                    if (ListUtil.isEmpty(mList1))
                        mList1.add(0, "1");
                    jumpFragment(mList, mList1, "1");
                    mCurrent = mSearch;
                    mList = mList1;
                }
                break;
            case R.id.menu_more:
                if (!(mCurrent instanceof MoreFragment)) {
                    if (mMore == null) {
                        mMore = Fragment.instantiate(this, MoreFragment.class.getName());
                        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mMore, "2").commitNow();
                    }
                    if (ListUtil.isEmpty(mList2))
                        mList2.add(0, "2");
                    jumpFragment(mList, mList2, "2");
                    mCurrent = mMore;
                    mList = mList2;
                }
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int pos = mBottomBar.getCurrentTabPosition();
        switch (pos) {
            case 0:
                if (mList0.size() == 1) {
                    super.onBackPressed();
                } else {
                    remove(mList0);
                }
                break;
            case 1:
                if (mList1.size() == 1) {
                    mBottomBar.selectTabAtPosition(0);
                } else {
                    remove(mList1);
                }
                break;
            case 2:
                if (mList2.size() == 1) {
                    mBottomBar.selectTabAtPosition(0);
                } else {
                    remove(mList2);
                }
                break;
            default:
                break;
        }


    }

    private void remove(List<String> list) {
        FragmentManager manager = getSupportFragmentManager();
        String remove = list.remove(0);
        String show = list.get(0);
        manager.beginTransaction()
                .remove(manager.findFragmentByTag(remove))
                .show(manager.findFragmentByTag(show))
                .commitNow();
    }

}
