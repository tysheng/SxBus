package tysheng.sxbus.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.db.DbUtil;
import tysheng.sxbus.db.StarHelper;

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
    private QueryBuilder<Star> mQueryBuilder;

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
        if (mQueryBuilder == null)
            mQueryBuilder = mHelper.queryBuilder()
                    .where(StarDao.Properties.TableName.eq(Constant.STAR), StarDao.Properties.IsStar.eq("TRUE"))
                    .orderAsc(StarDao.Properties.SortId);
        return mQueryBuilder
                .list();
    }

    @Override
    protected void initData() {
        mHelper = DbUtil.getDriverHelper();
        mStarList = getStarList();
        doNext();
    }

    private void doNext() {
        mAdapter = new StarAdapter(0, mStarList);
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
                        ((FragmentCallback) getActivity()).handleCallbackNew(new FragCallback(Constant.WHAT_STAR, mAdapter.getItem(i).id,
                                mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                        break;
                    case R.id.star:
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
                dragEnd();
                viewHolder.itemView.setPressed(false);
            }
        });
    }

    void dragEnd() {
        mHelper.delete(getStarList());
        for (int i = 0; i < mStarList.size(); i++) {
            Star star = mStarList.get(i);
            star.setSortId((long) i);
            mHelper.save(star);
        }
    }


}
