package tysheng.sxbus.utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by tysheng
 * Date: 2017/5/4 13:33.
 * Email: tyshengsx@gmail.com
 */

public class StringConverterFactory extends Converter.Factory {
    private StringConverterFactory() {
        super();
    }

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return new StringConverter();
        }
        return super.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        return new String2Converter();
    }

    private static class StringConverter implements Converter<ResponseBody, String> {

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    private static class String2Converter implements Converter<String, String> {

        @Override
        public String convert(String value) throws IOException {
            return value;
        }
    }
}
