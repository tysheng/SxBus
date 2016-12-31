package tysheng.sxbus.bean;

import android.os.Parcelable;

/**
 * Created by tysheng
 * Date: 2016/12/31 18:48.
 * Email: tyshengsx@gmail.com
 */

public class FragCallback {
    public int what;
    public String arg1;
    public String arg2;
    public Parcelable parcelable;

    public FragCallback(int what) {
        this.what = what;
    }

    public FragCallback(int what, String arg1) {
        this.what = what;
        this.arg1 = arg1;
    }

    public FragCallback(int what, String arg1, String arg2) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public FragCallback(int what, String arg1, String arg2, Parcelable parcelable) {
        this.what = what;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.parcelable = parcelable;
    }
}
