package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.models.Email;
import ru.otus.hw.models.Order;

@MessagingGateway
public interface IncomingCorrespondenceGateway {

    @Gateway(requestChannel = "emailChannel", replyChannel = "orderChannel")
    Order process(Email email);
}
