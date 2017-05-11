package tysheng.sxbus.adapter;

import tysheng.sxbus.R;
import tysheng.sxbus.bean.OpenSourceInfo;
import tysheng.sxbus.databinding.ItemOpenSourceBinding;

/**
 * Created by tysheng
 * Date: 2017/5/5 16:25.
 * Email: tyshengsx@gmail.com
 */

public class OpenSourceAdapter extends BaseDataBindingAdapter<OpenSourceInfo, ItemOpenSourceBinding> {
    public OpenSourceAdapter() {
        super(R.layout.item_open_source, null);
    }

    @Override
    protected void convert(ItemOpenSourceBinding binding, OpenSourceInfo item) {
        binding.setInfo(item);

    }

}
