package tysheng.sxbus.ui;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.view.PositionBottomNavigationView;


public class MainActivity extends BaseActivity implements PositionBottomNavigationView.onPositionSelectedListener, BaseFragment.FragmentCallback {
    public static final String POSITION = "POSITION";
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.bottom)
    PositionBottomNavigationView mBottom;
    private int pre;// 0 ,1 ,2 ,3,4

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(POSITION, pre);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    /**
     * restoreFragment for crash
     *
     * @param savedInstanceState savedInstanceState
     */
    private void restoreFragment(Bundle savedInstanceState) {
        int pos = savedInstanceState.getInt(POSITION);
        preToCur(pos, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        } else {
            preToCur(0, null);
        }
        LogUtil.d("initData" + (savedInstanceState != null));

        mBottom.registerIds(R.id.menu_star, R.id.menu_search, R.id.menu_more);
        mBottom.setOnPositionSelectedListener(this);

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
        SnackBarUtil.show(mBottom, msg,
                isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        if (pre == 3 || pre == 4) {
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentByTag(String.valueOf(pre)))
                    .show(getSupportFragmentManager().findFragmentByTag(String.valueOf(pre -= 3)))
                    .commit();
        } else if (pre != 0) {
            mBottom.setCurrentPosition(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPositionSelected(int position) {
        int finalPos = position;
        if (position == 0 || position == 1) {
            if (getSupportFragmentManager().findFragmentByTag(String.valueOf(position + 3)) != null) {
                finalPos += 3;
            }
        }
        preToCur(finalPos, null);
    }

    private void preToCur(int cur, FragCallback callback) {
        Fragment preFrag = getSupportFragmentManager().findFragmentByTag(String.valueOf(pre));
        Fragment to = getSupportFragmentManager().findFragmentByTag(String.valueOf(cur));
        if (to == null) {
            to = get(cur, callback);
        }
        jumpFragment(preFrag, to, String.valueOf(cur));
        pre = cur;
    }

    private Fragment get(int pos, FragCallback callback) {
        if (callback == null) {
            if (pos == 0) {
                return new StarFragment();
            } else if (pos == 1) {
                return new SearchFragment();
            } else {
                return new MoreFragment();
            }
        } else {
            return RunningFragment.newFragment(callback.arg1, callback.arg2);
        }
    }

    @Override
    public void onPositionReselected(int position) {

    }

    @Override
    public void handleCallbackNew(FragCallback callback) {
        preToCur(callback.what, callback);
    }
}
