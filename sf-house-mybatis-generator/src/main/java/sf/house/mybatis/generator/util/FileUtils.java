package sf.house.mybatis.generator.util;


import lombok.extern.slf4j.Slf4j;
import sf.house.bean.excps.UnifiedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by nijianfeng on 18/1/29.
 */

@Slf4j
public class FileUtils {


    public static void genFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            if (!path.toFile().getParentFile().exists()) {
                path.toFile().getParentFile().mkdirs();
            }
            if (path.toFile().exists()) {
                path.toFile().delete();
            }
            log.info("生成文件:" + filePath);
            Files.write(Files.createFile(path), content.getBytes());
        } catch (Exception e) {
            throw UnifiedException.gen("生成文件失败! ", e);
        }
    }


}
