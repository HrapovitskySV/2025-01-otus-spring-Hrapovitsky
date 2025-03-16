package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;


public interface StudentService {

    Student determineCurrentStudent();
}
