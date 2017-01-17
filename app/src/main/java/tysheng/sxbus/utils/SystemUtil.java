package tysheng.sxbus.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import tysheng.sxbus.App;


/**
 * Created by Sty
 * Date: 16/8/19 09:36.
 */
public class SystemUtil {
    public static final long MB = 1048576; // 1024 * 1024

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
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) App.get().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        Context context = App.get();
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "x.x";
    }

    public static int getVersionCode() {
        Context context = App.get();
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * 文件大小获取
     *
     * @param file File对象
     * @return 文件大小字符串
     */
    public static String getFileSize(File file) {
        long length = getDirSize(file);
        if (length / MB < 1)
            return "0M";
        return length / MB + "M";
    }

    /**
     * 遍历文件大小
     *
     * @param dir file
     * @return file size
     */
    private static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    public static void clearCache() {
        Observable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return SystemUtil.deleteFile(App.get().getCacheDir().getPath());
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static int dp2px(float dpValue) {

        return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }
}
