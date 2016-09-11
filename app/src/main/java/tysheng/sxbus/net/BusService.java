package tysheng.sxbus.net;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import tysheng.sxbus.bean.BusLine;
import tysheng.sxbus.bean.BusLines;
import tysheng.sxbus.bean.BusLinesSimple;

/**
 * Created by Sty
 * Date: 16/8/10 21:31.
 */
public interface BusService {
    String BASE_URL="http://220.191.224.75/server-ue2/rest/";
    @GET("buslines/simple/330600/{number}/0/20")
    Observable<BusLinesSimple> getBusLinesSimple(@Path("number") int number);
    @GET("buslines/330600/{code}")
    Observable<BusLines> getBusLines(@Path("code") String code);

    @GET("buses/busline/330600/{code}")
    Observable<BusLine> getBusLine(@Path("code") String code);
}
