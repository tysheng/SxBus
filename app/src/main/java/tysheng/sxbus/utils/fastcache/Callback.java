package tysheng.sxbus.utils.fastcache;

public interface Callback {
    void onSuccess();

    void onFailure(Exception e);
}
