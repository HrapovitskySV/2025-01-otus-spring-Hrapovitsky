package ru.otus.hw.services;


import ru.otus.hw.models.Email;

import java.util.List;

public interface EmailService {

    void startGenerateEmailsLoop();


    void info(Email email);
}
