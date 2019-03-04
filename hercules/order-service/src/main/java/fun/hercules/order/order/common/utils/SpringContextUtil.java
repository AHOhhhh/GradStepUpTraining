package fun.hercules.order.order.common.utils;

import fun.hercules.order.order.clients.CachedUserClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class SpringContextUtil implements ApplicationContextAware {

    private static Map<Class, Object> contextObjectMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        contextObjectMap.put(CachedUserClient.class, applicationContext.getBean(CachedUserClient.class));
    }

    public static Object getBean(Class clazz) throws BeansException {
        return contextObjectMap.get(clazz);
    }
}
