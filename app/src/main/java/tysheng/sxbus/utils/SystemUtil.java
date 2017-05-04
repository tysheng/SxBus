package tysheng.sxbus.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;

import tysheng.sxbus.BuildConfig;


/**
 * Created by Sty
 * Date: 16/8/19 09:36.
 */
public class SystemUtil {

    public static void share(Context context, String text, String title, Uri path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_STREAM, path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 检测网络是否连接,不管是否可以上网
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static void sendEmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "353491983@qq.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "关于车来哉的问题反馈");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"353491983@qq.com"});
        context.startActivity(Intent.createChooser(emailIntent, "发送邮件..."));
    }

    /**
     * 是否可以上网
     *
     * @return
     */
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 114.114.114.114");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return 获取App版本号
     * @throws Exception
     */
    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }
}
