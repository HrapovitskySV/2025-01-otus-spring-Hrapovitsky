package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import ru.otus.hw.models.Email;
import ru.otus.hw.services.EmailService;
import ru.otus.hw.services.OrderService;

import java.util.List;


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

/*
    @Bean
    @InboundChannelAdapter(channel = "rawEmailChannel", poller = @Poller(fixedDelay = "3000"))
    public MessageSource<List<String>> receiveMessage(EmailService emailService) {
            return () -> {
                List<String> email = emailService.getRawEmailMessage();
                log.info("Received emails: {}", email);
                return new GenericMessage<>(email);
            };
    }

 */

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
                //.handle(emailService, "transformation")
                //.channel("emailChannel")

                .route(Email.class,
                        p -> p.getTitle().equalsIgnoreCase("order")  ? "orderEmailChannel" : "infoEmailChannel")/*,
                        mapping ->
                                mapping.subFlowMapping("infoEmailChannel",
                                        f -> f.handle(msg -> {
                                                    log.info("Information Message {}", msg.getPayload());
                                                })

                                )
                )
                */
                .get();
    }

}
