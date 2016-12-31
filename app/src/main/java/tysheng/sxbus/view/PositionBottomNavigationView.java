package tysheng.sxbus.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by tysheng
 * Date: 2016/12/31 12:24.
 * Email: tyshengsx@gmail.com
 */

public class PositionBottomNavigationView extends BottomNavigationView implements BottomNavigationView.OnNavigationItemSelectedListener {
    private SparseIntArray mMenuIds;
    private int currentPosition;
    private onPositionSelectedListener mListener;

    public PositionBottomNavigationView(Context context) {
        this(context, null, 0);
    }

    public PositionBottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PositionBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMenuIds = new SparseIntArray();
        setOnNavigationItemSelectedListener(this);
    }

    /**
     * ATTENTION: register the ids first.
     *
     * @param ids menu ids by order, like R.id.menu_0,R.id.menu_1...
     */
    public void registerIds(int... ids) {
        mMenuIds.clear();
        for (int i = 0; i < ids.length; i++) {
            mMenuIds.put(i, ids[i]);
        }
    }

    public void setOnPositionSelectedListener(onPositionSelectedListener listener) {
        mListener = listener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        setPositionChecked();
        if (mListener != null)
            mListener.onPositionSelected(currentPosition);
    }

    private void setPositionChecked() {
        Menu menu = getMenu();
        if (menu.getItem(currentPosition).isChecked()) {
            return;
        }
        for (int i = 0; i < mMenuIds.size(); i++) {
            if (mMenuIds.get(i) != mMenuIds.get(currentPosition)) {
                menu.getItem(i).setChecked(false);
            } else {
                menu.getItem(i).setChecked(true);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int pos = mMenuIds.indexOfValue(item.getItemId());
        if (pos != -1) {
            setCurrentPosition(pos);
        }
        return true;
    }

    public interface onPositionSelectedListener {
        void onPositionSelected(int position);
    }
}
