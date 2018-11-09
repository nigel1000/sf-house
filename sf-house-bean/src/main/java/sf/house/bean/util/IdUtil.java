package sf.house.bean.util;

/**
 * Created by hznijianfeng on 2018/8/20.
 */

import lombok.extern.slf4j.Slf4j;
import sf.house.bean.excps.UnifiedException;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public final class IdUtil implements Serializable {

    private static final SecureRandom _RNG = new SecureRandom();

    public static String timeDiy(String diy) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timePrefix = sdf.format(new Date());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            sb.append("0123456789".charAt(_RNG.nextInt("0123456789".length())));
        }
        return timePrefix + diy + sb.toString();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    // 没有-
    public static String uuidHex() {
        return uuid().replace("-", "");
    }

    public static long snowflakeId() {
        return SnowFlake.nextId();
    }


    /**
     * Twitter_Snowflake,SnowFlake的结构如下(每部分用-分开):
     * 
     * 0 - 0000000000 0000000000 0000000000 0000000000 000 - 00000 - 00000 - 0000000000
     * 
     * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
     * 
     * 43位时间戳(毫秒级)，注意，43位时间戳（当前时间戳 - 开始时间戳)得到的值，开始时间戳一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下面程序类的 twEpoch 属性）。
     * 43位时间戳，可以使用278年，(1L << 43) / (1000L * 60 * 60 * 24 * 365) = 278
     * 
     * 10位的数据机器位，可以部署在1024个节点，包括5位 dataCenterId 和5位 workerId
     * 
     * 10位序列，毫秒内的计数，支持每个节点每毫秒(同一机器，同一时间戳)产生1023个ID序号 加起来刚好64位，为一个 Long 型。
     *
     * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
     */
    private static class SnowFlake {

        /** 开始时间戳 (2017-01-01) */
        private static final long twEpoch = 1483200000000L;

        /** 机器id所占的位数 */
        private static final long workerIdBits = 5L;
        /** 数据标识id所占的位数 */
        private static final long dataCenterIdBits = 5L;
        /** 序列在id中占的位数 */
        private static final long sequenceBits = 10L;

        /** 支持的最大机器id (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
        private static final long maxWorkerId = ~(-1L << workerIdBits);
        /** 支持的最大数据中心id，结果是31 （0x11111） */
        private static final long maxDataCenterId = ~(-1L << dataCenterIdBits);
        /** 生成序列的掩码 */
        private static final long sequenceMask = ~(-1L << sequenceBits);

        /** 机器ID向左移5位 */
        private static final long workerIdLeftShift = sequenceBits;
        /** 数据标识id向左移位数 */
        private static final long dataCenterIdLeftShift = sequenceBits + workerIdBits;
        /** 时间戳向左移位数 */
        private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

        /** 工作机器ID(0~31) */
        private long workerId;
        /** 数据中心ID(0~31) */
        private long dataCenterId;
        /** 毫秒内序列 2的sequenceBits次减1 */
        private long sequence = 0L;
        /** 上次生成ID的时间戳 */
        private long lastTimestamp = -1L;

        /**
         * 构造函数
         *
         * @param workerId 工作ID (0~31)
         * @param dataCenterId 数据中心ID (0~31)
         */
        private SnowFlake(long workerId, long dataCenterId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw UnifiedException.gen(String.format("工作ID不能大于 %d 或者小于 0", maxWorkerId));
            }
            if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
                throw UnifiedException.gen(String.format("数据中心ID不能大于 %d 或者小于 0", maxDataCenterId));
            }
            this.workerId = workerId;
            this.dataCenterId = dataCenterId;
        }

        /** 单例模式 */
        private static class SingletonInstance {
            private static long workId;
            private static long dataCenterId;
            static {
                // 配置比较麻烦 按服务器mac的属性做md5定workId值
                try {
                    // 获取本地网卡
                    byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
                    // 下面代码是把mac地址拼装成String
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        if (i != 0) {
                            sb.append("-");
                        }
                        // mac[i] & 0xFF 是为了把byte转化为正整数
                        String s = Integer.toHexString(mac[i] & 0xFF);
                        sb.append(s.length() == 1 ? 0 + s : s);
                    }
                    String macAddress = sb.toString();
                    // hash倒数10-5位
                    int hash = macAddress.hashCode();
                    log.info("初始化SnowFlake的 mac address:{}，hashCode:{}", macAddress, hash);
                    dataCenterId = (hash >> workerIdBits) & maxDataCenterId;
                    // hash倒数五位
                    workId = hash & maxWorkerId;
                } catch (Exception ex) {
                    throw UnifiedException.gen("初始化 workId 和 dataCenterId 失败!!", ex);
                }
                log.info("初始化SnowFlake的 workId:{}，dataCenterId：{}", workId, dataCenterId);
            }
            private static SnowFlake SNOW_FLAKE = new SnowFlake(workId, dataCenterId);
        }

        /**
         * 获得下一个ID (该方法是线程安全的)
         *
         * @return SnowflakeId
         */
        public static synchronized long nextId() {
            SnowFlake snowFlake = SingletonInstance.SNOW_FLAKE;
            long timestamp = System.currentTimeMillis();
            // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < snowFlake.lastTimestamp) {
                throw UnifiedException
                        .gen(String.format("时钟回退过。上个时间戳：%d，当前时间戳：%d", snowFlake.lastTimestamp, timestamp));
            }

            // 如果是同一时间生成的，则进行毫秒内序列
            if (snowFlake.lastTimestamp == timestamp) {
                snowFlake.sequence = (snowFlake.sequence + 1) & sequenceMask;
                // 毫秒内序列溢出
                if (snowFlake.sequence == 0) {
                    // 阻塞到下一个毫秒,获得新的时间戳
                    timestamp = System.currentTimeMillis();
                    while (timestamp <= snowFlake.lastTimestamp) {
                        timestamp = System.currentTimeMillis();
                    }
                }
            } else {
                // 时间戳改变，毫秒内序列重置
                snowFlake.sequence = 0L;
            }

            // 上次生成ID的时间戳
            snowFlake.lastTimestamp = timestamp;

            // 移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - twEpoch) << timestampLeftShift) //
                    | (snowFlake.dataCenterId << dataCenterIdLeftShift) //
                    | (snowFlake.workerId << workerIdLeftShift) //
                    | snowFlake.sequence;
        }
    }

}
