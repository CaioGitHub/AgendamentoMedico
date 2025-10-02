package com.example.AgendamentoMedico.config;

import org.springdoc.core.providers.ControllerAdviceBeanFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.ControllerAdviceBean;

/**
 * Overrides Springdoc's default {@link ControllerAdviceBeanFactory} so it can
 * instantiate {@link ControllerAdviceBean} using the constructor that is
 * available in the Spring Framework version bundled with Spring Boot 3.5.
 */
@Configuration
class SpringDocCompatibilityConfiguration {

    @Bean
    @Primary
    ControllerAdviceBeanFactory controllerAdviceBeanFactory(BeanFactory beanFactory) {
        return new ControllerAdviceBeanFactory(beanFactory) {
            @Override
            public ControllerAdviceBean createControllerAdviceBean(Object bean) {
                return SpringDocControllerAdviceCompatibility.create(bean, beanFactory);
            }
        };
    }
}

