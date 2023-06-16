package com.jmg.checkagro.customer.event;

import com.jmg.checkagro.customer.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;

@Component
public class RegisterCustomerProducer {
    private final RabbitTemplate rabbitTemplate;

    public RegisterCustomerProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRegisterCustomer(RegisterCustomerProducer.Data message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.TOPIC_REGISTER_CUSTOMER, message);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Data {
        private String documentType;
        private String documentValue;
    }
}
