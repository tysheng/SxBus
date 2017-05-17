package tysheng.sxbus.base;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.di.component.UniverseComponent;
import tysheng.sxbus.utils.LogUtil;

/**
 * Created by shengtianyang on 16/2/22.
 */
public abstract class BaseFragment extends RxFragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(getTag() + "onCreate");
        initDagger();
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    protected abstract void initDagger();

    public UniverseComponent getUniverseComponent() {
        return ((BaseActivity) getActivity()).getUniverseComponent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    public <R> LifecycleTransformer<R> bindUntilDestroyView() {
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
    }

    public View getRootView() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getContext(), toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(getContext(), toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("onDestroyView " + toString());
    }


    protected void addFragment(@NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag, String backStackTag) {
        getFragmentManager()
                .beginTransaction()
                .hide(from)
//                .addToBackStack(backStackTag)
                .add(id, to, tag)
                .commit();
    }

    protected void addFragment(@NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag) {
        addFragment(from, to, id, tag, null);
    }

    public boolean onInterceptBackPressed() {
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    public interface FragmentCallback {
        void handleCallbackNew(FragCallback callback);
    }


}
