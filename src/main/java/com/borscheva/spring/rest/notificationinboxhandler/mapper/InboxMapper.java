package com.borscheva.spring.rest.notificationinboxhandler.mapper;

import com.borscheva.spring.rest.notificationinboxhandler.model.EmailInbox;
import com.borscheva.spring.rest.notificationinboxhandler.model.PushInbox;
import com.borscheva.spring.rest.notificationinboxhandler.model.SmsInbox;
import com.borscheva.spring.rest.notificationinboxhandler.model.TelegramInbox;
import com.borscheva.spring.rest.notificationinboxhandler.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboxMapper {

    private final HashUtil hashUtil;

    public String generatePayloadHash(String messageKey, String payload) {
        return hashUtil.generatePayloadHash(messageKey, payload);
    }

    public SmsInbox toSmsInbox(String messageKey, String payload, String topic) {
        return SmsInbox.builder()
            .messageKey(messageKey)
            .payload(payload)
            .payloadHash(generatePayloadHash(messageKey, payload))
            .processed(false)
            .attemptCount(0)
            .build();
    }

    public EmailInbox toEmailInbox(String messageKey, String payload, String topic) {
        return EmailInbox.builder()
            .messageKey(messageKey)
            .payload(payload)
            .payloadHash(generatePayloadHash(messageKey, payload))
            .processed(false)
            .attemptCount(0)
            .build();
    }

    public PushInbox toPushInbox(String messageKey, String payload, String topic) {
        return PushInbox.builder()
            .messageKey(messageKey)
            .payload(payload)
            .payloadHash(generatePayloadHash(messageKey, payload))
            .processed(false)
            .attemptCount(0)
            .build();
    }

    public TelegramInbox toTelegramInbox(String messageKey, String payload, String topic) {
        return TelegramInbox.builder()
            .messageKey(messageKey)
            .payload(payload)
            .payloadHash(generatePayloadHash(messageKey, payload))
            .processed(false)
            .attemptCount(0)
            .build();
    }
}