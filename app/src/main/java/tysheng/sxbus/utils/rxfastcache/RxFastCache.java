package tysheng.sxbus.utils.rxfastcache;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * The main reservoir class.
 */
public class RxFastCache {

    private static SimpleDiskCache cache;

    private static File cacheDir;

    private static String cacheFileName = "RxFastCache";

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
     * Initialize RxFastCache
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
            cache = SimpleDiskCache.open(cacheDir, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if an object with the given key exists in the RxFastCache.
     *
     * @param key the key string.
     * @return true if object with given key exists.
     */
    public static boolean containsSync(final String key) {
        boolean contain = false;
        try {
            contain = cache.contains(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contain;
    }
    /**
     * Check if an object with the given key exists in the RxFastCache.
     *
     * @param key the key string.
     * @return true if object with given key exists.
     */
    public static Observable<Boolean> contain(final String key) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                return containsSync(key);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Put an object into RxFastCache with the given key asynchronously. Previously
     * stored object with the same
     * key (if any) will be overwritten.
     *
     * @param key    the key string.
     * @param object the object to be stored.
     * @return an {@link Observable} that will insert the object into RxFastCache. By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> put(final String key, final Object object) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                Boolean b = true;
                String json = toJson(object);
                try {
                    cache.put(key, json);
                } catch (Exception e) {
                    e.printStackTrace();
                    b = false;
                }
                return b;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get an object from RxFastCache with the given key. This a blocking IO operation.
     *
     * @param key      the key string.
     * @param classOfT the class type of the expected return object.
     * @return the object of the given type if it exists.
     */
    public static <T> T getSync(final String key, final Class<T> classOfT) {
        String json;
        T value = null;
        try {
            json = cache.getString(key);
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
    public static <T> List<T> getArraySync(final String key, final Class<T> classOfT) {
        String json;
        List<T> value = null;
        try {
            json = cache.getString(key);
            value = parseJsonArray(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value == null)
            value = new ArrayList<>();
        return value;
    }

    /**
     * Get an object from RxFastCache with the given key asynchronously.
     *
     * @param key      the key string.
     * @param classOfT the class type of the expected return object.
     * @return an {@link Observable} that will fetch the object from RxFastCache. By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static <T> Observable<T> get(final String key, final Class<T> classOfT) {
        return Observable.fromCallable(new Func0<T>() {
            @Override
            public T call() {
                return getSync(key, classOfT);
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
    public static <T> Observable<List<T>> getArray(final String key, final Class<T> classOfT) {
        return Observable.fromCallable(new Func0<List<T>>() {
            @Override
            public List<T> call() {
                return getArraySync(key, classOfT);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Delete an object into RxFastCache with the given key asynchronously. Previously
     * stored object with the same
     * key (if any) will be deleted.
     *
     * @param key the key string.
     * @return an {@link Observable} that will delete the object from RxFastCache.By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> delete(final String key) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                boolean b = true;
                try {
                    cache.delete(key);
                } catch (IOException e) {
                    e.printStackTrace();
                    b = false;
                }
                return b;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Clears the cache. Deletes all the stored key-value pairs asynchronously.
     *
     * @return an {@link Observable} that will clear all the key-value pairs from RxFastCache.By default, this
     * will be scheduled on a background thread and will be observed on the main thread.
     */
    public static Observable<Boolean> clear(final Context context) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                long maxSize = 0;
                boolean b = true;
                try {
                    maxSize = cache.getMaxSize();
                    cache.destroy();
                    b = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                init(context, maxSize);
                return b;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns the number of bytes being used currently by the cache.
     */
    public static long getUsedSync() {
        long bytes = 0;
        try {
            bytes = cache.bytesUsed();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }


    /**
     * Bitmap
     *
     * @param key
     * @param bitmap
     * @return
     */
    public static Observable<Boolean> putBitmap(final String key, final Bitmap bitmap) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                Boolean b = true;
                try {
                    cache.putBitmap(key, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    b = false;
                }
                return b;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Bitmap getBitmapSync(final String key) {
        Bitmap bitmap = null;
        try {
            bitmap = cache.getBitmap(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Observable<Bitmap> getBitmap(final String key) {
        return Observable.fromCallable(new Func0<Bitmap>() {
            @Override
            public Bitmap call() {
                return getBitmapSync(key);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
