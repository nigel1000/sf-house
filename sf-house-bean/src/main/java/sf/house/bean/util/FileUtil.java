package sf.house.bean.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by hznijianfeng on 2018/8/20.
 */

@Slf4j
public class FileUtil {

    public static File saveTempFile(InputStream inputStream, @NonNull String namePrefix, @NonNull String nameSuffix) {
        return saveTempFile(getBytes(inputStream), namePrefix, nameSuffix);
    }

    public static File saveTempFile(@NonNull byte[] contents, @NonNull String namePrefix, @NonNull String nameSuffix) {
        File tempFile;
        try {
            tempFile = File.createTempFile(namePrefix, nameSuffix);
            OutputStream out = new FileOutputStream(tempFile);
            out.write(contents);
            out.flush();
            out.close();
        } catch (Exception ex) {
            log.error("[FileUtil][saveTempFile]", ex);
            return null;
        }
        return tempFile;
    }

    public static File saveTempFile(@NonNull String namePrefix, @NonNull String nameSuffix) {
        File tempFile;
        try {
            tempFile = File.createTempFile(namePrefix, nameSuffix);
        } catch (Exception ex) {
            log.error("[FileUtil][saveTempFile]", ex);
            return null;
        }
        return tempFile;
    }

    public static String genImageName(File file) {
        return IdUtil.timeDiy("image") + getImageFileSize(file) + getFileNameSuffix(file);
    }

    /**
     * 获取上传图片的宽高
     *
     * @return 格式：_width_height
     */
    public static String getImageFileSize(File file) {
        String sizeStr = "";
        try {
            BufferedImage buff = ImageIO.read(file);
            int width = buff.getWidth();
            int height = buff.getHeight();
            sizeStr += "_" + width + "_" + height;
        } catch (IOException ex) {
            log.error("[FileUtil][getImageFileSize]", ex);
        }
        return sizeStr;
    }

    public static String getFileNameSuffix(File file) {
        if (file == null) {
            return "";
        }
        return getFileNameSuffix(file.getName());
    }

    public static String getFileNamePrefix(File file) {
        if (file == null) {
            return "";
        }
        return getFileNamePrefix(file.getName());
    }

    public static String getFileNameSuffix(String fileName) {
        if (fileName == null) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getFileNamePrefix(String fileName) {
        if (fileName == null) {
            return "";
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static byte[] getBytes(@NonNull InputStream inputStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            inputStream.close();
            return outStream.toByteArray();
        } catch (Exception ex) {
            log.error("[FileUtil][getBytes]", ex);
            return null;
        }
    }

}
