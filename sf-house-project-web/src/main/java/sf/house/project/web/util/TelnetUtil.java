package sf.house.project.web.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hznijianfeng on 2018/10/26.
 */

@Slf4j
public class TelnetUtil {

    public static String command(@NonNull String host, @NonNull Integer port, String command) throws Exception {
        log.info("telnet connection:{}", "telnet " + host + " " + port);
        TelnetClient telnet = new TelnetClient("VT220");
        telnet.setConnectTimeout(3000);
        telnet.connect(host, port);
        // 执行命令
        command(telnet, command);

        // 读取返回结果
        ExecutorService executors = Executors.newFixedThreadPool(1);
        Future<String> readFuture = executors.submit(() -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(telnet.getInputStream(), "GBK"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("dubbo>") || line.contains("elapsed:")) {
                    break;
                }
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        });
        executors.submit(() -> {
            try {
                // readLine 是阻塞方法 3秒后若没有主动退出就强制退出
                Thread.sleep(3000);
                command(telnet, "exit");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String result = readFuture.get();
        log.info("telnet return:{}", result);
        if (telnet.isConnected()) {
            telnet.disconnect();
        }
        return result;
    }

    private static void command(TelnetClient telnet, @NonNull String command) throws Exception {
        if (telnet == null) {
            return;
        }
        log.info("telnet execute:{}", command);
        OutputStream outputStream = telnet.getOutputStream();
        outputStream.write(command.getBytes("GBK"));
        outputStream.write("\n".getBytes("GBK"));
        outputStream.flush();
    }

}
