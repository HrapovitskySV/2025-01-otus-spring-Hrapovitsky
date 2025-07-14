package ru.otus.hw.services;


import ru.otus.hw.models.Email;



public interface EmailService {

    void startGenerateEmailsLoop();

    void info(Email email);
}
