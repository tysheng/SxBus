package tysheng.sxbus.utils.rxfastcache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private static DiskLruCache cache;

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
            cache = DiskLruCache.open(cacheDir, 1, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String md5(String string) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Check if an object with the given key exists in the RxFastCache.
     *
     * @param key the key string.
     * @return true if object with given key exists.
     */
    public static boolean containsSync(final String key) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(md5(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (snapshot == null)
            return false;
        snapshot.close();
        return true;
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
                    _put(key, json);
                } catch (Exception e) {
                    e.printStackTrace();
                    b = false;
                }
                return b;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * put json object
     *
     * @param key   key
     * @param value json object
     * @throws IOException
     */
    private static void _put(String key, String value) throws IOException {
        if (value.getBytes().length > cache.getMaxSize())
            throw new IOException("Object size greater than cache size!");
        OutputStream cos = null;
        DiskLruCache.Editor editor = cache.edit(md5(key));
        BufferedOutputStream bos = new BufferedOutputStream(editor.newOutputStream(0));
        try {
            cos = new CacheOutputStream(bos, editor);
            cos.write(value.getBytes());
        } catch (IOException e) {
            editor.abort();
        } finally {
            if (cos != null)
                cos.close();
        }
    }

    private static String getString(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = cache.get(md5(key));
        if (snapshot == null)
            return null;
        try {
            return snapshot.getString(0);
        } finally {
            snapshot.close();
        }
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
            json = getString(key);
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
            json = getString(key);
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
    public static Observable<Boolean> remove(final String key) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override
            public Boolean call() {
                boolean b = true;
                try {
                    cache.remove(md5(key));
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
                    cache.delete();
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
        return cache.size();
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
                    _putBitmap(key, bitmap);
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
            bitmap = _getBitmap(key);
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

    private static Bitmap _getBitmap(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = cache.get(md5(key));
        if (snapshot == null)
            return null;
        try {
            return BitmapFactory.decodeStream(snapshot.getInputStream(0));
        } finally {
            snapshot.close();
        }
    }

    private static void _putBitmap(String key, Bitmap value) throws IOException {
        if (value == null)
            return;
        if (value.getByteCount() > cache.getMaxSize())
            throw new IOException("Object size greater than cache size!");
        OutputStream cos = null;
        DiskLruCache.Editor editor = cache.edit(md5(key));
        BufferedOutputStream bos = new BufferedOutputStream(editor.newOutputStream(0));
        try {
            cos = new CacheOutputStream(bos, editor);
            //bitmap to byte[]
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            value.compress(Bitmap.CompressFormat.PNG, 100, stream);
            cos.write(stream.toByteArray());
        } catch (IOException e) {
            editor.abort();
        } finally {
            if (cos != null)
                cos.close();
        }
    }

}
