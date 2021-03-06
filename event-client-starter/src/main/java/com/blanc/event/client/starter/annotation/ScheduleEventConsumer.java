package com.blanc.event.client.starter.annotation;

import org.springframework.context.annotation.Bean;

import java.lang.annotation.*;

/**
 * 功能：事件消费的服务
 *
 * @author chuchengyi
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Bean
public @interface ScheduleEventConsumer {

    String version();

    int timeout();
}
