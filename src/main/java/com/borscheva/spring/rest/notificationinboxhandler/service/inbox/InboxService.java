package com.borscheva.spring.rest.notificationinboxhandler.service.inbox;

import com.borscheva.spring.rest.notificationinboxhandler.model.BaseInbox;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InboxService<T extends BaseInbox> {

    Optional<T> saveEvent(String messageKey, String payload, String topic);

    boolean existsByKeyAndHash(String messageKey, String payloadHash);

    void incrementAttempt(UUID id, String error);

    void setProcessed(UUID id);

    List<T> findUnprocessed(Pageable pageable);
}