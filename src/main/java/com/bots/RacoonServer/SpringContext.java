package com.bots.RacoonServer;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/***
 * Used by POJO's to reference spring application context.
 */
@Component("spring_context")
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * @param beanClass requested class
     * @return spring managed bean instance of given class or null if doesn't exist
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) throws BeansException {
        SpringContext.context = context;
    }
}
