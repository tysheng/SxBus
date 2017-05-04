package tysheng.sxbus.net;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Sty
 * Date: 16/8/10 21:31.
 */
public interface BusService {
    String IP = "220.191.224.75";
    String MAIL_CODE = "330600";
    String BASE_URL = "http://" + IP + "/server-ue2/rest/";//220.191.224.75
    String HTTP_ADDRESS_URL = "http://www.iwaybook.com/server-ue2/rest/servers-v2/";//330600


    @GET("buslines/simple/" + MAIL_CODE + "/{number}/0/20")
    Flowable<String> numberToSearch(@Path("number") String number);

    @GET("buslines/" + MAIL_CODE + "/{code}")
    Flowable<String> getBusLines(@Path("code") String code);

    @GET("buses/busline/" + MAIL_CODE + "/{code}")
    Flowable<String> getRunningBus(@Path("code") String code);

    @GET
    Flowable<String> url(@Url() String url);
}
