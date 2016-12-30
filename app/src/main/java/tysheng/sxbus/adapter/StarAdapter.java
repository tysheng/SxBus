package tysheng.sxbus.adapter;


import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.bean.Star;

/**
 * Created by Sty
 * Date: 16/8/10 22:07.
 */
public class StarAdapter extends BaseItemDraggableAdapter<Star> {
    private int type;

    public StarAdapter(int type, List<Star> data) {
        super(R.layout.item_star, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, Star result) {
        holder.setText(R.id.textView, result.startStationName + " - " + result.endStationName)
                .setText(R.id.number, result.lineName)
                .setImageResource(R.id.star, result.isStar ? R.drawable.star_yes : R.drawable.star_no)
                .addOnClickListener(R.id.textView)
                .addOnClickListener(R.id.number)
                .addOnClickListener(R.id.star);
//        if (type == 0)
//            result.setSortId((long) holder.getAdapterPosition());
    }


}
