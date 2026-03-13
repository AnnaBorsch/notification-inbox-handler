package com.borscheva.spring.rest.notificationinboxhandler.repository;

import com.borscheva.spring.rest.notificationinboxhandler.model.SmsInbox;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsInboxRepository extends InboxRepository<SmsInbox> {
}