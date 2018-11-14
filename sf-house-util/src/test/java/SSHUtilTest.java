import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.IdUtil;
import sf.house.util.ssh.SSHUtil;

/**
 * Created by hznijianfeng on 2018/11/12.
 */

@Slf4j
public class SSHUtilTest {

    public static String path;

    static {
        String targetPath = SSHUtilTest.class.getResource("/").getPath();
        if (targetPath.contains("C:")) {
            path = targetPath.substring(1, targetPath.indexOf("target")) + "src/main/resources/";
        } else {
            path = targetPath.substring(0, targetPath.indexOf("target")) + "src/main/resources/";
        }
        log.info("文件位置:" + path);
    }

    public static void main(String[] args) throws Exception {
        String home = System.getProperty("user.home");
        log.info("############## {}", home);

        SSHUtil.SSHInfo sshInfo = new SSHUtil.SSHInfo("10.177.8.97", "hznijianfeng");
        sshInfo.setPort(1046);
        sshInfo.setCmd("pwd");
        String result = SSHUtil.execCommand(sshInfo);
        result = result.replace("\n", "");
        log.info("############## {}", result);

        sshInfo.setSourcePath(path + "/demo.log");
        sshInfo.setTargetPath(result);
        SSHUtil.uploadFile(sshInfo);

        sshInfo.setSourcePath(result + "/demo.log");
        sshInfo.setTargetPath(path + "/demo-" + IdUtil.snowflakeId() + ".log");
        SSHUtil.downloadFile(sshInfo);

        System.exit(0);
    }

}
