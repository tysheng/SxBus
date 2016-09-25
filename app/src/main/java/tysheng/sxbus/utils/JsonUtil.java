package tysheng.sxbus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * JsonUtil for FastJson
 * Created by Sty
 * Date: 2016/9/24 07:58.
 */

public class JsonUtil {
    public static <T> T parse(String s, Class<T> tClass) {
        return JSON.parseObject(s, tClass);
    }

    public static <T> List<T> parseArray(String s, Class<T> tClass) {
        return JSONArray.parseArray(s, tClass);
    }
}
