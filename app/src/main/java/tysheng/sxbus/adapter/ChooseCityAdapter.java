package tysheng.sxbus.adapter;

import com.chad.library.adapter.base.util.MultiTypeDelegate;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.base.TyViewHolder;
import tysheng.sxbus.bean.CitySection;

/**
 * Created by Sty
 * Date: 16/9/15 10:20.
 */
public class ChooseCityAdapter extends BaseRecyclerViewAdapter<CitySection> {
    public ChooseCityAdapter() {
        super(null);
        setMultiTypeDelegate(new MultiTypeDelegate<CitySection>() {
            @Override
            protected int getItemType(CitySection citySection) {
                return citySection.type;
            }
        });
        getMultiTypeDelegate().registerItemTypeAutoIncrease(
                R.layout.item_city, R.layout.item_section_header
        );
    }

    @Override
    protected void convert(TyViewHolder holder, CitySection citySection) {
        switch (holder.getItemViewType()) {
            case 0:
                holder.addOnClickListener(R.id.textView);
                break;
            case 1:
                break;
            default:
                break;
        }
        holder.setText(R.id.textView, citySection.name);
    }


}
