package tysheng.sxbus.base;

import android.databinding.ViewDataBinding;
import android.view.View;

/**
 * Created by tysheng
 * Date: 2017/5/11 14:42.
 * Email: tyshengsx@gmail.com
 */

public class TyBindingViewHolder<Binding extends ViewDataBinding> extends TyViewHolder {
    private Binding mBinding;

    public TyBindingViewHolder(View view) {
        super(view);
    }

    public Binding getBinding() {
        return mBinding;
    }

    public void setBinding(Binding binding) {
        mBinding = binding;
    }
}
