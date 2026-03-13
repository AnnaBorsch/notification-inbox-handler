package com.borscheva.spring.rest.notificationinboxhandler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.inbox")
@Getter
@Setter
public class InboxProperties {

    private int batchSize = 50;
    private long delayMs = 1000;
    private int maxRetries = 3;
}