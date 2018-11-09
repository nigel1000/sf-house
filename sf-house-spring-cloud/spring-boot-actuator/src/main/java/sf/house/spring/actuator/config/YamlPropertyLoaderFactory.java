package sf.house.spring.actuator.config;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

/**
 * Created by nijianfeng on 2018/10/27.
 */

public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            return null;
        }
        return new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource(), null);
    }
}
