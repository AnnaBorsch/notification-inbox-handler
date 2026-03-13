package com.borscheva.spring.rest.notificationinboxhandler.service.listener;

import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.InboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.SmsInboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.EmailInboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.PushInboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.TelegramInboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageKafkaConsumer {

    private final SmsInboxService smsInboxService;
    private final EmailInboxService emailInboxService;
    private final PushInboxService pushInboxService;
    private final TelegramInboxService telegramInboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "sms-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSms(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack) {

        log.info("Получено SMS сообщение: key={}", key);
        handleEvent(message, key, topic, smsInboxService, ack);
    }

    @KafkaListener(topics = "email-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEmail(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack) {

        log.info("Получено Email сообщение: key={}", key);
        handleEvent(message, key, topic, emailInboxService, ack);
    }

    @KafkaListener(topics = "push-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePush(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack) {

        log.info("Получено Push сообщение: key={}", key);
        handleEvent(message, key, topic, pushInboxService, ack);
    }

    @KafkaListener(topics = "telegram-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTelegram(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment ack) {

        log.info("Получено Telegram сообщение: key={}", key);
        handleEvent(message, key, topic, telegramInboxService, ack);
    }

    private void handleEvent(
            String messageJson,
            String key,
            String topic,
            InboxService inboxService,
            Acknowledgment ack) {

        try {
            boolean isSaved = inboxService.saveEvent(key, messageJson, topic).isPresent();

            if (isSaved) {
                log.info("Сохранено новое сообщение: key={}, topic={}", key, topic);
            } else {
                log.info("Дубликат сообщения: key={}, topic={}", key, topic);
            }

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения: key={}, topic={}", key, topic, e);
            ack.acknowledge();
        }
    }
}