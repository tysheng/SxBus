package tysheng.sxbus.di;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by tysheng
 * Date: 2017/5/5 09:51.
 * Email: tyshengsx@gmail.com
 */
@Scope
@Retention(RUNTIME)
public @interface PerPresenter {
}
