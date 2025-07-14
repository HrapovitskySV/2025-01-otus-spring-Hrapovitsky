package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.models.Email;
import ru.otus.hw.services.EmailService;
import ru.otus.hw.services.OrderService;


@Configuration
@Slf4j
public class IntegrationConfig {


    @Bean
    public MessageChannelSpec<?, ?> emailChannel() {
        return MessageChannels.queue(100);
    }

    @Bean
    public MessageChannelSpec<?, ?> orderChannel() {
        return MessageChannels.queue(100);
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public MessageChannel orderEmailChannel() {
        return MessageChannels.queue(100).getObject();
    }

    @Bean
    public MessageChannel infoEmailChannel() {
        return MessageChannels.queue(100).getObject();
    }

    @Bean
    public IntegrationFlow ordersFlow(OrderService orderService) {
        return IntegrationFlow.from(orderEmailChannel())
                .handle(orderService, "createOrder")
                .channel("orderChannel")
                .get();
    }

    @Bean
    public IntegrationFlow emailsInfoFlow(EmailService emailService) {
        return IntegrationFlow.from(infoEmailChannel())
                .handle(emailService,  "info")
                .get();
    }


    @Bean
    public IntegrationFlow emailsFlow(EmailService emailService, OrderService orderService) {
        return IntegrationFlow.from(emailChannel())
                .<Email>filter((p) -> p.getTitle().equalsIgnoreCase("order") || p.getTitle().equalsIgnoreCase("info"))
                .route(Email.class,
                        p -> p.getTitle().equalsIgnoreCase("order")  ? "orderEmailChannel" : "infoEmailChannel")
                .get();
    }

}
