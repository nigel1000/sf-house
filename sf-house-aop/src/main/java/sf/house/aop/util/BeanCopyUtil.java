package sf.house.aop.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class BeanCopyUtil {

    public static <T> T emptyBean(Class<T> target) {
        T temp;
        try {
            temp = target.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("没有默认构造方法", e);
        }
        return temp;
    }

    public static <S, T> T genBean(S source, Class<T> target) {
        if (source == null) {
            return null;
        }
        T temp;
        try {
            temp = target.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("没有默认构造方法", e);
        }
        try {
            BeanUtils.copyProperties(temp, source);
        } catch (Exception e) {
            throw new RuntimeException("copyProperties出错了", e);
        }
        return temp;
    }

    public static <S, T> List<T> genBeanList(List<S> sources, Class<T> target) {
        if (sources == null || sources.size() == 0) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        sources.forEach(s -> result.add(genBean(s, target)));
        return result;
    }

    public static <T> T map2Object(Map<String, Object> map, Class<T> beanClass) {
        if (map == null)
            return null;
        T obj;
        try {
            obj = beanClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("没有默认构造方法", e);
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            throw new RuntimeException("populate出错了", e);
        }
        return obj;
    }

    public static Map<String, Object> object2Map(Object source) {
        if (source == null)
            return null;
        return new BeanMap(source);
    }

}
