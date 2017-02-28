package tysheng.sxbus.presenter.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.trello.rxlifecycle2.LifecycleTransformer;

import tysheng.sxbus.ui.base.BaseView;

/**
 * Created by tysheng
 * Date: 2017/2/28 15:50.
 * Email: tyshengsx@gmail.com
 */

public abstract class AbstractPresenter<T extends BaseView> implements BaseFragmentPresenter {
    protected T mView;

    public AbstractPresenter(T view) {
        mView = view;
    }

    @Override
    public Activity getActivity() {
        return mView.getActivity();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public <R> LifecycleTransformer<R> bindUntilDestroyView() {
        return mView.bindUntilDestroyView();
    }

    @Override
    public void startActivity(Intent intent) {
        mView.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mView.startActivityForResult(intent, requestCode);
    }

    @Override
    public Context getContext() {
        return mView.getContext();
    }

    @Override
    public FragmentManager getChildFragmentManager() {
        return mView.getChildFragmentManager();
    }


}
