package tysheng.sxbus.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class BusRetrofit {
    private static BusService sService = null;
    private static volatile Retrofit retrofit = null;


    private static void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final int TIME_MAX = 10;
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .baseUrl(BusService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

