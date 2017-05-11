package tysheng.sxbus.base;

import android.view.View;

import java.util.List;


/**
 * Base adapter for BaseQuickAdapter
 * Attention: all method called after setAdapter().
 * Created by tysheng
 * Date: 16/9/18 09:18.
 * Email: tyshengsx@gmail.com
 */
public abstract class BaseRecyclerViewAdapter<T> extends BaseRecyclerAdapter<T, TyViewHolder> {

    public BaseRecyclerViewAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public BaseRecyclerViewAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected TyViewHolder createBaseViewHolder(View view) {
        return new TyViewHolder(view);
    }


}
