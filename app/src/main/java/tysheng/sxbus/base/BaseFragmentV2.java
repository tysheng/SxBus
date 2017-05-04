package tysheng.sxbus.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tysheng.sxbus.presenter.base.AbstractPresenter;

/**
 * Created by tysheng
 * Date: 2017/5/3 09:52.
 * Email: tyshengsx@gmail.com
 */

public abstract class BaseFragmentV2<T extends AbstractPresenter, Binding extends ViewDataBinding> extends BaseFragment<T> {
    protected Binding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initData();
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public View getRootView() {
        return binding.getRoot();
    }
}
