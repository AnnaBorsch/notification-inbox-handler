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
@Table(name = "email_inbox")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmailInbox extends BaseInbox {

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "subject")
    private String subject;
}