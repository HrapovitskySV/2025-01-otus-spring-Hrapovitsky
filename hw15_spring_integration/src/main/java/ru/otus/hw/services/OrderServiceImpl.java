package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Email;
import ru.otus.hw.models.Order;

import java.util.Arrays;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    public Order createOrder(Email email) {

        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                email.getFrom(),
                Arrays.asList(email.getMessage().split("\\s*,\\s*"))
        );
        log.info("Created Order: {}", order);

        return order;

    }


}
