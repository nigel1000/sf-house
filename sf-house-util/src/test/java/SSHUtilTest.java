import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.IdUtil;
import sf.house.util.ssh.SSHUtil;

/**
 * Created by hznijianfeng on 2018/11/12.
 */

@Slf4j
public class SSHUtilTest {

    private static String path;

    static {
        String targetPath = SSHUtilTest.class.getResource("/").getPath();
        if (targetPath.contains("C:")) {
            path = targetPath.substring(1, targetPath.indexOf("target")) + "src/main/resources/";
        } else {
            path = targetPath.substring(0, targetPath.indexOf("target")) + "src/main/resources/";
        }
        log.info("文件位置:" + path);
    }

    private static String host = "10.177.34.196";

    public static void main(String[] args) throws Exception {
        String home = System.getProperty("user.home");
        log.info("############## {}", home);

        String machinePath = cmd("pwd");
        upload(machinePath);
        download(machinePath);

        forward();

        System.exit(0);
    }

    private static void forward() throws Exception {
        SSHUtil.SSHInfo sshInfo = new SSHUtil.SSHInfo(host, "hznijianfeng");
        sshInfo.setPort(1046);
        sshInfo.setLocalForward(true);
        sshInfo.setLocalPort(11111);
        sshInfo.setRemotePort(17079);
        sshInfo.getSession();

        boolean needHold = true;
        while (needHold) {
            Thread.sleep(1000 * 60 * 5);
            needHold = false;
        }
    }

    private static String cmd(String cmd) {
        SSHUtil.SSHInfo sshInfo = new SSHUtil.SSHInfo(host, "hznijianfeng");
        sshInfo.setPort(1046);
        sshInfo.setCmd(cmd);
        String result = SSHUtil.execCommand(sshInfo);
        result = result.replace("\n", "");
        log.info("############## {}", result);
        return result;
    }

    private static void upload(String machinePath) {
        SSHUtil.SSHInfo sshInfo = new SSHUtil.SSHInfo(host, "hznijianfeng");
        sshInfo.setPort(1046);
        sshInfo.setSourcePath(path + "/demo.log");
        sshInfo.setTargetPath(machinePath);
        SSHUtil.uploadFile(sshInfo);
    }

    private static void download(String machinePath) {
        SSHUtil.SSHInfo sshInfo = new SSHUtil.SSHInfo(host, "hznijianfeng");
        sshInfo.setPort(1046);
        sshInfo.setSourcePath(machinePath + "/demo.log");
        sshInfo.setTargetPath(path + "/demo-" + IdUtil.snowflakeId() + ".log");
        SSHUtil.downloadFile(sshInfo);
    }


}
