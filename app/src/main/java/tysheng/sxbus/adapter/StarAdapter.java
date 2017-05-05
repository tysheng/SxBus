package tysheng.sxbus.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.base.TyViewHolder;
import tysheng.sxbus.bean.Star;

/**
 * Created by Sty
 * Date: 16/8/10 22:07.
 */
public class StarAdapter extends BaseItemDraggableAdapter<Star, TyViewHolder> {
    public StarAdapter(List<Star> data) {
        super(R.layout.item_star, data);
    }

    public StarAdapter() {
        super(R.layout.item_star, null);
    }

    @Override
    protected void convert(TyViewHolder holder, Star bean) {
        holder.setText(R.id.textView, bean.startStationName + " - " + bean.endStationName)
                .setText(R.id.number, bean.lineName)
                .setImageResource(R.id.star, bean.isStar ? R.drawable.star_yes : R.drawable.star_no);
        holder.addOnClickListeners(R.id.textView, R.id.number, R.id.star);
    }

    @Override
    protected TyViewHolder createBaseViewHolder(View view) {
        return new TyViewHolder(view);
    }
}
