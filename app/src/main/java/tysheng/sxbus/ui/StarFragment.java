package tysheng.sxbus.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Subscriber;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.utils.fastcache.FastCache;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseFragment {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Stars mStars;
    private StarAdapter mAdapter;

    public static StarFragment newInstance() {
        return new StarFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initData() {
        getCache();

        mAdapter = new StarAdapter(mStars.result);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mActivity));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.textView:
                        startActivity(RunningActivity.newIntent(getContext(), mAdapter.getItem(i).id));
                        break;
                    case R.id.star:
                        mAdapter.remove(i);
                    default:
                        break;
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCache();
                mAdapter.setNewData(mStars.result);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mStars != null && mStars.result.size() != 0)
            FastCache.putAsync(Constant.STAR, mStars)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {

                        }
                    });
    }

    private void getCache() {
        try {
            mStars = FastCache.get(Constant.STAR, Stars.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStars == null) {
            mStars = new Stars();
            mStars.result = new ArrayList<>();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
