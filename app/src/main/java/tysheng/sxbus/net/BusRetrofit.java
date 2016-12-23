package tysheng.sxbus.net;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import tysheng.sxbus.App;
import tysheng.sxbus.utils.SystemUtil;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class BusRetrofit {
    private static BusService sService = null;
    private static volatile Retrofit retrofit = null;
    private static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if (!SystemUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            Response.Builder builder = originalResponse.newBuilder()
                    .removeHeader("Pragma");
            if (SystemUtil.isNetworkAvailable()) {
                int maxAge = 0; // read from cache
                return builder
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 7; // tolerate one-week stale
                return builder
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    public static void init() {
        final File baseDir = App.get().getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpCache");
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        }
        //设置缓存 10M
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final int TIME_MAX = 12;
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);

        builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
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

