package tysheng.sxbus.bean;

/**
 * Created by tysheng
 * Date: 2016/9/25 16:27.
 * Email: tyshengsx@gmail.com
 */

public class FragmentTags {
    public String tag;
    public String backStackTag;

    public FragmentTags(String tag, String backStackTag) {
        this.tag = tag;
        this.backStackTag = backStackTag;
    }

    public FragmentTags(String tag) {
        this.tag = tag;
    }
}
