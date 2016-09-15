package tysheng.sxbus.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.bean.CitySection;

/**
 * Created by Sty
 * Date: 16/9/15 10:20.
 */
public class ChooseCityAdapter extends BaseSectionQuickAdapter<CitySection> {
    public ChooseCityAdapter(List<CitySection> data) {
        super(R.layout.item_city, R.layout.item_section_header, data);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, CitySection citySection) {
        baseViewHolder.setText(R.id.textView, citySection.header);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CitySection citySection) {
        baseViewHolder.setText(R.id.textView, citySection.t)
                .addOnClickListener(R.id.textView);
    }
}
