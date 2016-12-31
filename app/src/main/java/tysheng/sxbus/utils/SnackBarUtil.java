package tysheng.sxbus.utils;

import android.graphics.Color;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Sty
 * Date: 16/9/11 13:47.
 */
public class SnackBarUtil {
    public static void show(View view,String msg){
       show(view,msg,Snackbar.LENGTH_SHORT);
    }
    public static void show(View view,String msg,int time){
        Snackbar snackbar = Snackbar.make(view, msg, time);
        FrameLayout frameLayout = (FrameLayout) snackbar.getView();
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) frameLayout.getChildAt(0);
        contentLayout.getMessageView().setTextColor(Color.WHITE);
        snackbar.show();
    }
}
