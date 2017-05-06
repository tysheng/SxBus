package tysheng.sxbus.presenter.impl;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.List;

import javax.inject.Inject;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.di.component.DaggerStarComponent;
import tysheng.sxbus.di.module.StarModule;
import tysheng.sxbus.model.impl.DbModelImplImpl;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.StarPresenter;
import tysheng.sxbus.ui.inter.StarView;

/**
 * Created by tysheng
 * Date: 2017/1/3 15:00.
 * Email: tyshengsx@gmail.com
 */

public class StarPresenterImpl extends AbstractPresenter<StarView> implements StarPresenter, OnItemDragListener {
    @Inject
    DbModelImplImpl mDbModel;
    @Inject
    StarAdapter mAdapter;

    public StarPresenterImpl(StarView view) {
        super(view);
        DaggerStarComponent.builder()
                .universeComponent(getUniverseComponent())
                .starModule(new StarModule())
                .build()
                .inject(this);
    }

    private List<Star> getStarList() {
        return mDbModel.getStarList();
    }

    @Override
    public void setArgs(Bundle bundle) {

    }

    @Override
    public void initData() {

    }

    private void deleteByKey(Long mainId) {
        mDbModel.deleteByKey(mainId);
    }

    @Override
    public void setNewDataFromRecent() {
        mAdapter.setNewData(getStarList());
    }

    @Override
    public void onSimpleItemChildClick(View view, int i) {
        final Star star = mAdapter.getItem(i);
        switch (view.getId()) {
            case R.id.number:
            case R.id.textView:
                ((BaseFragmentV2.FragmentCallback) getActivity())
                        .handleCallbackNew(new FragCallback(
                                Constant.WHAT_STAR, star.id, star.lineName + getString(R.string.go_to) + star.endStationName
                        ));
                break;
            case R.id.star:
                mAdapter.remove(i);
                deleteByKey(star.getMainId());
            default:
                break;
        }
    }

    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder viewHolder, int i, RecyclerView.ViewHolder viewHolder1, int i1) {

    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int i) {
        mDbModel.dragEnd(mAdapter.getData());
        viewHolder.itemView.setPressed(false);
    }

    public void bindAdapter(RecyclerView recyclerView) {
        mAdapter.bindToRecyclerView(recyclerView);
        setNewDataFromRecent();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                onSimpleItemChildClick(view, position);
            }
        });
        mAdapter.setEmptyView(R.layout.empty_layout);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
        mAdapter.setOnItemDragListener(this);
    }
}
