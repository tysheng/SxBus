package tysheng.sxbus.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.databinding.FragmentStarBinding;
import tysheng.sxbus.presenter.impl.StarPresenterImpl;
import tysheng.sxbus.ui.inter.StarView;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseFragmentV2<StarPresenterImpl, FragmentStarBinding> implements StarView {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.setNewDataFromRecent();
        }
    }

    @Override
    protected StarPresenterImpl initPresenter() {
        return new StarPresenterImpl(this);
    }

    @Override
    protected void initData() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.bindAdapter(binding.recyclerView);
    }
}
