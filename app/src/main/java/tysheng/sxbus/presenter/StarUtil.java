package tysheng.sxbus.presenter;

import java.util.List;

import tysheng.sxbus.bean.Star;
import tysheng.sxbus.utils.StySubscriber;
import tysheng.sxbus.utils.fastcache.FastCache;

/**
 * Created by Sty
 * Date: 16/9/12 09:47.
 */
public class StarUtil {
    public static void saveStarList(String tag, List<Star> mStars) {
        if (mStars != null)
            FastCache.putAsync(tag, mStars)
                    .subscribe(new StySubscriber<Boolean>() {
                        @Override
                        public void next(Boolean aBoolean) {

                        }
                    });
    }

    public static List<Star> initStarList(String tag) {
        return FastCache.getArray(tag, Star.class);
    }


}
