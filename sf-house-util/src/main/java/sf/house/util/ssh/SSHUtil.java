package sf.house.util.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sf.house.bean.util.FileUtil;
import sf.house.bean.util.ThreadPoolUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by hznijianfeng on 2018/11/12.
 */

@Slf4j
public class SSHUtil {

    public static byte[] prvkey;
    public static byte[] pubKey;
    static {
        try {
            prvkey = FileUtil.getBytes(new FileInputStream(System.getProperty("user.home") + "/.ssh/id_rsa"));
            pubKey = FileUtil.getBytes(new FileInputStream(System.getProperty("user.home") + "/.ssh/id_rsa.pub"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String execCommand(@NonNull String ip, @NonNull Integer port, String userName, @NonNull String cmd) {
        try {
            JSch jsch = new JSch();

            // set private key for auth

            jsch.addIdentity(null, prvkey, pubKey, null);
            // jsch.addIdentity("C:\\Users\\hznijianfeng\\.ssh\\id_rsa");
            Session session = jsch.getSession(userName, ip, port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            // set auth info interactively
            // session.setUserInfo(new UserInfo(){.....});
            // session.setPassword("");
            session.connect();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd);
            // channel.setInputStream(null);
            channel.setErrStream(outStream);
            channel.setOutputStream(outStream);
            channel.connect();

            // 读取返回结果
            Future<String> readFuture = ThreadPoolUtil.submit(() -> {
                byte[] result = outStream.toByteArray();
                while (result == null || result.length == 0) {
                    Thread.sleep(500);
                    result = outStream.toByteArray();
                }
                String ret = new String(outStream.toByteArray(), StandardCharsets.UTF_8);
                if (ret.equals("!@#$%^&*()exit!@#$%^&*()")) {
                    return "未返回任何结果，可能是5秒超时，可能本身就没有返回!";
                }
                return ret;
            });
            ThreadPoolUtil.exec(() -> {
                try {
                    // 5秒后若没有主动退出就强制退出
                    Thread.sleep(5000);
                    outStream.write("!@#$%^&*()exit!@#$%^&*()".getBytes(StandardCharsets.UTF_8));
                    channel.disconnect();
                    session.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            return readFuture.get();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

}
