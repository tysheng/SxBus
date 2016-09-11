package tysheng.sxbus.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof TextView)
                ((TextView) v).setTextColor(Color.WHITE);
        }
        snackbar.show();

    }
}
