package tysheng.sxbus.adapter;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.base.TyViewHolder;
import tysheng.sxbus.bean.OpenSourceInfo;

/**
 * Created by tysheng
 * Date: 2017/5/5 16:25.
 * Email: tyshengsx@gmail.com
 */

public class OpenSourceAdapter extends BaseRecyclerViewAdapter<OpenSourceInfo> {
    public OpenSourceAdapter() {
        super(R.layout.item_open_source, null);
    }

    @Override
    protected void convert(TyViewHolder holder, OpenSourceInfo item) {
        holder.setText(R.id.name, item.name)
                .setText(R.id.intro, item.intro);
        setListener(holder);
    }
}
