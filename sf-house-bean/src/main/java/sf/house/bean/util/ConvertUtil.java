package sf.house.bean.util;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nijianfeng on 2018/8/18.
 */

@Slf4j
public class ConvertUtil {

    public static Map<String, Object> obj2Map(Object item) {

        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(item.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(item) : null;
                map.put(key, value);
            }
        } catch (Exception ex) {
            log.warn("[ConvertUtil][obj2Map]失败!", ex);
        }

        return map;
    }

}
