package sf.house.aop.util;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class BeanUtil {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static String[] getNotNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

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
            BeanUtils.copyProperties(source, temp, getNullPropertyNames(source));
        } catch (Exception e) {
            throw new RuntimeException("copyProperties出错了", e);
        }
        return temp;
    }

    public static <T> T genBeanIgnoreSourceNullProperty(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        return target;
    }

    public static <T> T genBeanIgnoreTargetNotNullProperty(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNotNullPropertyNames(target));
        return target;
    }

    public static <T> T genBean(Object source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <S, T> List<T> genBeanList(List<S> sources, Class<T> target) {
        if (sources == null || sources.size() == 0) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        sources.forEach(s -> result.add(genBean(s, target)));
        return result;
    }

    public static <T> T map2Obj(Map<String, Object> map, Class<T> beanClass) {
        if (map == null)
            return null;
        T obj;
        try {
            obj = beanClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("没有默认构造方法", e);
        }
        try {
            org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        } catch (Exception e) {
            throw new RuntimeException("populate出错了", e);
        }
        return obj;
    }

    public static Map<String, Object> obj2Map(Object source) {
        if (source == null)
            return null;
        return (Map<String, Object>) new BeanMap(source);
    }

}