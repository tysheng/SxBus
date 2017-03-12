package tysheng.sxbus.utils;

import android.content.Context;
import android.content.res.Resources;


public class UiUtil {

    private static int screenWidth;

    public static int getScreenWidth() {
        if (screenWidth > 0) {
            return screenWidth;
        }
        return screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getImageWidth() {
        return getScreenWidth();
    }

    public static int getImageHeight() {
        return 1000;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dp2px(float dpValue) {

        return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(float pxValue) {

        return (int) (pxValue / Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

}
