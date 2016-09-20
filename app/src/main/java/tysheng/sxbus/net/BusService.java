package tysheng.sxbus.net;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.HttpAdd;

/**
 * Created by Sty
 * Date: 16/8/10 21:31.
 */
public interface BusService {
    String IP = "220.191.224.75";
    String MAIL_CODE = "330600";
    String BASE_URL = "http://" + IP + "/server-ue2/rest/";//220.191.224.75
    String HTTP_ADDRESS_URL = "http://www.iwaybook.com/server-ue2/rest/servers-v2/";//330600

    @GET("{code}")
    Observable<HttpAdd> getAddress(@Path("code") String code);

    @GET("buslines/simple/" + MAIL_CODE + "/{number}/0/20")
    Observable<CallBack> numberToSearch(@Path("number") String number);

    @GET("buslines/" + MAIL_CODE + "/{code}")
    Observable<CallBack> getBusLines(@Path("code") String code);

    @GET("buses/busline/" + MAIL_CODE + "/{code}")
    Observable<CallBack> getRunningBus(@Path("code") String code);
}
