package ru.otus.hw.security;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;

import static java.util.Objects.nonNull;

@Component
public class InMemoryLoginContext implements LoginContext {

    @Getter
    private Student student;

    @Override
    public void login(String firstName, String lastName) {
        this.student = new Student(firstName, lastName);
    }

    @Override
    public boolean isUserLoggedIn() {
        return nonNull(student);
    }
}
