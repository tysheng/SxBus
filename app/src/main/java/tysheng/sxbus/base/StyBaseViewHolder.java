package tysheng.sxbus.base;

import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by tysheng
 * Date: 2016/11/4 10:24.
 * Email: tyshengsx@gmail.com
 */

public class StyBaseViewHolder extends BaseViewHolder {
    public StyBaseViewHolder(View view) {
        super(view);
    }

    public StyBaseViewHolder addOnClickListeners(int... viewIds) {
        for (int id : viewIds) {
            addOnClickListener(id);
        }
        return this;
    }

    public StyBaseViewHolder addOnLongClickListeners(int... viewIds) {
        for (int id : viewIds) {
            addOnLongClickListener(id);
        }
        return this;
    }


    public StyBaseViewHolder setColorFilter(@IdRes int viewId, @ColorInt int color) {
        ImageView view = getView(viewId);
        view.setColorFilter(color);
        return this;
    }


    public StyBaseViewHolder setTextAndVisible(@IdRes int viewId, String text) {
        TextView textView = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
        return this;
    }


    public StyBaseViewHolder setTextAndVisibleFront(@IdRes int viewId, String text, String frontExtra) {
        TextView textView = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(frontExtra + text);
            textView.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public StyBaseViewHolder setTextAndVisibleBehind(@IdRes int viewId, String text, String behindExtra) {
        TextView textView = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text + behindExtra);
            textView.setVisibility(View.VISIBLE);
        }
        return this;
    }


}
