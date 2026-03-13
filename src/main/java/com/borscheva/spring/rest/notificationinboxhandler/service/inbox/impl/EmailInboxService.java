package com.borscheva.spring.rest.notificationinboxhandler.service.inbox.impl;

import com.borscheva.spring.rest.notificationinboxhandler.mapper.InboxMapper;
import com.borscheva.spring.rest.notificationinboxhandler.model.EmailInbox;
import com.borscheva.spring.rest.notificationinboxhandler.repository.EmailInboxRepository;
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
public class EmailInboxService implements InboxService<EmailInbox> {

    private final EmailInboxRepository emailInboxRepository;
    private final InboxMapper inboxMapper;

    @Override
    @Transactional
    public Optional<EmailInbox> saveEvent(String messageKey, String payload, String topic) {
        String payloadHash = inboxMapper.generatePayloadHash(messageKey, payload);

        if (existsByKeyAndHash(messageKey, payloadHash)) {
            log.info("Дубликат Email сообщения: key={}", messageKey);
            return Optional.empty();
        }

        EmailInbox inbox = inboxMapper.toEmailInbox(messageKey, payload, topic);

        try {
            EmailInbox saved = emailInboxRepository.save(inbox);
            log.info("Сохранено Email сообщение: key={}", messageKey);
            return Optional.of(saved);
        } catch (DataIntegrityViolationException e) {
            log.info("Email сообщение уже существует: key={}", messageKey);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByKeyAndHash(String messageKey, String payloadHash) {
        return emailInboxRepository.findByMessageKeyAndPayloadHash(messageKey, payloadHash).isPresent();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempt(UUID id, String error) {
        emailInboxRepository.incrementAttempt(id, error);
    }

    @Override
    @Transactional
    public void setProcessed(UUID id) {
        emailInboxRepository.setProcessedTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailInbox> findUnprocessed(Pageable pageable) {
        return emailInboxRepository.findUnprocessed(pageable);
    }
}