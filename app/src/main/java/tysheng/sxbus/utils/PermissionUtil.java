package tysheng.sxbus.utils;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by tysheng
 * Date: 2017/1/9 10:12.
 * Email: tyshengsx@gmail.com
 */

public class PermissionUtil {
    public static void request(Activity activity, final Callback callback, final String... permissions) {
        new RxPermissions(activity)
                .request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (callback != null)
                            callback.call(aBoolean);
                    }
                });
    }

    public static void requestLocation(Activity activity, final Callback callback) {
        request(activity, callback, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static void requestAudio(Activity activity, final Callback callback) {
        request(activity, callback, Manifest.permission.RECORD_AUDIO);
    }

    public static void requestWrite(Activity activity, final Callback callback) {
        request(activity, callback, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestPhoneState(Activity activity, final Callback callback) {
        request(activity, callback, Manifest.permission.READ_PHONE_STATE);
    }

    public static void requestCamera(Activity activity, final Callback callback) {
        request(activity, callback, Manifest.permission.CAMERA);
    }

    public static boolean isGranted(Activity activity, final String permission) {
        return new RxPermissions(activity)
                .isGranted(permission);
    }

    public interface Callback {
        void call(boolean b);
    }
}
