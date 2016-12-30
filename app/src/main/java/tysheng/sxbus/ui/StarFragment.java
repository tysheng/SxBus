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
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.db.DbUtil;
import tysheng.sxbus.db.StarHelper;

import static tysheng.sxbus.R.id.star;

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
    private StarHelper mHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mStarList = getStarList();
            mAdapter.setNewData(mStarList);
        }
    }

    List<Star> getStarList() {
        return mHelper.queryBuilder()
                .where(StarDao.Properties.TableName.eq(Constant.STAR))
                .list();
    }

    @Override
    protected void initData() {
        mHelper = DbUtil.getDriverHelper();
        mStarList = getStarList();
        doNext();
    }

    @Override
    protected void addFragment(@NonNull Fragment from, @NonNull Fragment to, @IdRes int id, String tag) {
        super.addFragment(from, to, id, tag);
        ((MainActivity) getActivity()).addTag(0, tag);
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
                    case star:
                        Star star = mAdapter.getItem(i);
                        mAdapter.remove(i);
                        mHelper.deleteByKey(star.getMainId());
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
                mHelper.delete(getStarList());
                mHelper.save(mStarList);
                viewHolder.itemView.setPressed(false);
            }
        });
    }

}
