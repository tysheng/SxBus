package tysheng.sxbus.presenter;

import java.util.List;

import tysheng.sxbus.bean.Star;
import tysheng.sxbus.utils.StySubscriber;
import tysheng.sxbus.utils.rxfastcache.RxFastCache;

/**
 * Created by Sty
 * Date: 16/9/12 09:47.
 */
public class StarUtil {
    public static void saveStarList(String tag, List<Star> mStars) {
        if (mStars != null)
            RxFastCache.put(tag, mStars)
                    .subscribe(new StySubscriber<Boolean>() {
                        @Override
                        public void next(Boolean aBoolean) {

                        }
                    });
    }
}
