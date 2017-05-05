package tysheng.sxbus.ui.inter;

import android.text.SpannableString;

import java.util.List;

import tysheng.sxbus.bean.More;
import tysheng.sxbus.ui.base.BaseView;

/**
 * Created by tysheng
 * Date: 2017/2/28 17:00.
 * Email: tyshengsx@gmail.com
 */

public interface MoreView extends BaseView {
    void setTitle(SpannableString title);

    void snackBarShow(String s);

    void setMoreList(List<More> list);
}
