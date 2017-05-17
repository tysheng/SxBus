package tysheng.sxbus.presenter.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.ui.fragments.MoreFragment;
import tysheng.sxbus.ui.fragments.RunningFragment;
import tysheng.sxbus.ui.fragments.SearchFragment;
import tysheng.sxbus.ui.fragments.StarFragment;
import tysheng.sxbus.ui.inter.MainView;

/**
 * Created by tysheng
 * Date: 2017/1/3 15:08.
 * Email: tyshengsx@gmail.com
 */

public class MainPresenterImpl extends AbstractPresenter<MainView> {
    private static final String POSITION = "POSITION";
    private FragmentManager mFragmentManager;
    private int pre;// 0 ,1 ,2 ,3(0跳到running),4(1跳到running)

    @Inject
    public MainPresenterImpl(MainView mainView, FragmentManager manager) {
        super(mainView);
        mFragmentManager = manager;
    }

    @Override
    public void setArgs(Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    public void onDestroy() {
        mFragmentManager = null;
        super.onDestroy();
    }

    private Fragment getFragmentInstance(int pos, FragCallback callback) {
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

    public void restorePosition(Bundle savedInstanceState) {
        preToCur(savedInstanceState != null ? savedInstanceState.getInt(POSITION) : 0, null);
    }

    /**
     * fragment 的跳转
     *
     * @param cur      现在要跳转的 position
     * @param callback
     */
    public void preToCur(int cur, FragCallback callback) {
        Fragment preFrag = mFragmentManager.findFragmentByTag(String.valueOf(pre));
        Fragment to = mFragmentManager.findFragmentByTag(String.valueOf(cur));
        if (to == null) {
            to = getFragmentInstance(cur, callback);
        }
        mView.jumpFragment(preFrag, to, String.valueOf(cur));
        pre = cur;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION, pre);
    }

    public boolean onBackPressed() {
        boolean dispatch = true;
        if (pre == 3 || pre == 4) {
            mFragmentManager.beginTransaction()
                    .remove(mFragmentManager.findFragmentByTag(String.valueOf(pre)))
                    .show(mFragmentManager.findFragmentByTag(String.valueOf(pre -= 3)))
                    .commit();
            dispatch = false;
        } else if (pre != 0) {
            mView.setCurrentPosition(0);
            dispatch = false;
        }
        return dispatch;
    }

    public void onPositionSelected(int position) {
        int finalPos = position;
        if (position == 0 || position == 1) {
            if (mFragmentManager.findFragmentByTag(String.valueOf(position + 3)) != null) {
                finalPos += 3;
            }
        }
        preToCur(finalPos, null);
    }


}
