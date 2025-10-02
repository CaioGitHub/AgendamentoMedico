package org.springdoc.core.providers;

import com.example.AgendamentoMedico.config.SpringDocControllerAdviceCompatibility;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.method.ControllerAdviceBean;

/**
 * Minimal replacement for the Springdoc {@code ControllerAdviceBeanFactory},
 * which is missing in older releases bundled with this project. It delegates
 * the actual instantiation of {@link ControllerAdviceBean} to the
 * compatibility helper so the application can run against both Spring
 * Framework constructor signatures.
 */
public class ControllerAdviceBeanFactory {

    private final BeanFactory beanFactory;

    public ControllerAdviceBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ControllerAdviceBean createControllerAdviceBean(Object bean) {
        return SpringDocControllerAdviceCompatibility.create(bean, this.beanFactory);
    }
}

