package tysheng.sxbus.ui.fragments;

import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.view.View;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.adapter.MoreAdapter;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.bean.More;
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
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {
        binding.recyclerView.setHasFixedSize(true);
        binding.setAdapter(new MoreAdapter());
        binding.getAdapter().bindToRecyclerView(binding.recyclerView);
        binding.getAdapter().setOnItemClickListener(new BaseRecyclerViewAdapter.SimpleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                mPresenter.onItemClick(binding.getAdapter().getData().get(position).internalPosition);
            }
        });
        mPresenter.initData();
    }

    @Override
    public void setTitle(SpannableString title) {
        binding.toolbar.setTitle(title);
    }

    @Override
    public void snackBarShow(String s) {
        SnackBarUtil.show(binding.getRoot(), s, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void setMoreList(List<More> list) {
        binding.getAdapter().setData(list);
    }
}
