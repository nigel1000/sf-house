package sf.house.bean.util;

import lombok.NonNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hznijianfeng on 2018/8/25.
 */

public class PathUtil {

    private static final Pattern PATH_SPECIAL_URI = Pattern.compile(".*[<>&\"].*");
    private static final Pattern FILE_SPECIAL_URI = Pattern.compile(".*[/\\\\].*");


    public static boolean hasPathSpecial(@NonNull String path) {
        if (path == null || "".equals(path.trim())) {
            return true;
        } else if (path.contains(File.separator + '.') || path.contains('.' + File.separator) || path.startsWith(".")
                || path.endsWith(".") || PATH_SPECIAL_URI.matcher(path).matches()) {
            return true;
        } else if (path.contains("..")) {
            return true;
        }
        return false;
    }

    public static boolean hasFileSpecial(@NonNull String fileName) {
        if (hasPathSpecial(fileName)) {
            return true;
        } else if (FILE_SPECIAL_URI.matcher(fileName).matches()) {
            return true;
        }
        return false;
    }

    public static String addSeparator(@NonNull String path) {
        return correctSeparator(path + File.separator);
    }

    public static String correctSeparator(@NonNull String path) {
        List<Character> separator = Arrays.asList('\\', '/');
        StringBuilder ret = new StringBuilder();
        Character preStr = null;
        char[] chars = path.toCharArray();
        for (char one : chars) {
            if (preStr != null && separator.contains(one)) {
                continue;
            }
            if (separator.contains(one)) {
                preStr = one;
                ret.append(File.separator);
            } else {
                preStr = null;
                ret.append(one);
            }
        }
        return ret.toString();
    }

}
