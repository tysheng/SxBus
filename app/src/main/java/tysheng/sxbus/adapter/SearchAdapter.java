package tysheng.sxbus.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.bean.Star;

/**
 * Created by Sty
 * Date: 16/8/10 22:07.
 */
public class SearchAdapter extends BaseQuickAdapter<Star> {
    public SearchAdapter(List<Star> data) {
        super(R.layout.item_bus_simple, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Star result) {
        holder.setText(R.id.textView, result.lineName + "  " + result.startStationName + "-->" + result.endStationName)
                .addOnClickListener(R.id.textView)
                .addOnClickListener(R.id.star)
                .setImageResource(R.id.star, R.drawable.star_no);
    }


}
