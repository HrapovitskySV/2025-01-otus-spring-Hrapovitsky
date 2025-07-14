package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Order {

    private String orderId;

    private String customer;

    private List<String> products;

}
