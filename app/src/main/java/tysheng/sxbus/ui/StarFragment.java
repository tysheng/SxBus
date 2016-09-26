package tysheng.sxbus.ui;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.List;

import butterknife.BindView;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseTabFragment;
import tysheng.sxbus.bean.FragmentTag;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.presenter.StarUtil;
import tysheng.sxbus.utils.RxBus;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.StySubscriber;
import tysheng.sxbus.utils.rxfastcache.RxFastCache;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseTabFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private List<Star> mStarList;
    private StarAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            RxFastCache.getArray(Constant.STAR, Star.class)
                    .compose(RxHelper.<List<Star>>ioToMain())
                    .subscribe(new StySubscriber<List<Star>>() {
                        @Override
                        public void next(List<Star> stars) {
                            mStarList = stars;
                            mAdapter.setNewData(mStarList);
                        }
                    });
        }
    }

    @Override
    protected void initData() {
        RxFastCache.getArray(Constant.STAR, Star.class)
                .compose(RxHelper.<List<Star>>ioToMain())
                .subscribe(new StySubscriber<List<Star>>() {
                    @Override
                    public void next(List<Star> stars) {
                        mStarList = stars;
                    }

                    @Override
                    public void onCompleted() {
                        doNext();
                    }
                });
    }

    @Override
    protected void addFragment(@NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag) {
        super.addFragment(from, to, id, tag);
        RxBus.getDefault().post(new FragmentTag(0, tag));
    }

    private void doNext() {
        mAdapter = new StarAdapter(mStarList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        View view = LayoutInflater.from(mActivity).inflate(R.layout.empty_layout, mRecyclerView, false);
        mAdapter.setEmptyView(view);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.number:
                    case R.id.textView:
                        addFragment(getFragmentManager().findFragmentByTag("0"), RunningFragment.newFragment(mAdapter.getItem(i).id,
                                mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName), R.id.frameLayout, "0_1");
                        break;
                    case R.id.star:
                        mAdapter.remove(i);
                        StarUtil.saveStarList(Constant.STAR, mStarList);
                    default:
                        break;
                }
            }
        });
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder viewHolder, int i, RecyclerView.ViewHolder viewHolder1, int i1) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int i) {
                StarUtil.saveStarList(Constant.STAR, mStarList);
                viewHolder.itemView.setPressed(false);
            }
        });
    }

}
