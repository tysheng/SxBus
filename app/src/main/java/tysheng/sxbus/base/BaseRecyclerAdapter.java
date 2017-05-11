package tysheng.sxbus.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.utils.ListUtil;

/**
 * Created by tysheng
 * Date: 2017/5/11 15:14.
 * Email: tyshengsx@gmail.com
 */

public abstract class BaseRecyclerAdapter<T, Holder extends BaseViewHolder> extends BaseQuickAdapter<T, Holder> {

    private OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseRecyclerAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseRecyclerAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public void onEmptyView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
        setEmptyView(v);
    }

    public void onLoad(BaseQuickAdapter.RequestLoadMoreListener listener) {
        setLoadMoreView(new LoadView());
        setOnLoadMoreListener(listener);
    }

    public void onLoad(int size, BaseQuickAdapter.RequestLoadMoreListener listener) {
        setAutoLoadMoreSize(size);
        onLoad(listener);
    }

    public void onEnd() {
        loadMoreEnd();
    }

    public void setData(List<T> data, boolean enable) {
        setNewData(data);
        if (!enable) {
            setEnableLoadMore(false);
        } else {
            loadMoreComplete();
        }
    }

    /**
     * override the method, unEnable load more after the first time setData.
     *
     * @param data
     */
    public void setData(List<T> data) {
        setData(data, false);
    }

    /**
     * auto call loadMoreComplete() after addData().
     *
     * @param newData
     */
    @Override
    public void addData(List<T> newData) {
        super.addData(newData);
        loadMoreComplete();
    }

    /**
     * when remove all items , show empty view
     *
     * @param position
     */
    @Override
    public void remove(int position) {
        super.remove(position);
        if (ListUtil.isEmpty(mData))
            onEmpty();
    }

    /**
     * remove data between from and to, [min,max)
     *
     * @param from  pos
     * @param count count
     */
    public void remove(int from, int count) {
        for (int i = 0; i < count; i++) {
            mData.remove(from);
        }
        notifyItemRangeRemoved(from + getHeaderLayoutCount(), count);
        if (ListUtil.isEmpty(mData))
            onEmpty();
    }

    /**
     * update single item
     *
     * @param pos click position
     */
    public void updateSingleItem(int pos) {
        notifyItemChanged(pos + getHeaderLayoutCount());
    }


    public void onEmpty() {
        loadMoreComplete();
        setNewData(null);
        View view = getEmptyView();
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            view.findViewById(R.id.textView).setVisibility(View.VISIBLE);
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    public void onError() {
        loadMoreFail();
    }

    /**
     * clickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private boolean isEnabled(int viewType) {
        switch (viewType) {
            case HEADER_VIEW:
            case LOADING_VIEW:
            case FOOTER_VIEW:
            case EMPTY_VIEW:
                return false;
            default:
                return true;
        }
    }


    /**
     * only for the whole item
     * <p>
     * user in convert()
     *
     * @param viewHolder viewHolder
     * @see #convert(BaseViewHolder, Object)
     */
    protected void setListener(final TyViewHolder viewHolder) {
        setListener(viewHolder, false);
    }

    protected void setListener(final TyViewHolder viewHolder, boolean setLong) {
        if (!isEnabled(viewHolder.getItemViewType())) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getLayoutPosition() - getHeaderLayoutCount();
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        if (!setLong) return;
        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getLayoutPosition() - getHeaderLayoutCount();
                    return mOnItemClickListener.onItemLongClick(v, position);
                }
                return false;
            }
        });

    }

    /**
     * for child and whole item, notice: better to give an id to root view, and consider it a child view.
     * <p>
     * user in convert()
     *
     * @param viewHolder holder
     * @param ids        one or more
     * @see #convert(BaseViewHolder, Object)
     */
    protected void setChildListener(final TyViewHolder viewHolder, int... ids) {
        for (int id : ids) {
            setChildListener(viewHolder, id, false);
        }
    }

    protected void setChildListener(final TyViewHolder viewHolder, int id, boolean setLong) {
        if (!isEnabled(viewHolder.getItemViewType())) return;
        View view = viewHolder.getView(id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getLayoutPosition() - getHeaderLayoutCount();
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
        if (!setLong) return;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getLayoutPosition() - getHeaderLayoutCount();
                    return mOnItemClickListener.onItemLongClick(v, position);
                }
                return false;
            }
        });

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }

    private static class LoadView extends LoadMoreView {

        @Override
        public int getLayoutId() {
            return R.layout.item_recycler_loading_more_new;
        }

        @Override
        protected int getLoadingViewId() {
            return R.id.progressBar;
        }

        @Override
        protected int getLoadFailViewId() {
            return R.id.error;
        }

        @Override
        protected int getLoadEndViewId() {
            return R.id.end;
        }

    }

    public static class SimpleItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
        }

        @Override
        public boolean onItemLongClick(View view, int position) {
            return false;
        }
    }
}
