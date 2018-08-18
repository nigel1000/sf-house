package sf.house.aop.util;

import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class FunctionUtil {

    public static <V, K> Map<K, V> keyValueMap(Collection<V> collection, Function<V, K> keyFunc) {

        if (CollectionUtils.isEmpty(collection) || keyFunc == null) {
            return new HashMap<>();
        }
        return collection.stream().collect(Collectors.toMap(keyFunc, Function.identity(), (k1, k2) -> k1));
    }

    public static <T, K> Set<K> valueSet(Collection<T> collection, Predicate<T> predicate, Function<T, K> valueFunc) {

        if (CollectionUtils.isEmpty(collection) || predicate == null || valueFunc == null) {
            return new HashSet<>();
        }
        return collection.stream().filter(predicate).map(valueFunc).collect(Collectors.toSet());
    }

    public static <T, K> Set<K> valueSet(Collection<T> collection, Function<T, K> valueFunc) {

        if (CollectionUtils.isEmpty(collection) || valueFunc == null) {
            return new HashSet<>();
        }
        return collection.stream().map(valueFunc).collect(Collectors.toSet());
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {

        if (CollectionUtils.isEmpty(collection) || predicate == null) {
            return new ArrayList<>();
        }
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, K> List<K> valueList(Collection<T> collection, Predicate<T> predicate, Function<T, K> valueFunc) {

        if (CollectionUtils.isEmpty(collection) || predicate == null || valueFunc == null) {
            return new ArrayList<>();
        }
        return collection.stream().filter(predicate).map(valueFunc).collect(Collectors.toList());
    }

    public static <T, K> List<K> valueList(Collection<T> collection, Function<T, K> valueFunc) {

        if (CollectionUtils.isEmpty(collection) || valueFunc == null) {
            return new ArrayList<>();
        }
        return collection.stream().map(valueFunc).collect(Collectors.toList());
    }

    public static <T, K> Map<K, List<T>> valueMap(Collection<T> collection, Function<T, K> valueFunc) {

        if (CollectionUtils.isEmpty(collection) || valueFunc == null) {
            return new HashMap<>();
        }
        return collection.stream().collect(
                Collectors.groupingBy(valueFunc, Collectors.mapping(Function.identity(), Collectors.toList())));
    }

    /**
     * 批处理限流
     * 
     * @param objList
     * @param everyTimeSize
     * @param execute
     * @param <T>
     */
    public static <T> void splitExecute(@NonNull List<T> objList, @NonNull Integer everyTimeSize, Execute<T> execute) {
        int times = objList.size() % everyTimeSize == 0 ? (objList.size() / everyTimeSize)
                : (objList.size() / everyTimeSize + 1);
        for (int i = 0; i < times; i++) {
            if ((i + 1) * everyTimeSize >= objList.size()) {
                execute.execute(objList.subList(i * everyTimeSize, objList.size()));
            } else {
                execute.execute(objList.subList(i * everyTimeSize, (i + 1) * everyTimeSize));
            }
        }
    }

    private FunctionUtil() {}

}
