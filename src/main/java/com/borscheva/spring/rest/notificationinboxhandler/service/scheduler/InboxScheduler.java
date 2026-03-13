package com.borscheva.spring.rest.notificationinboxhandler.service.scheduler;

import com.borscheva.spring.rest.notificationinboxhandler.config.InboxProperties;
import com.borscheva.spring.rest.notificationinboxhandler.model.BaseInbox;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.InboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.EmailInboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.PushInboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.SmsInboxService;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl.TelegramInboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxScheduler {

    private final SmsInboxService smsInboxService;
    private final EmailInboxService emailInboxService;
    private final PushInboxService pushInboxService;
    private final TelegramInboxService telegramInboxService;
    private final InboxProperties properties;

    @Scheduled(fixedDelayString = "#{@inboxProperties.delayMs}")
    public void processAllMessages() {
        log.debug("Запуск обработки необработанных сообщений");

        processInboxMessages(smsInboxService, "SMS");
        processInboxMessages(emailInboxService, "EMAIL");
        processInboxMessages(pushInboxService, "PUSH");
        processInboxMessages(telegramInboxService, "TELEGRAM");
    }

    private <T extends BaseInbox> void processInboxMessages(InboxService<T> service, String type) {
        Pageable pageable = PageRequest.of(0, properties.getBatchSize());
        List<T> messages = service.findUnprocessed(pageable);

        if (messages.isEmpty()) {
            log.debug("Нет необработанных {} сообщений", type);
            return;
        }

        log.info("Найдено {} необработанных {} сообщений", messages.size(), type);

        for (T message : messages) {
            try {
                log.info("Обработано событие: Key: {}, Payload: {}, topic: {}",
                    message.getMessageKey(),
                    truncatePayload(message.getPayload(), 50),
                    type.toLowerCase() + "-events");

                service.setProcessed(message.getId());

            } catch (Exception e) {
                log.error("Ошибка при обработке {} сообщения с key: {}", type, message.getMessageKey(), e);
                service.incrementAttempt(message.getId(), e.getMessage());
            }
        }
    }

    private String truncatePayload(String payload, int maxLength) {
        if (payload == null || payload.length() <= maxLength) {
            return payload;
        }
        return payload.substring(0, maxLength) + "...";
    }
}