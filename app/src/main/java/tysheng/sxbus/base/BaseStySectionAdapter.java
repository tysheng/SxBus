package tysheng.sxbus.base;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by tysheng
 * Date: 2016/11/23 14:21.
 * Email: tyshengsx@gmail.com
 */

public abstract class BaseStySectionAdapter<T extends SectionEntity> extends BaseRecyclerViewAdapter<T> {
    protected static final int SECTION_HEADER_VIEW = 1092;
    protected int mSectionHeadResId;

    public BaseStySectionAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public BaseStySectionAdapter(List<T> data) {
        super(data);
    }

    public BaseStySectionAdapter(int layoutResId, int sectionHeadResId, List<T> data) {
        super(layoutResId, data);
        this.mSectionHeadResId = sectionHeadResId;
    }

    protected int getDefItemViewType(int position) {
        return this.mData.get(position).isHeader ? 1092 : 0;
    }

    protected StyBaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return viewType == 1092 ? this.createBaseViewHolder(this.getItemView(this.mSectionHeadResId, parent)) : super.onCreateDefViewHolder(parent, viewType);
    }

    public void onBindViewHolder(StyBaseViewHolder holder, int positions) {
        switch (holder.getItemViewType()) {
            case 1092:
                this.setFullSpan(holder);
                this.convertHead(holder, this.mData.get(holder.getLayoutPosition() - this.getHeaderLayoutCount()));
                break;
            default:
                super.onBindViewHolder(holder, positions);
        }

    }

    protected abstract void convertHead(BaseViewHolder var1, T var2);
}
