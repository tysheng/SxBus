package tysheng.sxbus.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import butterknife.BindView;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.presenter.StarUtil;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Stars mStars;
    private StarAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void initData() {
        mStars = StarUtil.initStars(mStars);
        mAdapter = new StarAdapter(mStars.result);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.number:
                    case R.id.textView:
                        startActivity(RunningActivity.newIntent(getContext(), mAdapter.getItem(i).id,
                                mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                        break;
                    case R.id.star:
                        mAdapter.remove(i);
                        StarUtil.onStopSave(mStars);
                    default:
                        break;
                }
            }
        });
    }

}
