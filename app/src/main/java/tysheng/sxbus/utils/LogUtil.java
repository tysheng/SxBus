package tysheng.sxbus.utils;

import android.util.Log;

/**
 * Created by Sty
 * Date: 16/8/11 23:15.
 */
public class LogUtil {
    public static void d(String msg){
        Log.d("sty",msg);
    }
    public static void d(int msg){
        Log.d("sty",String.valueOf(msg));
    }
}
