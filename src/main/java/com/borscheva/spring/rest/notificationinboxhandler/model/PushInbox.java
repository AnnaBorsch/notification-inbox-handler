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
@Table(name = "push_inbox")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PushInbox extends BaseInbox {

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "app_id")
    private String appId;
}