package tysheng.sxbus.utils.fastcache;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The main reservoir class.
 */
public class FastCache {

    private static SimpleDiskCache cache;

    private static File cacheDir;

    private static String cacheFileName = "FastCache";

    private static int version = 1;

    /**
     * FastJson Version
     * --------------------------------------
     */
    private static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

    private static <T> T parseJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }

    private static <T> List<T> parseJsonArray(String json, Class<T> classOfT) {
        return JSON.parseArray(json, classOfT);
    }
    /**
     * --------------------------------------
     */

    /**
     * Initialize FastCache
     *
     * @param context context.
     * @param maxSize the maximum size in bytes.
     */
    public static synchronized void init(Context context, long maxSize) {
        if (cacheDir == null)
            cacheDir = new File(context.getCacheDir() + File.separator + cacheFileName);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        try {
            cache = SimpleDiskCache.open(cacheDir, version, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if an object with the given key exists in the FastCache.
     *
     * @param key the key string.
     * @return true if object with given key exists.
     */
    public static boolean contains(final String key) {
        boolean contain = false;
        try {
            contain = cache.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contain;
    }

    /**
     * Put an object into FastCache with the given key. This a blocking IO operation. Previously
     * stored object with the same
     * key (if any) will be overwritten.
     *
     * @param key    the key string.
     * @param object the object to be stored.
     */
    public static void put(final String key, final Object object) {
        String json = toJson(object);
        try {
            cache.put(key, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Put an object into FastCache with the given key asynchronously. Previously
     * stored object with the same
     * key (if any) will be overwritten.
     *
     * @param key    the key string.
     * @param object the object to be stored.
     * @return an {@link Observable} that will insert the object into FastCache. By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> putAsync(final String key, final Object object) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    FastCache.put(key, object);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get an object from FastCache with the given key. This a blocking IO operation.
     *
     * @param key      the key string.
     * @param classOfT the class type of the expected return object.
     * @return the object of the given type if it exists.
     */
    public static <T> T get(final String key, final Class<T> classOfT) {
        String json;
        T value = null;
        try {
            json = cache.getString(key).getString();
            value = parseJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * FastJson Array
     *
     * @param key
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> List<T> getArray(final String key, final Class<T> classOfT) {
        String json;
        List<T> value = null;
        try {
            json = cache.getString(key).getString();
            value = parseJsonArray(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value == null)
            value = new ArrayList<>();
        return value;
    }


    /**
     * Get an object from FastCache with the given key asynchronously.
     *
     * @param key      the key string.
     * @param classOfT the class type of the expected return object.
     * @return an {@link Observable} that will fetch the object from FastCache. By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static <T> Observable<T> getAsync(final String key, final Class<T> classOfT) {

        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    T t = FastCache.get(key, classOfT);
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * FastJson Array
     *
     * @param key
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> Observable<List<T>> getArrayAsync(final String key, final Class<T> classOfT) {
        return Observable.create(new Observable.OnSubscribe<List<T>>() {
            @Override
            public void call(Subscriber<? super List<T>> subscriber) {
                try {
                    List<T> t = FastCache.getArray(key, classOfT);
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Delete an object into FastCache with the given key asynchronously. Previously
     * stored object with the same
     * key (if any) will be deleted.
     *
     * @param key the key string.
     * @return an {@link Observable} that will delete the object from FastCache.By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> delete(final String key) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    cache.delete(key);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Clears the cache. Deletes all the stored key-value pairs asynchronously.
     *
     * @return an {@link Observable} that will clear all the key-value pairs from FastCache.By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> clear(final Context context) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long maxSize;
                try {
                    maxSize = cache.getMaxSize();
                    cache.destroy();
                    init(context, maxSize);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns the number of bytes being used currently by the cache.
     */
    public static long bytesUsed() {
        long bytes = 0;
        try {
            bytes = cache.bytesUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
