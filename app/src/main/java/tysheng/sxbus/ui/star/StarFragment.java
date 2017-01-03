package tysheng.sxbus.ui.star;

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
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.bean.Star;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private List<Star> mStarList;
    private StarAdapter mAdapter;
    private StarPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mStarList = mPresenter.getStarList();
            mAdapter.setNewData(mStarList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void initData() {
        mPresenter = new StarPresenter(this);
        mStarList = mPresenter.getStarList();
        doNext();
    }

    private void doNext() {
        mAdapter = new StarAdapter(mStarList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, mRecyclerView, false));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.number:
                    case R.id.textView:
                        ((FragmentCallback) getActivity()).handleCallbackNew(new FragCallback(Constant.WHAT_STAR, mAdapter.getItem(i).id,
                                mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                        break;
                    case R.id.star:
                        Star star = mAdapter.getItem(i);
                        mAdapter.remove(i);
                        mPresenter.deleteByKey(star.getMainId());
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
                mPresenter.dragEnd(mStarList);
                viewHolder.itemView.setPressed(false);
            }
        });
    }
}
