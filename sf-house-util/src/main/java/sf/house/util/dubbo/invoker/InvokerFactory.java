package sf.house.util.dubbo.invoker;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.UrlUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;
import sf.house.bean.excps.UnifiedException;

import java.util.List;

/**
 * Created by hznijianfeng on 2019/1/17.
 */
@Slf4j
public class InvokerFactory {

    private static final RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();

    public static <T> T getInstance(InvokerParam invokerParam) {
        URL registryUrl = URL.valueOf(invokerParam.getZkAddress());
        log.info("registryUrl:[{}]", registryUrl.toFullString());
        Registry registry = registryFactory.getRegistry(registryUrl);
        if (registry == null || !registry.isAvailable()) {
            throw UnifiedException.gen("没有找到 registry, registryUrl is " + registryUrl.toFullString());
        }
        URL url = new URL(invokerParam.getProtocol(), invokerParam.getHost(), invokerParam.getPort(), invokerParam.getMockClassName(), invokerParam.getParams());
        log.info("url:[{}]", url.toFullString());
        try {
            List<URL> providerUrls = registry.lookup(url);
            log.info("providerUrls:{}", providerUrls);
            if (providerUrls != null && providerUrls.size() > 0) {
                for (int i = 0; i < providerUrls.size(); i++) {
                    URL providerUrl = providerUrls.get(i);
                    try {
                        ApplicationConfig applicationConfig = new ApplicationConfig();
                        applicationConfig.setName(invokerParam.getApplicationName());
                        ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
                        referenceConfig.setUrl(UrlUtils.parseURL(providerUrl.getAddress(), invokerParam.getInvokerParam(providerUrl)).toString());
                        referenceConfig.setInterface(Class.forName(providerUrl.getServiceInterface()));
                        referenceConfig.setVersion(invokerParam.getVersion());
                        referenceConfig.setApplication(applicationConfig);
                        return referenceConfig.get();
                    } catch (ClassNotFoundException ex) {
                        throw UnifiedException.gen("没有找到 class " + url.getServiceInterface());
                    } catch (Exception ex) {
                        log.error("无法访问 interface [{}] in {},尝试下一个", url.getServiceInterface(), url.getAddress(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            throw UnifiedException.gen("registry.lookup 失败 ", ex);
        }
        throw UnifiedException.gen("没有找到匹配的接口类");
    }

}
