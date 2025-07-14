package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Email;
import ru.otus.hw.models.Order;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;


@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final IncomingCorrespondenceGateway incomingCorrespondence;

    private final AtomicInteger counter = new AtomicInteger(0);

    private final List<Email> emails = List.of(
            new Email("1@ozon.ru", "Order", "book,notebook"),
            new Email("10@ozon.ru", "Info", "Buy our devices!"),
            new Email("2@ozon.ru", "Question", "Do you work today?"),
            new Email("3@ozon.ru", "Order", "pizza,tea"),
            new Email("3@ozon.ru", "Order", "burger,cola"),
            new Email("5@ozon.ru", "Question", "This is the laundry room?"),
            new Email("6@ozon.ru", "Order", "pasta, tea"),
            new Email("7@ozon.ru", "Info", "Buy our elephants!"),
            new Email("8@ozon.ru", "Order", "cake, coffee"),
            new Email("9@ozon.ru", "Order", "sausage")

    );

    @Override
    public void startGenerateEmailsLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            int num = i + 1;
            pool.execute(() -> {
                Email email = getEmailMessage(num - 1);
                log.info("{}, New email: {}", num, email.getMessage());
                Order order = incomingCorrespondence.process(email);
                if (!isNull(order)) {
                    log.info("{}, Ready order: {}", num, order.getOrderId());
                }
            });
        }
    }


    public Email getEmailMessage(int i) {
        return emails.get(i);
    }

    public void info(Email email) {
        log.info("Information Message {}", email.toString());
    }

}
