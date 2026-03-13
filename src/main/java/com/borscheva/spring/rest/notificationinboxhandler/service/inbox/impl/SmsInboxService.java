package com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl;

import com.borscheva.spring.rest.notificationinboxhandler.mapper.InboxMapper;
import com.borscheva.spring.rest.notificationinboxhandler.model.SmsInbox;
import com.borscheva.spring.rest.notificationinboxhandler.repository.SmsInboxRepository;
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
public class SmsInboxService implements InboxService<SmsInbox> {

    private final SmsInboxRepository smsInboxRepository;
    private final InboxMapper inboxMapper;

    @Override
    @Transactional
    public Optional<SmsInbox> saveEvent(String messageKey, String payload, String topic) {
        String payloadHash = inboxMapper.generatePayloadHash(messageKey, payload);

        if (existsByKeyAndHash(messageKey, payloadHash)) {
            log.info("Дубликат SMS сообщения: key={}", messageKey);
            return Optional.empty();
        }

        SmsInbox inbox = inboxMapper.toSmsInbox(messageKey, payload, topic);

        try {
            SmsInbox saved = smsInboxRepository.save(inbox);
            log.info("Сохранено SMS сообщение: key={}", messageKey);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("SMS сообщение уже существует: key={}", messageKey);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByKeyAndHash(String messageKey, String payloadHash) {
        return smsInboxRepository.findByMessageKeyAndPayloadHash(messageKey, payloadHash).isPresent();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id, String error) {
        smsInboxRepository.incrementAttempt(id, error);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        smsInboxRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SmsInbox> findUnprocessed(Pageable pageable) {
        return smsInboxRepository.findUnprocessed(pageable);
    }
}