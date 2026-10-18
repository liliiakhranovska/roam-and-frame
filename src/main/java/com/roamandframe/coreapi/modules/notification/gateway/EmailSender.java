package com.roamandframe.coreapi.modules.notification.gateway;

public interface EmailSender {

    void send(String to, String subject, String body);
}
