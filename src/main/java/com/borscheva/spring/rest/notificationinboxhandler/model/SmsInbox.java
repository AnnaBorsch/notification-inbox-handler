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
@Table(name = "sms_inbox")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SmsInbox extends BaseInbox {

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "sender")
    private String sender;
}