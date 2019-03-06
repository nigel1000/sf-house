package sf.house.bean.util;

import lombok.extern.slf4j.Slf4j;
import sf.house.bean.excps.UnifiedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by hznijianfeng on 2019/3/5.
 */

@Slf4j
public class ImageUtil {

    public static String genImageName(File file) {
        return FileUtil.getFileNamePrefix(file) + getImageFileSize(file) + FileUtil.getFileNameSuffix(file);
    }

    /**
     * 获取上传图片的宽高
     * 格式：_width_height
     */
    public static String getImageFileSize(File file) {
        String size = "";
        try {
            BufferedImage buff = ImageIO.read(file);
            int width = buff.getWidth();
            int height = buff.getHeight();
            size += "_" + width + "_" + height;
        } catch (IOException ex) {
            throw UnifiedException.gen("image 长宽获取失败", ex);
        }
        return size;
    }


}
