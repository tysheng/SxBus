package tysheng.sxbus.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.ui.MoreFragment;
import tysheng.sxbus.ui.running.RunningFragment;
import tysheng.sxbus.ui.search.SearchFragment;
import tysheng.sxbus.ui.star.StarFragment;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by tysheng
 * Date: 2017/1/3 15:08.
 * Email: tyshengsx@gmail.com
 */

class MainPresenter {
    private static final String POSITION = "POSITION";
    private MainActivity mActivity;
    private FragmentManager mFragmentManager;
    private int pre;// 0 ,1 ,2 ,3,4

    MainPresenter(MainActivity activity) {
        mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
    }

    public void onDestroy() {
        mFragmentManager = null;
        mActivity = null;
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

    void restoreFragment(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt(POSITION);
            preToCur(pos, null);
        } else {
            preToCur(0, null);
        }
    }

    void preToCur(int cur, FragCallback callback) {
        Fragment preFrag = mFragmentManager.findFragmentByTag(String.valueOf(pre));
        Fragment to = mFragmentManager.findFragmentByTag(String.valueOf(cur));
        if (to == null) {
            to = get(cur, callback);
        }
        mActivity.jumpFragment(preFrag, to, String.valueOf(cur));
        pre = cur;
    }

    void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION, pre);
    }

    boolean onBackPressed() {
        boolean dispatch = true;
        if (pre == 3 || pre == 4) {
            mFragmentManager.beginTransaction()
                    .remove(mFragmentManager.findFragmentByTag(String.valueOf(pre)))
                    .show(mFragmentManager.findFragmentByTag(String.valueOf(pre -= 3)))
                    .commit();
            dispatch = false;
        } else if (pre != 0) {
            mActivity.setCurrentPosition(0);
            dispatch = false;
        }
        return dispatch;
    }

    void onPositionSelected(int position) {
        int finalPos = position;
        if (position == 0 || position == 1) {
            if (mFragmentManager.findFragmentByTag(String.valueOf(position + 3)) != null) {
                finalPos += 3;
            }
        }
        preToCur(finalPos, null);
    }

    void askPermission(final View v) {
        new RxPermissions(mActivity)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            SnackBarUtil.show(v, "没有这些权限可能会出现问题:(", Snackbar.LENGTH_LONG);
                        }
                    }
                });
    }
}
