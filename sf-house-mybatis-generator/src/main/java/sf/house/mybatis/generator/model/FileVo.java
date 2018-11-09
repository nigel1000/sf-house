package sf.house.mybatis.generator.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/10/9.
 */

@Data
public class FileVo implements Serializable {

    private String filePath;
    private String fileName;
    private String fileContent;

    public FileVo(@NonNull String filePath, @NonNull String fileName, @NonNull String fileContent) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}
