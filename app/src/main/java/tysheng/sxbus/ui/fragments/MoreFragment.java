package tysheng.sxbus.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.databinding.FragmentMoreBinding;
import tysheng.sxbus.presenter.impl.MorePresenterImpl;
import tysheng.sxbus.ui.inter.MoreView;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by Sty
 * Date: 16/9/11 20:23.
 */
public class MoreFragment extends BaseFragmentV2<MorePresenterImpl, FragmentMoreBinding> implements MoreView {

    @Override
    protected MorePresenterImpl initPresenter() {
        return new MorePresenterImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setPresenter(mPresenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {
        binding.toolbar.setTitle(mPresenter.getTitle());
    }

    @Override
    public void snackBarShow(String s) {
        SnackBarUtil.show(binding.getRoot(), s, Snackbar.LENGTH_SHORT);
    }
}
