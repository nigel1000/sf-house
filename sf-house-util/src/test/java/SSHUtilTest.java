import lombok.extern.slf4j.Slf4j;
import sf.house.util.ssh.SSHUtil;

/**
 * Created by hznijianfeng on 2018/11/12.
 */

@Slf4j
public class SSHUtilTest {

    public static void main(String[] args) throws Exception {
        String home = System.getProperty("user.home");
        log.info("##############{}", home);
        String result = SSHUtil.execCommand("10.177.8.92", 1046, "hznijianfeng", "ls -l");
        log.info("##############{}", result);
        System.exit(0);
    }

}
