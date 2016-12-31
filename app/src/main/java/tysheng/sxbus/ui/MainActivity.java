package tysheng.sxbus.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.view.PositionBottomNavigationView;


public class MainActivity extends BaseActivity implements PositionBottomNavigationView.onPositionSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.bottom)
    PositionBottomNavigationView mBottom;
    private Fragment mCurrent, mSearch, mStar, mMore;
    private ArrayList<String> mList, mList0, mList1, mList2;

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
        outState.putStringArrayList("-1", mList);
        outState.putStringArrayList("0", mList0);
        outState.putStringArrayList("1", mList1);
        outState.putStringArrayList("2", mList2);
    }

    /**
     * restoreFragment for crash
     *
     * @param savedInstanceState savedInstanceState
     */
    private void restoreFragment(Bundle savedInstanceState) {
        mCurrent = getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString(TAG));
        mList = savedInstanceState.getStringArrayList("-1");
        mList0 = savedInstanceState.getStringArrayList("0");
        mList1 = savedInstanceState.getStringArrayList("1");
        mList2 = savedInstanceState.getStringArrayList("2");
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        } else {
            mList = new ArrayList<>();
            mList0 = new ArrayList<>();
            mList1 = new ArrayList<>();
            mList2 = new ArrayList<>();
        }
        mBottom.registerIds(R.id.menu_star, R.id.menu_search, R.id.menu_more);
        mBottom.setOnPositionSelectedListener(this);
        onPositionSelected(0);
        new RxPermissions(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            showSnackBar("没有这些权限可能会出现问题:(", true);
                        }
                    }
                });
    }

    public void showSnackBar(String msg, boolean isLong) {
        SnackBarUtil.show(mCoordinatorLayout, msg,
                isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        int pos = mBottom.getCurrentPosition();
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
                    mBottom.setCurrentPosition(0);
                } else {
                    remove(mList1);
                }
                break;
            case 2:
                if (mList2.size() == 1) {
                    mBottom.setCurrentPosition(0);
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

    @Override
    public void onPositionSelected(int position) {
        switch (position) {
            case 0:
                if (!(mCurrent instanceof StarFragment)) {
                    if (mStar == null) {
                        Fragment f0 = getSupportFragmentManager().findFragmentByTag("0");
                        if (f0 == null) {
                            mStar = Fragment.instantiate(this, StarFragment.class.getName());
                            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mStar, "0").commitNow();
                        } else {
                            mStar = f0;
                        }
                    }
                    if (ListUtil.isEmpty(mList0))
                        mList0.add(0, "0");
                    jumpFragment(mList, mList0, "0");
                    mCurrent = mStar;
                    mList = mList0;
                }
                break;
            case 1:
                if (!(mCurrent instanceof SearchFragment)) {
                    if (mSearch == null) {
                        Fragment f0 = getSupportFragmentManager().findFragmentByTag("1");
                        if (f0 == null) {
                            mSearch = Fragment.instantiate(this, SearchFragment.class.getName());
                            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mSearch, "1").commitNow();
                        } else {
                            mSearch = f0;
                        }
                    }
                    if (ListUtil.isEmpty(mList1))
                        mList1.add(0, "1");
                    jumpFragment(mList, mList1, "1");
                    mCurrent = mSearch;
                    mList = mList1;
                }
                break;
            case 2:
                if (!(mCurrent instanceof MoreFragment)) {
                    if (mMore == null) {
                        Fragment f0 = getSupportFragmentManager().findFragmentByTag("2");
                        if (f0 == null) {
                            mMore = Fragment.instantiate(this, MoreFragment.class.getName());
                            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, mMore, "2").commitNow();
                        } else {
                            mMore = f0;
                        }
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
}
