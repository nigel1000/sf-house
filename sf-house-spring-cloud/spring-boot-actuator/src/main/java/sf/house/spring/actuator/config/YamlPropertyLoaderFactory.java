package sf.house.spring.actuator.config;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by nijianfeng on 2018/10/27.
 */

public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            return null;
        }
        YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
        List<PropertySource<?>> propertySources = yamlPropertySourceLoader.load(resource.getResource().getFilename(), resource.getResource());
        if (CollectionUtils.isEmpty(propertySources)) {
            return null;
        }
        return propertySources.get(0);
    }

}
