package tysheng.sxbus;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class App extends TinkerApplication {

    public App() {
        super(ShareConstants.TINKER_ENABLE_ALL, "tysheng.sxbus.AppLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
