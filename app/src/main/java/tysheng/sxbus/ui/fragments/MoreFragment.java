package tysheng.sxbus.ui.fragments;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.presenter.impl.MorePresenterImpl;
import tysheng.sxbus.ui.inter.MoreView;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by Sty
 * Date: 16/9/11 20:23.
 */
public class MoreFragment extends BaseFragment<MorePresenterImpl> implements MoreView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

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
        mToolbar.setTitle(mPresenter.getTitle());
    }

    @Override
    public void snackBarShow(String s) {
        SnackBarUtil.show(mCoordinatorLayout, s, Snackbar.LENGTH_SHORT);
    }

    @OnClick({R.id.feedback, R.id.donate, R.id.check_update, R.id.chooseCity, R.id.stationMode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback:
                mPresenter.feedback();
                break;
            case R.id.donate:
                mPresenter.donate();
                break;
            case R.id.check_update:
                mPresenter.checkVersionByBaidu();
                break;
            case R.id.chooseCity:
                mPresenter.chooseCity();
                break;
            case R.id.stationMode:
                mPresenter.setStationMode();
                break;
        }
    }
}
