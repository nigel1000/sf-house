package sf.house.aop.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.NonNull;
import sf.house.bean.util.FunctionUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class SplitUtil {

    public static final String COMMA_SPECIAL = ",";
    public static final String COLON_SPECIAL = ":";

    public static Splitter splitter(@NonNull String special) {
        return Splitter.on(special).trimResults().omitEmptyStrings();
    }

    public static Joiner joiner(@NonNull String special) {
        return Joiner.on(special).skipNulls();
    }

    public static List<String> splitByComma(String str) {
        return splitByComma(str, a -> a);
    }

    public static List<String> splitByColon(String str) {
        return splitByColon(str, a -> a);
    }

    public static <T> List<T> splitByComma(String str, Function<String, T> valueFunc) {
        if (str == null) {
            return Lists.newArrayList();
        }
        return FunctionUtil.valueList(splitter(COMMA_SPECIAL).splitToList(str), valueFunc);
    }

    public static <T> List<T> splitByColon(String str, Function<String, T> valueFunc) {
        if (str == null) {
            return Lists.newArrayList();
        }
        return FunctionUtil.valueList(splitter(COLON_SPECIAL).splitToList(str), valueFunc);
    }

    public static <T> String joinByComma(List<T> list) {
        if (list == null) {
            return "";
        }
        return joiner(COMMA_SPECIAL).join(list);
    }

    public static <T> String joinByColon(List<T> list) {
        if (list == null) {
            return "";
        }
        return joiner(COLON_SPECIAL).join(list);
    }

    // 批处理限流
    public static <T> void splitExecute(List<T> objList, int everyTimeSize, Consumer<List<T>> execute) {
        if (objList == null || objList.size() == 0) {
            return;
        }
        int totalPage = (objList.size() + everyTimeSize - 1) / everyTimeSize;
        for (int i = 0; i < totalPage; i++) {
            int fromIndex = i * everyTimeSize;
            int toIndex = Math.min((i + 1) * everyTimeSize, objList.size());
            execute.accept(objList.subList(fromIndex, toIndex));
        }
    }

    public static <T, R> void splitExecute(List<T> objList, int everyTimeSize, Function<List<T>, R> execute, R breakFlag) {
        if (objList == null || objList.size() == 0) {
            return;
        }
        int totalPage = (objList.size() + everyTimeSize - 1) / everyTimeSize;
        for (int i = 0; i < totalPage; i++) {
            int fromIndex = i * everyTimeSize;
            int toIndex = Math.min((i + 1) * everyTimeSize, objList.size());
            R result = execute.apply(objList.subList(fromIndex, toIndex));
            if (result == breakFlag || (result != null && result.equals(breakFlag))) {
                break;
            }
        }
    }


}
