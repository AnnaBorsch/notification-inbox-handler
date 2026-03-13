package com.borscheva.spring.rest.notificationinboxhandler.repository;

import com.borscheva.spring.rest.notificationinboxhandler.model.TelegramInbox;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramInboxRepository extends InboxRepository<TelegramInbox> {
}