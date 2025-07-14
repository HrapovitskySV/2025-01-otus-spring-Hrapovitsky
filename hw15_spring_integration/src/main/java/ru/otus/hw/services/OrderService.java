package ru.otus.hw.services;

import ru.otus.hw.models.Order;
import ru.otus.hw.models.Email;


public interface OrderService {

    Order createOrder(Email email);

}
