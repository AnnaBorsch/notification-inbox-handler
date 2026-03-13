package com.borscheva.spring.rest.notificationinboxhandler.repository;

import com.borscheva.spring.rest.notificationinboxhandler.model.PushInbox;
import org.springframework.stereotype.Repository;

@Repository
public interface PushInboxRepository extends InboxRepository<PushInbox> {
}