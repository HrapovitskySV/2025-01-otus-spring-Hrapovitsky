package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.TestService;

@RequiredArgsConstructor
@ShellComponent
public class TestCommands {

    private final TestService testService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    private final LoginContext loginContext;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(String firstName, String lastName) {
        loginContext.login(firstName, lastName);
        return ioService.getMessage("TestCommands.welcome", firstName, lastName);
    }

    @ShellMethod(value = "Run test", key = {"t", "test"})
    @ShellMethodAvailability(value = "isExecuteTestCommandAvailable")
    public void executeTest() {
        var testResult = testService.executeTestFor(loginContext.getStudent());
        resultService.showResult(testResult);
    }


    private Availability isExecuteTestCommandAvailable() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable(ioService.getMessage("TestCommands.log.in.first"));
    }
}
