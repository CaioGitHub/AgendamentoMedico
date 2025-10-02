package com.example.AgendamentoMedico.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.method.ControllerAdviceBean;

/**
 * Utility class that creates {@link ControllerAdviceBean} instances using the
 * constructor signature that is available in the running Spring Framework
 * version. Spring Framework 6.2 removed the single argument constructor and
 * replaced it with a variant that also requires a {@link BeanFactory}. Older
 * Springdoc releases still call the legacy constructor, which results in a
 * {@link NoSuchMethodError}. By resolving the appropriate constructor via
 * reflection we keep the application compatible with both generations.
 */
final class SpringDocControllerAdviceCompatibility {

    private static final Constructor<ControllerAdviceBean> TWO_ARG_CTOR;
    private static final Constructor<ControllerAdviceBean> SINGLE_ARG_CTOR;

    static {
        TWO_ARG_CTOR = findConstructor(ControllerAdviceBean.class, Object.class, BeanFactory.class);
        SINGLE_ARG_CTOR = findConstructor(ControllerAdviceBean.class, Object.class);
    }

    private SpringDocControllerAdviceCompatibility() {
    }

    static ControllerAdviceBean create(Object advice, BeanFactory beanFactory) {
        Constructor<ControllerAdviceBean> constructor =
                TWO_ARG_CTOR != null ? TWO_ARG_CTOR : SINGLE_ARG_CTOR;

        if (constructor == null) {
            throw new IllegalStateException("Unsupported ControllerAdviceBean constructor signature");
        }

        try {
            if (constructor == TWO_ARG_CTOR) {
                return constructor.newInstance(advice, beanFactory);
            }
            return constructor.newInstance(advice);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException("Failed to instantiate ControllerAdviceBean", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static Constructor<ControllerAdviceBean> findConstructor(Class<?> type, Class<?>... parameterTypes) {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return (Constructor<ControllerAdviceBean>) constructor;
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
}

