package com.jmg.checkagro.check.event;

import com.jmg.checkagro.check.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RegisterCustomerConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_REGISTER_CONSUMER)
    public void listen(RegisterCustomerConsumer.Data message) {
        System.out.println("Se registro el cliente. " + message);
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
