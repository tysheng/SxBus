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

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shengtianyang on 16/2/22.
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected Activity mActivity;
    private CompositeSubscription mSubscription;
    private Unbinder mBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mBinder = ButterKnife.bind(this, mRootView);
        mSubscription = new CompositeSubscription();

        initData();
        return mRootView;
    }

    protected void add(Subscription s) {
        if (this.mSubscription == null) {
            this.mSubscription = new CompositeSubscription();
        }

        this.mSubscription.add(s);
    }

    protected void addFragment(@Nullable FragmentManager manager, @NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag, String backStackTag){
        doAddFragment(manager, from, to, id, tag, backStackTag);
    }
    protected void addFragment(@Nullable FragmentManager manager, @NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag){
        doAddFragment(manager, from, to, id, tag,null);
    }

    private void doAddFragment(@Nullable FragmentManager manager, @NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag, String backStackTag) {
        if (manager==null)
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
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }
    protected abstract int getLayoutId();
    protected abstract void initData();

}
