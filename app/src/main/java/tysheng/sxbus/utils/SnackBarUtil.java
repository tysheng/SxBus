package tysheng.sxbus.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Sty
 * Date: 16/9/11 13:47.
 */
public class SnackBarUtil {
    public static void show(View view,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }
    public static void show(View view,String msg,int time){
        Snackbar.make(view,msg,time).show();
    }
}
