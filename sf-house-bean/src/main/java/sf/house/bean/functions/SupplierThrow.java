package sf.house.bean.functions;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@FunctionalInterface
public interface SupplierThrow<T, E extends Throwable> {
    T get() throws E;
}
