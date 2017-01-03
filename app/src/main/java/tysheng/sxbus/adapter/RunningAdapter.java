package tysheng.sxbus.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.base.StyBaseViewHolder;
import tysheng.sxbus.bean.Stations;


/**
 * Created by Sty
 * Date: 16/8/10 22:28.
 */
public class RunningAdapter extends BaseRecyclerViewAdapter<Stations> {
    public RunningAdapter() {
        super(R.layout.item_running, null);
    }

    @Override
    protected void convert(StyBaseViewHolder holder, Stations bean) {
        TextView textView = holder.getView(R.id.textView);
        TextView going = holder.getView(R.id.going);
        if (TextUtils.equals("a", bean.updateTime)) {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            going.setVisibility(View.VISIBLE);
        } else {
            going.setVisibility(View.INVISIBLE);
            textView.setTextColor(Color.BLACK);
        }
        textView.setText(bean.stationName);
    }
}
