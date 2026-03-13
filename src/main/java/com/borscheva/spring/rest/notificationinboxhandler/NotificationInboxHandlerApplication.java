package com.borscheva.spring.rest.notificationinboxhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NotificationInboxHandlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationInboxHandlerApplication.class, args);
    }

}