package sf.house.bean.util;

import java.util.*;

/**
 * Created by hznijianfeng on 2018/9/3.
 */

public class CollectionUtil {

    public static <T> List<T> pickRepeat(List<T> origin) {
        List<T> result = new ArrayList<>(origin);
        if (result.size() == 0) {
            return result;
        }
        Set<T> originSet = new HashSet<>(result);
        Iterator<T> it = result.iterator();
        while (it.hasNext()) {
            T obj = it.next();
            if (originSet.contains(obj)) {
                originSet.remove(obj);
                it.remove();
            }
        }
        return new ArrayList<>(new HashSet<>(result));
    }

}
