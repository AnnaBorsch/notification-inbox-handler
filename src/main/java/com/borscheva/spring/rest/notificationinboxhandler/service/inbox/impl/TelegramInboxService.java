package com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl;

import com.borscheva.spring.rest.notificationinboxhandler.mapper.InboxMapper;
import com.borscheva.spring.rest.notificationinboxhandler.model.TelegramInbox;
import com.borscheva.spring.rest.notificationinboxhandler.repository.TelegramInboxRepository;
import com.borscheva.spring.rest.notificationinboxhandler.service.inbox.InboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramInboxService implements InboxService<TelegramInbox> {

    private final TelegramInboxRepository telegramInboxRepository;
    private final InboxMapper inboxMapper;

    @Override
    @Transactional
    public Optional<TelegramInbox> saveEvent(String messageKey, String payload, String topic) {
        String payloadHash = inboxMapper.generatePayloadHash(messageKey, payload);

        if (existsByKeyAndHash(messageKey, payloadHash)) {
            log.info("Дубликат Telegram сообщения: key={}", messageKey);
            return Optional.empty();
        }

        TelegramInbox inbox = inboxMapper.toTelegramInbox(messageKey, payload, topic);

        try {
            TelegramInbox saved = telegramInboxRepository.save(inbox);
            log.info("Сохранено Telegram сообщение: key={}", messageKey);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("Telegram сообщение уже существует: key={}", messageKey);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByKeyAndHash(String messageKey, String payloadHash) {
        return telegramInboxRepository.findByMessageKeyAndPayloadHash(messageKey, payloadHash).isPresent();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id, String error) {
        telegramInboxRepository.incrementAttempt(id, error);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        telegramInboxRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelegramInbox> findUnprocessed(Pageable pageable) {
        return telegramInboxRepository.findUnprocessed(pageable);
    }
}