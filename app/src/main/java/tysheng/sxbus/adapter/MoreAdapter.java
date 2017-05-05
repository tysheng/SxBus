package tysheng.sxbus.adapter;

import com.chad.library.adapter.base.util.MultiTypeDelegate;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.base.TyViewHolder;
import tysheng.sxbus.bean.More;

/**
 * Created by tysheng
 * Date: 2017/5/5 14:07.
 * Email: tyshengsx@gmail.com
 */

public class MoreAdapter extends BaseRecyclerViewAdapter<More> {
    public MoreAdapter() {
        super(null);
        setMultiTypeDelegate(new MultiTypeDelegate<More>() {
            @Override
            protected int getItemType(More more) {
                return more.type;
            }
        });
        getMultiTypeDelegate().registerItemTypeAutoIncrease(
                R.layout.item_more_has_sub,
                R.layout.item_more_simple
        );
    }

    @Override
    protected void convert(TyViewHolder holder, More item) {
        switch (holder.getItemViewType()) {
            case More.HAS_SUB:
                holder.setText(R.id.topSub, item.topSub);
                break;
            case More.SIMPLE:

                break;
            default:
                break;
        }
        holder.setText(R.id.mainTitle, item.main)
                .setVisible(R.id.divider, item.hasDivider);
        setListener(holder);
    }
}
