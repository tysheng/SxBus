package tysheng.sxbus.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import tysheng.sxbus.utils.LogUtil;

/**
 * Created by shengtianyang on 16/2/22.
 */
public abstract class BaseFragment extends RxFragment {
    protected View mRootView;
    protected Activity mActivity;
    private Unbinder mBinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(this.getTag() + hidden);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mBinder = ButterKnife.bind(this, mRootView);


        initData();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    protected void addFragment(@Nullable FragmentManager manager, @NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag, String backStackTag) {
        doAddFragment(manager, from, to, id, tag, backStackTag);
    }

    protected void addFragment(@Nullable FragmentManager manager, @NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag) {
        doAddFragment(manager, from, to, id, tag, null);
    }

    private void doAddFragment(@Nullable FragmentManager manager, @NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag, String backStackTag) {
        if (manager == null)
            manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .hide(from)
                .addToBackStack(backStackTag)
                .add(id, to, tag)
                .commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinder.unbind();
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

}
