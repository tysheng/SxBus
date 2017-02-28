package tysheng.sxbus.presenter.impl;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.model.impl.DbModel;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.StarPresenterInterface;
import tysheng.sxbus.ui.inter.StarView;

/**
 * Created by tysheng
 * Date: 2017/1/3 15:00.
 * Email: tyshengsx@gmail.com
 */

public class StarPresenterImpl extends AbstractPresenter<StarView> implements StarPresenterInterface, OnItemDragListener {
    private DbModel mDbModel;
    private StarAdapter mAdapter;

    public StarPresenterImpl(StarView view) {
        super(view);
        mDbModel = new DbModel(this);
    }

    private List<Star> getStarList() {
        return mDbModel.getStarList();
    }

    @Override
    public void setArgs(Bundle bundle) {

    }

    @Override
    public void initData() {
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mView.getRecyclerView());
        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
        mAdapter.setOnItemDragListener(this);
    }

    private void deleteByKey(Long mainId) {
        mDbModel.deleteByKey(mainId);
    }

    private void dragEnd() {
        mDbModel.dragEnd();
    }

    @Override
    public void setNewDataFromRecent() {
        mAdapter.setNewData(getStarList());
    }

    @Override
    public StarAdapter getAdapter() {
        return mAdapter = new StarAdapter(getStarList());
    }

    @Override
    public void onSimpleItemChildClick(View view, int i) {
        switch (view.getId()) {
            case R.id.number:
            case R.id.textView:
                ((BaseFragment.FragmentCallback) getActivity()).handleCallbackNew(new FragCallback(Constant.WHAT_STAR, mAdapter.getItem(i).id,
                        mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                break;
            case R.id.star:
                Star star = mAdapter.getItem(i);
                mAdapter.remove(i);
                deleteByKey(star.getMainId());
            default:
                break;
        }
    }

    public void setEmptyView() {
        mAdapter.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, mView.getRecyclerView(), false));
    }

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
}
