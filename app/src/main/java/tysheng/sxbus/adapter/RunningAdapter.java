package tysheng.sxbus.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.bean.BusLinesResult;


/**
 * Created by Sty
 * Date: 16/8/10 22:28.
 */
public class RunningAdapter extends BaseQuickAdapter<BusLinesResult.StationsEntity> {
    public RunningAdapter(List<BusLinesResult.StationsEntity> data) {
        super(R.layout.item_running, data);
    }
    protected int color = Color.parseColor("#2196F3");

    @Override
    protected void convert(BaseViewHolder holder, BusLinesResult.StationsEntity result) {
        TextView textView = holder.getView(R.id.textView);
        TextView going = holder.getView(R.id.going);
        if (TextUtils.equals("a", result.updateTime) ){
            textView.setTextColor(color);
            going.setVisibility(View.VISIBLE);
        }else{
            going.setVisibility(View.INVISIBLE);
            textView.setTextColor(Color.BLACK);
        }
        textView.setText(result.stationName);
    }

}
