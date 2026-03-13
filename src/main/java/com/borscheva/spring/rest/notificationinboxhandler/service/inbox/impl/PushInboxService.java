package com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl;

import com.borscheva.spring.rest.notificationinboxhandler.mapper.InboxMapper;
import com.borscheva.spring.rest.notificationinboxhandler.model.PushInbox;
import com.borscheva.spring.rest.notificationinboxhandler.repository.PushInboxRepository;
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
public class PushInboxService implements InboxService<PushInbox> {

    private final PushInboxRepository pushInboxRepository;
    private final InboxMapper inboxMapper;

    @Override
    @Transactional
    public Optional<PushInbox> saveEvent(String messageKey, String payload, String topic) {
        String payloadHash = inboxMapper.generatePayloadHash(messageKey, payload);

        if (existsByKeyAndHash(messageKey, payloadHash)) {
            log.info("Дубликат Push сообщения: key={}", messageKey);
            return Optional.empty();
        }

        PushInbox inbox = inboxMapper.toPushInbox(messageKey, payload, topic);

        try {
            PushInbox saved = pushInboxRepository.save(inbox);
            log.info("Сохранено Push сообщение: key={}", messageKey);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("Push сообщение уже существует: key={}", messageKey);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByKeyAndHash(String messageKey, String payloadHash) {
        return pushInboxRepository.findByMessageKeyAndPayloadHash(messageKey, payloadHash).isPresent();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id, String error) {
        pushInboxRepository.incrementAttempt(id, error);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        pushInboxRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PushInbox> findUnprocessed(Pageable pageable) {
        return pushInboxRepository.findUnprocessed(pageable);
    }
}