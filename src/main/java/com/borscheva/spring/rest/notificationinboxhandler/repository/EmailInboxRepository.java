package com.borscheva.spring.rest.notificationinboxhandler.repository;

import com.borscheva.spring.rest.notificationinboxhandler.model.EmailInbox;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailInboxRepository extends InboxRepository<EmailInbox> {
}