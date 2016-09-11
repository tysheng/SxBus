package tysheng.sxbus.utils.fastcache;

public interface GetCallback<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}
