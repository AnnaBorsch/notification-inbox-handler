package com.borscheva.spring.rest.notificationinboxhandler.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class HashUtil {

    public String sha256(String text) {
        if (text == null) {
            return null;
        }
        return DigestUtils.sha256Hex(text);
    }

    public String generatePayloadHash(String messageKey, String payload) {
        String combined = messageKey + ":" + payload;
        return sha256(combined);
    }
}