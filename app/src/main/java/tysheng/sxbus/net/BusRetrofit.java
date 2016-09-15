package tysheng.sxbus.net;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import tysheng.sxbus.App;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class BusRetrofit {

    private static final int TIME_MAX = 10;
    public static BusService sService = null;
    private static volatile Retrofit retrofit = null;

    public static void init() {
        final File baseDir = App.get().getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpCache");
            cache = new Cache(cacheDir, 100 * 1024 * 1024);
        }
        //设置缓存 10M
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);
        OkHttpClient client = builder.cache(cache).build();


        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .baseUrl(BusService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }


    public static BusService get() {
        if (sService == null) {
            synchronized (BusRetrofit.class) {
                if (sService == null) {
                    init();
                }
                sService = retrofit.create(BusService.class);
            }
        }
        return sService;
    }
}

