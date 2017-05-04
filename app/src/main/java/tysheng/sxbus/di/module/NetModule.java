package tysheng.sxbus.di.module;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import tysheng.sxbus.net.BusService;
import tysheng.sxbus.utils.StringConverterFactory;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:32.
 * Email: tyshengsx@gmail.com
 */
@Module
public class NetModule {

    @Singleton
    @Provides
    Retrofit provideRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final int TIME_MAX = 10;
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BusService.BASE_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Singleton
    @Provides
    public BusService provideBusService(Retrofit retrofit) {
        return retrofit.create(BusService.class);
    }


}
