package tysheng.sxbus.utils.rxfastcache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SimpleDiskCache {
    private final DiskLruCache diskLruCache;

    private SimpleDiskCache(File dir, int appVersion, long maxSize) throws IOException {
        diskLruCache = DiskLruCache.open(dir, appVersion, 1, maxSize);
    }

    static synchronized SimpleDiskCache open(File dir, int appVersion,
                                             long maxSize) throws IOException {
        return new SimpleDiskCache(dir, appVersion, maxSize);
    }

    /**
     * put json object
     *
     * @param key   key
     * @param value json object
     * @throws IOException
     */
    void put(String key, String value) throws IOException {
        if (value.getBytes().length > getMaxSize())
            throw new IOException("Object size greater than cache size!");
        OutputStream cos = null;
        DiskLruCache.Editor editor = diskLruCache.edit(md5(key));
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

    /**
     * getString string for json
     *
     * @param key key
     * @return json string
     * @throws IOException
     */
    String getString(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(md5(key));
        if (snapshot == null)
            return null;
        try {
            return snapshot.getString(0);
        } finally {
            snapshot.close();
        }
    }

    long getMaxSize() throws IOException {
        return diskLruCache.getMaxSize();
    }

    boolean contains(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(md5(key));
        if (snapshot == null)
            return false;
        snapshot.close();
        return true;
    }

    void delete(String key) throws IOException {
        diskLruCache.remove(md5(key));
    }

    void destroy() throws IOException {
        diskLruCache.delete();
    }

    long bytesUsed() throws IOException {
        return diskLruCache.size();
    }

    private String md5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes("UTF-8"));
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

    void putBitmap(String key, Bitmap value) throws IOException {
        if (value == null)
            return;
        if (value.getByteCount() > getMaxSize())
            throw new IOException("Object size greater than cache size!");
        OutputStream cos = null;
        DiskLruCache.Editor editor = diskLruCache.edit(md5(key));
        BufferedOutputStream bos = new BufferedOutputStream(editor.newOutputStream(0));
        try {
            cos = new CacheOutputStream(bos, editor);
            cos.write(bitmap2Bytes(value));
        } catch (IOException e) {
            editor.abort();
        } finally {
            if (cos != null)
                cos.close();
        }
    }

    private byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    Bitmap getBitmap(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskLruCache.get(md5(key));
        if (snapshot == null)
            return null;
        try {
            return BitmapFactory.decodeStream(snapshot.getInputStream(0));
        } finally {
            snapshot.close();
        }
    }
}

