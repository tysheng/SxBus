package tysheng.sxbus.presenter;

import java.util.ArrayList;

import rx.Subscriber;
import tysheng.sxbus.Constant;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.utils.fastcache.FastCache;

/**
 * Created by Sty
 * Date: 16/9/12 09:47.
 */
public class StarUtil {

    public static void onStopSave(Stars mStars) {
        if (mStars != null && mStars.result.size() != 0)
            FastCache.putAsync(Constant.STAR, mStars)
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

    public static Stars initStars(Stars mStars) {
        try {
            mStars = FastCache.get(Constant.STAR, Stars.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStars == null) {
            mStars = new Stars();
            mStars.result = new ArrayList<>();
        }
        return mStars;
    }


}
