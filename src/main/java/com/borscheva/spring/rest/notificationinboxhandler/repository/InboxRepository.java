package com.borscheva.spring.rest.notificationinboxhandler.repository;

import com.borscheva.spring.rest.notificationinboxhandler.model.BaseInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface InboxRepository<T extends BaseInbox> extends JpaRepository<T, UUID> {

    @Query("SELECT i FROM #{#entityName} i WHERE i.processed = false ORDER BY i.createdAt ASC")
    List<T> findUnprocessed(Pageable pageable);

    Optional<T> findByMessageKeyAndPayloadHash(String messageKey, String payloadHash);

    @Modifying
    @Query("UPDATE #{#entityName} i SET i.attemptCount = i.attemptCount + 1, i.lastError = :error WHERE i.id = :id")
    void incrementAttempt(@Param("id") UUID id, @Param("error") String error);

    @Modifying
    @Query("UPDATE #{#entityName} i SET i.processed = true WHERE i.id = :id")
    void setProcessedTrue(@Param("id") UUID id);
}