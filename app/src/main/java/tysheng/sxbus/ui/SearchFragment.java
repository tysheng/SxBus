package tysheng.sxbus.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import butterknife.BindString;
import butterknife.BindView;
import rx.Subscriber;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.SearchAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.BusLinesSimple;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.net.HttpUtil;
import tysheng.sxbus.presenter.StarUtil;
import tysheng.sxbus.utils.KeyboardUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by Sty
 * Date: 16/8/10 22:52.
 */
public class SearchFragment extends BaseFragment {

    @BindString(R.string.recent)
    String mRecent;
    @BindString(R.string.result)
    String mResult;
    @BindString(R.string.search_error)
    String searchError;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    private Stars mStars;
    private SearchAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initData() {
        mStars = StarUtil.initStars(mStars);
        mAdapter = new SearchAdapter(mStars.result);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.textView:
                        startActivity(RunningActivity.newIntent(getContext(), mAdapter.getItem(i).id
                                , mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName)
                        );
                        break;
                    case R.id.star:
                        mStars.result.add(mAdapter.getItem(i));
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(R.drawable.star_yes);
                        StarUtil.onStopSave(mStars);
                        break;
                    default:
                        break;
                }
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtil.d(query);
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.onActionViewExpanded();

    }

    private void getBusSimple(int number) {
        add(HttpUtil.convert(BusRetrofit.get().numberToSearch(number))
                .subscribe(new Subscriber<BusLinesSimple>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("search   " + e.getMessage());
                        SnackBarUtil.show(mCoordinatorLayout, searchError);

                    }

                    @Override
                    public void onNext(BusLinesSimple busLinesSimple) {
                        LogUtil.d(JSON.toJSONString(busLinesSimple));
                        mAdapter.setNewData(busLinesSimple.result.result);
                    }
                }));
    }

    private void search(String s) {
        KeyboardUtil.hide(mActivity);
        int number;
        number = Integer.valueOf(s);
        getBusSimple(number);
    }

}
