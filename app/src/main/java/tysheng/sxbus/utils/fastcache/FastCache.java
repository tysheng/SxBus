package tysheng.sxbus.utils.fastcache;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;

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

    /**
     * FastJson Version
     * --------------------------------------
     */
    private static String toJson(Object object) {
        return JSON.toJSONString(object);
    }

    private static <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }

    private static <T> T fromJson(String json, Type typeOfT) {
        return JSON.parseObject(json, typeOfT);
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
    public static synchronized void init(Context context, long maxSize) throws Exception {
        if (cacheDir == null)
            cacheDir = new File(context.getCacheDir() + File.separator + cacheFileName);

        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        cache = SimpleDiskCache.open(cacheDir, 1, maxSize);
    }

    /**
     * Check if an object with the given key exists in the FastCache.
     *
     * @param key the key string.
     * @return true if object with given key exists.
     */
    public static boolean contains(final String key) throws Exception {
        return cache.contains(key);
    }

    /**
     * Put an object into FastCache with the given key. This a blocking IO operation. Previously
     * stored object with the same
     * key (if any) will be overwritten.
     *
     * @param key    the key string.
     * @param object the object to be stored.
     */
    public static void put(final String key, final Object object) throws Exception {
        String json = toJson(object);
        cache.put(key, json);
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
    public static <T> T get(final String key, final Class<T> classOfT) throws Exception {

        String json = cache.getString(key).getString();
        T value = fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return value;
    }


    /**
     * Get an object from FastCache with the given key. This a blocking IO operation.
     *
     * @param key     the key string.
     * @param typeOfT the type of the expected return object.
     * @return the object of the given type if it exists.
     */
    public static <T> T get(final String key, final Type typeOfT) throws Exception {

        String json = cache.getString(key).getString();
        T value = fromJson(json, typeOfT);
        if (value == null)
            throw new NullPointerException();
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
     * Get an object from FastCache with the given key asynchronously.
     *
     * @param key      the key string.
     * @param classOfT the class type of the expected return object.
     * @param typeOfT  the type of the collection object which contains objects of type {@code classOfT}.
     * @return an {@link Observable} that will fetch the object from FastCache. By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static <T> Observable<T> getAsync(final String key, final Class<T> classOfT, final Type typeOfT) {

        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    Collection<T> collectionOfT = FastCache.get(key, typeOfT);
                    for (T t : collectionOfT) {
                        subscriber.onNext(t);
                    }
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Delete an object from FastCache with the given key. This a blocking IO operation. Previously
     * stored object with the same
     * key (if any) will be deleted.
     *
     * @param key the key string.
     */
    public static void delete(final String key) throws Exception {

        cache.delete(key);
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
    public static Observable<Boolean> deleteAsync(final String key) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    FastCache.delete(key);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception exception) {
                    subscriber.onError(exception);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Clears the cache. Deletes all the stored key-value pairs synchronously.
     */
    public static void clear(Context context) throws Exception {

        long maxSize = cache.getMaxSize();
        cache.destroy();
        init(context, maxSize);
    }

    /**
     * Clears the cache. Deletes all the stored key-value pairs asynchronously.
     *
     * @return an {@link Observable} that will clear all the key-value pairs from FastCache.By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> clearAsync(final Context context) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    FastCache.clear(context);
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
    static long bytesUsed() throws Exception {

        return cache.bytesUsed();
    }

}
