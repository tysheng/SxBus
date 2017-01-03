package tysheng.sxbus.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseStySectionAdapter;
import tysheng.sxbus.base.StyBaseViewHolder;
import tysheng.sxbus.bean.CitySection;

/**
 * Created by Sty
 * Date: 16/9/15 10:20.
 */
public class ChooseCityAdapter extends BaseStySectionAdapter<CitySection> {
    public ChooseCityAdapter(List<CitySection> data) {
        super(R.layout.item_city, R.layout.item_section_header, data);
    }

    @Override
    protected void convert(StyBaseViewHolder holder, CitySection citySection) {
        holder.setText(R.id.textView, citySection.t)
                .addOnClickListener(R.id.textView);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, CitySection citySection) {
        baseViewHolder.setText(R.id.textView, citySection.header);

    }

}
