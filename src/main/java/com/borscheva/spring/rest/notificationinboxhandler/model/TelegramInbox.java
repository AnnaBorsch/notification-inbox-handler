package com.borscheva.spring.rest.notificationinboxhandler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "telegram_inbox")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramInbox extends BaseInbox {

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "parse_mode")
    private String parseMode;
}