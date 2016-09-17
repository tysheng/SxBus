package tysheng.sxbus.presenter;

import java.util.ArrayList;

import rx.Subscriber;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.utils.fastcache.FastCache;

/**
 * Created by Sty
 * Date: 16/9/12 09:47.
 */
public class StarUtil {
    public static void saveStars(String tag, Stars mStars) {
        if (mStars != null && mStars.result != null)
            FastCache.putAsync(tag, mStars)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {

                        }
                    });

    }

    public static Stars initStars(String tag) {
        Stars mStars = FastCache.get(tag, Stars.class);
        if (mStars == null) {
            mStars = new Stars();
            mStars.result = new ArrayList<>();
        }
        return mStars;
    }


}
